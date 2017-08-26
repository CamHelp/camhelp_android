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
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.adapter.HomeNewAndFocusAdapter;
import com.camhelp.adapter.MineFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
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
 * 我关注的
 */
public class MineFocusActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private LinearLayout ll_mine_focus_nodata;
    private TextView tv_mine_focus_nodata;
    private SwipeRefreshLayout srl_mine_focus;
    private RecyclerView recycler_mine_focus;

    private Handler handler = new Handler();
    boolean isLoading;
    private LinearLayoutManager mLinearLayoutManager;
    private MineFocusAdapter mineFocusAdapter;
    //    private List<CommonPropertyVO> userVOList = new ArrayList<CommonPropertyVO>();
    private List<UserVO> userVOList = new ArrayList<UserVO>();
    int muserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_focus);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        muserid = pref.getInt(CommonGlobal.user_id, 0);//得到自己的用户id
        initcolor();
        inittitle();
        initview();
        okhttpMineFocus();
//        initTestData();
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

        top_title.setText("我的关注");
        top_return.setOnClickListener(this);
    }

    private void initview() {
        ll_mine_focus_nodata = (LinearLayout) findViewById(R.id.ll_mine_focus_nodata);
        ll_mine_focus_nodata.setVisibility(View.GONE);
        tv_mine_focus_nodata = (TextView) findViewById(R.id.tv_mine_focus_nodata);
        recycler_mine_focus = (RecyclerView) findViewById(R.id.recycler_mine_focus);

        srl_mine_focus = (SwipeRefreshLayout) findViewById(R.id.srl_mine_focus);
        srl_mine_focus.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_mine_focus.setRefreshing(true);
                okhttpMineFocus();
            }
        });
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        srl_mine_focus.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
    }

    private void initTestData() {
        for (int i = 0; i < 9; i++) {
            UserVO userVo = new UserVO();
            if (i % 2 == 0) {
                userVo.setUserID(1);
                userVo.setAvatar("user/first.jpg");
            } else {
                userVo.setUserID(2);
                userVo.setAvatar("user/second.jpg");
            }
            userVo.setNickname("STORM" + i);
            userVo.setIntro("intro" + i);
            userVOList.add(userVo);
        }

        mLinearLayoutManager = new LinearLayoutManager(MineFocusActivity.this);
        recycler_mine_focus.setLayoutManager(mLinearLayoutManager);
        mineFocusAdapter = new MineFocusAdapter(userVOList, MineFocusActivity.this);
        recycler_mine_focus.setAdapter(mineFocusAdapter);
        srl_mine_focus.setRefreshing(false);
    }

    /**
     * 请求服务器数据
     */
    private void okhttpMineFocus() {
        srl_mine_focus.setRefreshing(true);

        final String url = CommonUrls.SERVER_MINE_FOCUS;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder()
                .add("userid", "" + muserid)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                MineFocusActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userVOList.size() == 0) {
                            ll_mine_focus_nodata.setVisibility(View.VISIBLE);
                            tv_mine_focus_nodata.setText("无法连接到服务器");
                        } else {
                            ll_mine_focus_nodata.setVisibility(View.GONE);
                        }
                        srl_mine_focus.setRefreshing(false);
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
                    userVOList.clear();//得到新数据把旧数据清空
                    final JsonArray dataJson = element.getAsJsonArray("data");
                    userVOList = gson.fromJson(dataJson, new TypeToken<List<UserVO>>() {
                    }.getType());

                    MineFocusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userVOList.size() == 0) {
                                ll_mine_focus_nodata.setVisibility(View.VISIBLE);
                                tv_mine_focus_nodata.setText("你还没有关注任何人");
                            }
                            mLinearLayoutManager = new LinearLayoutManager(MineFocusActivity.this);
                            recycler_mine_focus.setLayoutManager(mLinearLayoutManager);
                            mineFocusAdapter = new MineFocusAdapter(userVOList, MineFocusActivity.this);
                            recycler_mine_focus.setAdapter(mineFocusAdapter);
                            srl_mine_focus.setRefreshing(false);
                        }
                    });
                } else {
                    MineFocusActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userVOList.size() == 0) {
                                ll_mine_focus_nodata.setVisibility(View.VISIBLE);
                                tv_mine_focus_nodata.setText(msg);
                            } else {
                                ll_mine_focus_nodata.setVisibility(View.GONE);
                            }
                            srl_mine_focus.setRefreshing(false);
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
