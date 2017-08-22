package com.camhelp.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

/**
 * 我的收藏
 * 暂时还是获取最新的列表
 */
public class MineCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private LinearLayout ll_mine_collection_nodata;
    private TextView tv_mine_collection_nodata;
    private SwipeRefreshLayout srl_mine_collection;
    private RecyclerView recycler_mine_collection;

    private Handler handler = new Handler();
    boolean isLoading;
    private LinearLayoutManager mLinearLayoutManager;
    private HomeNewAndFocusAdapter homeNewAndFocusAdapter;
    private List<CommonPropertyVO> commonPropertyVOList = new ArrayList<CommonPropertyVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_collection);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initcolor();
        inittitle();
        initview();
        okhttpMineCollection();
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

    public void inittitle() {
        top_rl_title = (RelativeLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);

        top_title.setText("我的收藏");
        top_return.setOnClickListener(this);
    }


    private void initview(){
        ll_mine_collection_nodata = (LinearLayout) findViewById(R.id.ll_mine_collection_nodata);
        ll_mine_collection_nodata.setVisibility(View.GONE);
        tv_mine_collection_nodata = (TextView) findViewById(R.id.tv_mine_collection_nodata);
        recycler_mine_collection = (RecyclerView) findViewById(R.id.recycler_mine_collection);

        srl_mine_collection = (SwipeRefreshLayout) findViewById(R.id.srl_mine_collection);
        srl_mine_collection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_mine_collection.setRefreshing(true);
                okhttpMineCollection();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_mine_collection.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
    }

    /**
     * 请求服务器数据
     */
    private void okhttpMineCollection() {
        srl_mine_collection.setRefreshing(true);

        final String url = CommonUrls.SERVER_COMMONLIST_ALL;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                MineCollectionActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (commonPropertyVOList.size() == 0){
                            ll_mine_collection_nodata.setVisibility(View.VISIBLE);
                            tv_mine_collection_nodata.setText("无法连接到服务器");
                        }else {
                            ll_mine_collection_nodata.setVisibility(View.GONE);
                        }
                        srl_mine_collection.setRefreshing(false);
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

                    MineCollectionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() == 0) {
                                ll_mine_collection_nodata.setVisibility(View.VISIBLE);
                            }
                            mLinearLayoutManager = new LinearLayoutManager(MineCollectionActivity.this);
                            recycler_mine_collection.setLayoutManager(mLinearLayoutManager);
                            homeNewAndFocusAdapter = new HomeNewAndFocusAdapter(commonPropertyVOList, MineCollectionActivity.this);
                            recycler_mine_collection.setAdapter(homeNewAndFocusAdapter);
                            srl_mine_collection.setRefreshing(false);
                        }
                    });
                } else {
                    MineCollectionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (commonPropertyVOList.size() == 0){
                                ll_mine_collection_nodata.setVisibility(View.VISIBLE);
                                tv_mine_collection_nodata.setText(msg);
                            }else {
                                ll_mine_collection_nodata.setVisibility(View.GONE);
                            }
                            srl_mine_collection.setRefreshing(false);
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
