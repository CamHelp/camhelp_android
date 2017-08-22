package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.utils.MyProcessDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryEachActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private LinearLayout top_ll_title_search;
    private ImageButton top_return,top_imgbtn_search;
    private TextView top_title;

    String sCategoryType,eachUrl;//类型，服务器地址

    private LinearLayout ll_categoryeach_nodata;
    private TextView tv_categoryEach_nodata;
    private SwipeRefreshLayout srl_categoryEach;
    private RecyclerView recycler_categoryEach;

    private Handler handler = new Handler();
    boolean isLoading;
    private LinearLayoutManager mLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    private List<CommonPropertyVO> commonPropertyVOList = new ArrayList<CommonPropertyVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_each);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        sCategoryType = intent.getStringExtra(CommonGlobal.categoryType);
        initcolor();
        initEachUrl();
        initTitle();
        initview();
        okhttpCategoryEach();
    }


    /*获取主题色*/
    public void initcolor() {
        String defaultColorPrimary = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String defaultColorPrimaryBlew = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String defaultColorPrimaryDark = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String defaultColorAccent = "#" + Integer.toHexString(getResources().getColor(R.color.colorAccent));

        colorPrimary = pref.getString(CommonGlobal.colorPrimary, defaultColorPrimary);
        colorPrimaryBlew = pref.getString(CommonGlobal.colorPrimaryBlew, defaultColorPrimaryBlew);
        colorPrimaryDark = pref.getString(CommonGlobal.colorPrimaryDark, defaultColorPrimaryDark);
        colorAccent = pref.getString(CommonGlobal.colorAccent, defaultColorAccent);
    }

    private void initTitle(){
        top_ll_title_search = (LinearLayout) findViewById(R.id.top_ll_title_search);
        top_ll_title_search.setBackgroundColor(Color.parseColor(colorPrimary));
        top_return = (ImageButton) findViewById(R.id.top_return);
        top_return.setOnClickListener(this);
        top_imgbtn_search = (ImageButton) findViewById(R.id.top_imgbtn_search);
        top_imgbtn_search.setVisibility(View.GONE);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(sCategoryType+"（暂时使用最新列表）");
    }

    /*根据类型确定服务器获取地址*/
    private void initEachUrl(){//待确定，暂时都使用获取所有地址
        if (CommonGlobal.sCategoryType_focus.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_new.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_hot.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_fresh.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_experience.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_lose.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_pickup.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_problem.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }else if (CommonGlobal.sCategoryType_unburden.equals(sCategoryType)){
            eachUrl = CommonUrls.SERVER_COMMONLIST_ALL;
        }
    }

    private void initview(){
        ll_categoryeach_nodata = (LinearLayout) findViewById(R.id.ll_categoryeach_nodata);
        ll_categoryeach_nodata.setVisibility(View.GONE);
        tv_categoryEach_nodata = (TextView) findViewById(R.id.tv_categoryEach_nodata);
        recycler_categoryEach = (RecyclerView) findViewById(R.id.recycler_categoryEach);

        srl_categoryEach = (SwipeRefreshLayout) findViewById(R.id.srl_categoryEach);
        srl_categoryEach.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_categoryEach.setRefreshing(true);
                okhttpCategoryEach();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_categoryEach.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );

        /**滑动到底部自动加载更多*/
        recycler_categoryEach.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisiableItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiableItemPosition + 1 == homeNewAndFocusAdapter.getItemCount()) {
                    boolean isRefreshing = srl_categoryEach.isRefreshing();
                    if (isRefreshing) {
                        homeNewAndFocusAdapter.notifyItemRemoved(homeNewAndFocusAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        ll_categoryeach_nodata.setVisibility(View.VISIBLE);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadmoredata();
                                isLoading = false;
                            }
                        }, 2000);
                    }
                }
            }
        });
    }

    /**
     * 加载更多
     */
    public void loadmoredata() {
        int total = commonPropertyVOList.size();
        for (int i = 0; i < total; i++) {
            commonPropertyVOList.add(commonPropertyVOList.get(i));
        }
        homeNewAndFocusAdapter.notifyDataSetChanged();
        srl_categoryEach.setRefreshing(false);
        ll_categoryeach_nodata.setVisibility(View.GONE);
    }

    /**
     * 请求服务器数据
     */
    private void okhttpCategoryEach() {
        srl_categoryEach.setRefreshing(true);

        final String url = eachUrl;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                CategoryEachActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonPropertyVOList.size() == 0){
                            ll_categoryeach_nodata.setVisibility(View.VISIBLE);
                            tv_categoryEach_nodata.setText("无法连接到服务器");
                        }else {
                            ll_categoryeach_nodata.setVisibility(View.GONE);
                        }
                        srl_categoryEach.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG" + "onresponse result:", result);

                Gson gson = new Gson();
                //  获得 解析者
                JsonParser parser = new JsonParser();
                //  获得 根节点元素
                JsonElement root = parser.parse(result);
                //  根据 文档判断根节点属于 什么类型的 Gson节点对象
                // 假如文档 显示 根节点 为对象类型
                // 获得 根节点 的实际 节点类型
                JsonObject element = root.getAsJsonObject();
                //  取得 节点 下 的某个节点的 value
                // 获得 name 节点的值，name 节点为基本数据节点
                JsonPrimitive codeJson = element.getAsJsonPrimitive("code");
                int code = codeJson.getAsInt();
                JsonPrimitive msgJson = element.getAsJsonPrimitive("msg");
                final String msg = msgJson.getAsString();

                if (code == 0) {
                    commonPropertyVOList.clear();//得到新数据把旧数据清空
                    final JsonArray dataJson = element.getAsJsonArray("data");
                    commonPropertyVOList = gson.fromJson(dataJson, new TypeToken<List<CommonPropertyVO>>() {
                    }.getType());

                    CategoryEachActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() > 0) {
                                ll_categoryeach_nodata.setVisibility(View.GONE);
                            }
                            mLinearLayoutManager = new LinearLayoutManager(CategoryEachActivity.this);
                            recycler_categoryEach.setLayoutManager(mLinearLayoutManager);
                            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, CategoryEachActivity.this);
                            recycler_categoryEach.setAdapter(homeNewAndFocusAdapter);
                            srl_categoryEach.setRefreshing(false);
                        }
                    });
                } else {
                    CategoryEachActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() == 0){
                                ll_categoryeach_nodata.setVisibility(View.VISIBLE);
                                tv_categoryEach_nodata.setText(msg);
                            }else {
                                ll_categoryeach_nodata.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_return:
                finish();
                break;
            case R.id.top_imgbtn_search:
                break;
        }
    }
}
