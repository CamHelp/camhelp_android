package com.camhelp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camhelp.R;
import com.camhelp.adapter.LookOtherPeopleAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.UserVO;
import com.camhelp.entity.ZLMinePublishedCommonProperty;
import com.camhelp.utils.FullyLinearLayoutManager;
import com.camhelp.utils.L;
import com.camhelp.utils.MyProcessDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.os.Handler;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 查看其他用户发布的列表
 * 根据传过来的用户id查询
 */
public class LookOtherPeopleActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LookOtherPeopleActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private int user_id;//上一个activity传过来的用户id，根据此得到用户发布的列表
    private String  user_avatar;//上一个activity传过来的用户头像
    private String  user_nickname;//上一个activity传过来的用户昵称

    private String user_photoBg;//背景图片，待获取

    private CircleImageView cimg_mine_avatar;

    LookOtherPeopleAdapter lookOtherPeopleAdapter;
    //    private List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
    private List<ZLMinePublishedCommonProperty> zlPublishedCommonPropertyList = new ArrayList<ZLMinePublishedCommonProperty>();
    private RecyclerView recycler_publish_otherpeople;
    private LinearLayout ll_nodata, ll_recyclerView;


    Dialog dialogProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_other_people);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        user_id = getIntent().getIntExtra(CommonGlobal.user_id, 0);//得到userid
        user_avatar = getIntent().getStringExtra(CommonGlobal.userAvatar);//得到头像
        user_nickname = getIntent().getStringExtra(CommonGlobal.userNickname);//得到昵称
        okhttpOthersPublished(user_id);//查询该用户发布的列表

        initcolor();
        initview();
        inittitle();


        dialogProcess = MyProcessDialog.showDialog(this);
        dialogProcess.show();
        /**暂时利用等待2秒来加载获取的数据！！！*/
        Handler handler;
        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                recycler_publish_otherpeople.setLayoutManager(new FullyLinearLayoutManager(LookOtherPeopleActivity.this, LinearLayoutManager.VERTICAL, true));
                recycler_publish_otherpeople.setNestedScrollingEnabled(false);

                lookOtherPeopleAdapter = new LookOtherPeopleAdapter(zlPublishedCommonPropertyList, LookOtherPeopleActivity.this);
                recycler_publish_otherpeople.setAdapter(lookOtherPeopleAdapter);
                dialogProcess.dismiss();
            }
        };
        handler.sendEmptyMessageDelayed(1, 2000);//handler延迟2秒执行

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_bg);
        cimg_mine_avatar = (CircleImageView) findViewById(R.id.cimg_mine_avatar);
        cimg_mine_avatar.bringToFront();//把头像放在最上层不被标题栏挡住（失败！）
        cimg_mine_avatar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(user_nickname);

        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC+user_photoBg)
                .error(R.drawable.mine_bg)
                .placeholder(R.drawable.mine_bg)
                .into(iv_bg);
        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC+user_avatar)
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(cimg_mine_avatar);
    }

    public void initview() {
        ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
        ll_recyclerView = (LinearLayout) findViewById(R.id.ll_recyclerView);
        ll_recyclerView.bringToFront();

        recycler_publish_otherpeople = (RecyclerView) findViewById(R.id.recycler_publish_otherpeople);

        if (zlPublishedCommonPropertyList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cimg_mine_avatar://点击头像查看用户信息
                Intent intentLookOthersData = new Intent(this, LookOthersDataActivity.class);
                intentLookOthersData.putExtra(CommonGlobal.user_id, user_id);
                startActivity(intentLookOthersData);
                break;
        }
    }

    /**
     * 请求服务器数据
     */
    private void okhttpOthersPublished(int userid) {
        final String url = CommonUrls.SERVER_USER_PUBLISHED;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().add("userid", "" + userid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                LookOtherPeopleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LookOtherPeopleActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
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
                    final JsonArray dataJson = element.getAsJsonArray("data");
                    zlPublishedCommonPropertyList = gson.fromJson(dataJson, new TypeToken<List<ZLMinePublishedCommonProperty>>() {
                    }.getType());

                    LookOtherPeopleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } else {
                    LookOtherPeopleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LookOtherPeopleActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}