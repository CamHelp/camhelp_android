package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.camhelp.adapter.MinePublishedAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.ZLMinePublishedCommonProperty;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
import com.camhelp.utils.FullyLinearLayoutManager;
import com.camhelp.utils.L;
import com.camhelp.utils.MyLinearLayoutManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我发布的内容activity
 */
public class MinePublishedActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MinePublishedActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    //    User mUser = new User();
    UserVO mUser = new UserVO();

    private CircleImageView cimg_mine_avatar;

    MinePublishedAdapter minePublishedAdapter;
    private List<ZLMinePublishedCommonProperty> zlMinePublishedCommonPropertyList;
    private RecyclerView recycler_mine_published;
    private LinearLayout ll_nodata, ll_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_published);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mUser = getLocalUserVO();
        initcolor();
        inittitle();
        initdata();
        initview();

        recycler_mine_published.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recycler_mine_published.setNestedScrollingEnabled(false);
        minePublishedAdapter = new MinePublishedAdapter(zlMinePublishedCommonPropertyList, this);
        recycler_mine_published.setAdapter(minePublishedAdapter);
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
        String nickname = mUser.getNickname();//昵称
        String photoBg = mUser.getBgpicture();//背景
        String photoAvatar = mUser.getAvatar();//头像
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        cimg_mine_avatar = (CircleImageView) findViewById(R.id.cimg_mine_avatar);
        cimg_mine_avatar.bringToFront();//把头像放在最上层不被标题栏挡住（失败！）
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(nickname);

        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + photoBg)
                .error(R.drawable.mine_bg)
                .placeholder(R.drawable.mine_bg)
                .into(fruitImageView);
        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + photoAvatar)
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(cimg_mine_avatar);
    }

    public void initview() {
        ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
        ll_recyclerView = (LinearLayout) findViewById(R.id.ll_recyclerView);
        ll_recyclerView.bringToFront();

        recycler_mine_published = (RecyclerView) findViewById(R.id.recycler_mine_published);

        if (zlMinePublishedCommonPropertyList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    public void initdata() {
        zlMinePublishedCommonPropertyList = DataSupport.findAll(ZLMinePublishedCommonProperty.class);
//        if (zlMinePublishedCommonPropertyList.size() == 0)//如果本地没有，就从服务器加载
        okhttpMinePublished(mUser.getUserID());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public User getUser() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        L.d(TAG, temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return user;
    }

    public UserVO getLocalUserVO() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        L.d(TAG, temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        UserVO userVO = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            userVO = (UserVO) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return userVO;
    }

    /**
     * 请求服务器数据
     */
    private void okhttpMinePublished(int userid) {
        final String url = CommonUrls.SERVER_USER_PUBLISHED;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().add("userid", "" + userid).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                MinePublishedActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MinePublishedActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
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
                    zlMinePublishedCommonPropertyList = gson.fromJson(dataJson, new TypeToken<List<ZLMinePublishedCommonProperty>>() {
                    }.getType());

                    MinePublishedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveLocalMinePublished();
                            recycler_mine_published.setLayoutManager(new FullyLinearLayoutManager(MinePublishedActivity.this, LinearLayoutManager.VERTICAL, true));
                            recycler_mine_published.setNestedScrollingEnabled(false);
                            minePublishedAdapter = new MinePublishedAdapter(zlMinePublishedCommonPropertyList, MinePublishedActivity.this);
                            recycler_mine_published.setAdapter(minePublishedAdapter);
                        }
                    });
                } else {
                    MinePublishedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MinePublishedActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /*把数据保存本地*/
    private void saveLocalMinePublished() {
        DataSupport.deleteAll(ZLMinePublishedCommonProperty.class);
        for (int i = 0; i < zlMinePublishedCommonPropertyList.size(); i++) {
            zlMinePublishedCommonPropertyList.get(i).save();
        }

        minePublishedAdapter.notifyDataSetChanged();
        L.d(TAG, "保存本地成功");
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
