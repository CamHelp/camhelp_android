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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonPropertyVO;
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

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private String searchContent;
    private ImageButton top_return;
    private TextView top_title, top_tv_ok;
    private LinearLayout top_rl_title;

    private LinearLayout ll_searchResult_nodata;
    private TextView tv_searchResult_nodata;
    private SwipeRefreshLayout srl_searchResult;
    private RecyclerView recycler_searchResult;
    private TextView tv_loading_searchresult;

    private Handler handler = new Handler();
    boolean isLoading;
    private LinearLayoutManager mLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    private List<CommonPropertyVO> commonPropertyVOList = new ArrayList<CommonPropertyVO>();
    private List<CommonPropertyVO> commonPropertyVOListMore = new ArrayList<CommonPropertyVO>();

    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        searchContent = intent.getStringExtra(CommonGlobal.searchContent);
        initcolor();
        initTitle(searchContent);
        initview();
        srl_searchResult.setRefreshing(true);
        okhttpSearchResult();
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

    private void initTitle(String searchContent) {
        top_rl_title = (LinearLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));
        top_return = (ImageButton) findViewById(R.id.top_return);
        top_return.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(searchContent);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);
        top_tv_ok.setVisibility(View.GONE);
    }

    private void initview() {
        ll_searchResult_nodata = (LinearLayout) findViewById(R.id.ll_searchResult_nodata);
        ll_searchResult_nodata.setVisibility(View.GONE);
        tv_searchResult_nodata = (TextView) findViewById(R.id.tv_searchResult_nodata);
        recycler_searchResult = (RecyclerView) findViewById(R.id.recycler_searchResult);
        tv_loading_searchresult = (TextView) findViewById(R.id.tv_loading_searchresult);

        srl_searchResult = (SwipeRefreshLayout) findViewById(R.id.srl_searchResult);
        srl_searchResult.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_searchResult.setRefreshing(true);
                page = 0;//页数恢复为0
                okhttpSearchResult();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_searchResult.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );

        /*搜索没有加载更多*/
//        /**滑动到底部自动加载更多*/
//        recycler_searchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                int lastVisiableItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
//                if (lastVisiableItemPosition + 1 == homeNewAndFocusAdapter.getItemCount()) {
//                    boolean isRefreshing = srl_searchResult.isRefreshing();
//                    if (isRefreshing) {
//                        homeNewAndFocusAdapter.notifyItemRemoved(homeNewAndFocusAdapter.getItemCount());
//                        return;
//                    }
//                    if (!isLoading) {
//                        isLoading = true;
//                        tv_loading_searchresult.setVisibility(View.VISIBLE);
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                page = page + 1;//加载更多，页数加一
//                                loadmoredata();
//                                isLoading = false;
//                            }
//                        }, 500);
//                    }
//                }
//            }
//        });
    }

    /**
     * 加载更多
     */
    public void loadmoredata() {
        final String url = CommonUrls.SERVER_SEARCH;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder()
                .add("title", searchContent)
                .add("page", "" + page)
                .add("size", "" + CommonGlobal.PAGE_SIZE).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                SearchResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchResultActivity.this, "加载更多失败", Toast.LENGTH_SHORT).show();
                        tv_loading_searchresult.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG" + "onresponse result:", result);

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonElement root = parser.parse(result);
                JsonObject element = root.getAsJsonObject();
                JsonPrimitive codeJson = element.getAsJsonPrimitive("code");
                int code = codeJson.getAsInt();
                JsonPrimitive msgJson = element.getAsJsonPrimitive("msg");
                final String msg = msgJson.getAsString();

                if (code == 0) {
                    commonPropertyVOListMore.clear();//得到新数据把旧数据清空
                    final JsonArray dataJson = element.getAsJsonArray("data");
                    commonPropertyVOListMore = gson.fromJson(dataJson, new TypeToken<List<CommonPropertyVO>>() {
                    }.getType());

                    SearchResultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < commonPropertyVOListMore.size(); i++) {//把更多数据添加进列表
                                commonPropertyVOList.add(commonPropertyVOListMore.get(i));
                            }
                            if (commonPropertyVOList.size() > 0) {
                                ll_searchResult_nodata.setVisibility(View.GONE);
                            }
                            homeNewAndFocusAdapter.notifyDataSetChanged();
                            tv_loading_searchresult.setVisibility(View.GONE);
                        }
                    });
                } else {
                    SearchResultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_loading_searchresult.setVisibility(View.GONE);
//                            Toast.makeText(CategoryEachActivity.this, "已加载全部" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    /**
     * 请求服务器数据
     */
    private void okhttpSearchResult() {

        final String url = CommonUrls.SERVER_SEARCH;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder()
                .add("title", searchContent)
                .add("page", "" + page)
                .add("size", "" + CommonGlobal.PAGE_SIZE).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                SearchResultActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonPropertyVOList.size() == 0) {
                            ll_searchResult_nodata.setVisibility(View.VISIBLE);
                            tv_searchResult_nodata.setText("无法连接到服务器");
                        } else {
                            ll_searchResult_nodata.setVisibility(View.GONE);
                        }
                        srl_searchResult.setRefreshing(false);
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

                    SearchResultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() > 0) {
                                ll_searchResult_nodata.setVisibility(View.GONE);
                            }
                            mLinearLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
                            recycler_searchResult.setLayoutManager(mLinearLayoutManager);
                            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, SearchResultActivity.this);
                            recycler_searchResult.setAdapter(homeNewAndFocusAdapter);
                            srl_searchResult.setRefreshing(false);
                        }
                    });
                } else {
                    SearchResultActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() == 0) {
                                ll_searchResult_nodata.setVisibility(View.VISIBLE);
                                tv_searchResult_nodata.setText(msg+":没有结果");
                            } else {
                                ll_searchResult_nodata.setVisibility(View.GONE);
                            }
                            srl_searchResult.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
        }
    }
}
