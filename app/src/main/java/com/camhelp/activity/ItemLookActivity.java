package com.camhelp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camhelp.R;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.common.FindValueForID;
import com.camhelp.entity.CommomPropertyDetailsVo;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.CommonPropertyVO;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
import com.camhelp.utils.DateConversionUtils;
import com.camhelp.utils.GsonUtil;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 查看具体的内容activity
 * 先把传递过来的已经有的值显示，再加载具体内容，
 * 加载完毕再更新界面
 */
public class ItemLookActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ItemLookActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private FindValueForID findValueForID = new FindValueForID();
    private int commonPropertyID;
    private CommonPropertyVO commonPropertyVO = new CommonPropertyVO();
    //    private CommonProperty commonProperty = new CommonProperty();
    private CommomPropertyDetailsVo commonProperty = new CommomPropertyDetailsVo();

    private boolean isLike, isCollection;

    private ImageView item_top_iv_avatar;//头像
    private TextView item_top_tv_nickname, item_top_tv_createtime, item_top_iv_type;//顶部用户名，发布时间，类型
    private ImageView item_iv_pic1, item_iv_pic2, item_iv_pic3, item_iv_pic4;//四张照片
    private TextView item_look_title, item_look_intro, item_look_content;//标题，简介，详情
    private TextView item_foot_praisenum, item_foot_browsenum;//热度，浏览量
    private LinearLayout ll_look_share, ll_look_like, ll_look_collect;//分享，喜欢，收藏
    private ImageView iv_like, iv_collect;//喜欢，收藏按钮（点击改变）

    //    User mUser = new User();//用户
    UserVO mUser = new UserVO();//用户

    Dialog dialogProcess;
    DateConversionUtils dateConversionUtils = new DateConversionUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_look);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        dialogProcess = MyProcessDialog.showDialog(this);
        commonPropertyID = getIntent().getIntExtra(CommonGlobal.commonPropertyID, -1);
        commonPropertyVO = (CommonPropertyVO) getIntent().getSerializableExtra(CommonGlobal.commonProperty);

        initcolor();
        inittitle();
        initview();
        initUser();
        initFirstData();//先把传递过来的数据显示
        okhttpLookOne(commonPropertyID);//查询详细信息
//        initdata();
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

        top_title.setText("详情");
        top_return.setOnClickListener(this);
    }

    public void initview() {
        item_top_iv_avatar = (ImageView) findViewById(R.id.item_top_iv_avatar);
        item_top_iv_avatar.setOnClickListener(this);
        item_top_tv_nickname = (TextView) findViewById(R.id.item_top_tv_nickname);
        item_top_tv_createtime = (TextView) findViewById(R.id.item_top_tv_createtime);
        item_top_iv_type = (TextView) findViewById(R.id.item_top_iv_type);

        item_iv_pic1 = (ImageView) findViewById(R.id.item_iv_pic1);
        item_iv_pic2 = (ImageView) findViewById(R.id.item_iv_pic2);
        item_iv_pic3 = (ImageView) findViewById(R.id.item_iv_pic3);
        item_iv_pic4 = (ImageView) findViewById(R.id.item_iv_pic4);

        item_look_title = (TextView) findViewById(R.id.item_look_title);
        item_look_intro = (TextView) findViewById(R.id.item_look_intro);
        item_look_content = (TextView) findViewById(R.id.item_look_content);
        item_foot_praisenum = (TextView) findViewById(R.id.item_foot_praisenum);
        item_foot_browsenum = (TextView) findViewById(R.id.item_foot_browsenum);

        ll_look_share = (LinearLayout) findViewById(R.id.ll_look_share);
        ll_look_like = (LinearLayout) findViewById(R.id.ll_look_like);
        ll_look_collect = (LinearLayout) findViewById(R.id.ll_look_collect);
        ll_look_share.setOnClickListener(this);
        ll_look_like.setOnClickListener(this);
        ll_look_collect.setOnClickListener(this);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);

        /*设置字体样式*/
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonnts/Microsoft.ttf");
        item_look_title.setTypeface(typeface);
    }

    public void initFirstData() {
        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + commonPropertyVO.getAvatar())
                .placeholder(R.drawable.avatar)
                .into(item_top_iv_avatar);
        item_top_tv_nickname.setText(commonPropertyVO.getNickname());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (commonPropertyVO.getCreatetime() != null) {
            String sCreatetime = sdf.format(commonPropertyVO.getCreatetime());
            item_top_tv_createtime.setText(sCreatetime);
        }

        if (commonPropertyVO.getCategoryType() != null) {
            item_top_iv_type.setText(findValueForID.findCategoryType(commonPropertyVO.getCategoryType()));
        }

        String stitle = commonPropertyVO.getCommonTitle();

        if (stitle != null && !"".equals(stitle)) {
            item_look_title.setText(stitle);
        } else {
            item_look_title.setVisibility(View.GONE);
        }

        item_foot_praisenum.setText("" + commonPropertyVO.getPraisenum() + "热度");

        String pic1 = commonPropertyVO.getCommonPic1();
        String pic2 = commonPropertyVO.getCommonPic2();
        String pic3 = commonPropertyVO.getCommonPic3();
        String pic4 = commonPropertyVO.getCommonPic4();

        if (pic1 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic1).into(item_iv_pic1);
        } else {
            item_iv_pic1.setVisibility(View.GONE);
        }
        if (pic2 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic2).into(item_iv_pic2);
        } else {
            item_iv_pic2.setVisibility(View.GONE);
        }
        if (pic3 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic3).into(item_iv_pic3);
        } else {
            item_iv_pic3.setVisibility(View.GONE);
        }
        if (pic4 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic4).into(item_iv_pic4);
        } else {
            item_iv_pic4.setVisibility(View.GONE);
        }
    }

    public void initdata() {
        item_top_tv_nickname.setText(commonProperty.getNickname());

        if (commonProperty.getCreatetime() != null) {
            item_top_tv_createtime.setText(dateConversionUtils.sdateToString(commonProperty.getCreatetime()));
        }

        if (commonProperty.getCategoryType() != null) {
            item_top_iv_type.setText(findValueForID.findCategoryType(commonProperty.getCategoryType()));
        }

        String stitle = commonProperty.getCommonTitle();
        String sintro = commonProperty.getCommonIntro();
        String scontent = commonProperty.getCommonContent();

        if (stitle != null && !"".equals(stitle)) {
            item_look_title.setText(stitle);
        } else {
            item_look_title.setVisibility(View.GONE);
        }
        if (sintro != null && !"".equals(sintro)) {
            item_look_intro.setText(sintro);
        } else {
            item_look_intro.setVisibility(View.GONE);
        }
        if (scontent != null && !"".equals(scontent)) {
            item_look_content.setText(scontent);
        } else {
            item_look_content.setVisibility(View.GONE);
        }

        item_foot_praisenum.setText(commonProperty.getPraisenum() + "热度");
        item_foot_browsenum.setText(commonProperty.getBrowsenum() + "浏览量");

        String pic1 = commonProperty.getCommonPic1();
        String pic2 = commonProperty.getCommonPic2();
        String pic3 = commonProperty.getCommonPic3();
        String pic4 = commonProperty.getCommonPic4();

        if (pic1 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic1).into(item_iv_pic1);
        } else {
            item_iv_pic1.setVisibility(View.GONE);
        }
        if (pic2 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic2).into(item_iv_pic2);
        } else {
            item_iv_pic2.setVisibility(View.GONE);
        }
        if (pic3 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic3).into(item_iv_pic3);
        } else {
            item_iv_pic3.setVisibility(View.GONE);
        }
        if (pic4 != null) {
            Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + pic4).into(item_iv_pic4);
        } else {
            item_iv_pic4.setVisibility(View.GONE);
        }
    }

    /**
     * 请求服务器数据
     */
    private void okhttpLookOne(Integer commonid) {
        dialogProcess.show();
        final String url = CommonUrls.SERVER_COMMONLIST_ONE;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder()
                .add("commonid", "" + commonid)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                ItemLookActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemLookActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                        dialogProcess.dismiss();
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
                    final JsonObject dataJson = element.getAsJsonObject("data");
                    commonProperty = gson.fromJson(dataJson.toString(), CommomPropertyDetailsVo.class);
                    L.d(TAG, "详细信息" + dataJson.toString());
                    ItemLookActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            if (commonProperty.getUserID() != null) {
                                initdata();
                            }
                        }
                    });
                } else {
                    ItemLookActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            Toast.makeText(ItemLookActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 待完成
     * 得到对应的用户，浏览量加一
     * 检查是否已经喜欢和收藏
     */
    public void initUser() {
        item_top_iv_avatar.setImageResource(R.drawable.avatar);
        mUser = getUserVO();//得到user
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

    public UserVO getUserVO() {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                hintKbTwo();
                finish();
                break;
            case R.id.item_top_iv_avatar://点击头像查看
                Intent intentLookOtherPeople = new Intent(this, LookOtherPeopleActivity.class);
                intentLookOtherPeople.putExtra(CommonGlobal.user_id, commonPropertyVO.getUserID());//把用户id传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userAvatar, commonPropertyVO.getAvatar());//把用户头像传过去
                intentLookOtherPeople.putExtra(CommonGlobal.userNickname, commonPropertyVO.getNickname());//把用户昵称传过去
                startActivity(intentLookOtherPeople);
                break;
            case R.id.ll_look_share://分享
                Toast.makeText(this, "分享功能待完成", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_look_like://喜欢
                if (isLike) {
                    iv_like.setImageResource(R.drawable.item_foot_like);
                    deletelike();
                } else {
                    iv_like.setImageResource(R.drawable.item_foot_liked);
                    addlike();
                }
                break;
            case R.id.ll_look_collect://收藏
                if (isCollection) {
                    iv_collect.setImageResource(R.drawable.item_foot_collection);
                    deletecollection();
                } else {
                    iv_collect.setImageResource(R.drawable.item_foot_collected);
                    addcollection();
                }
                break;
        }
    }

    /*关闭软键盘*/
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * @return 增加喜欢
     */
    private boolean addlike() {
        isLike = true;
        return false;
    }

    /**
     * @return 删除喜欢
     */
    private boolean deletelike() {
        isLike = false;
        return false;
    }

    /**
     * @return 添加收藏
     */
    private boolean addcollection() {
        isCollection = true;
        return false;
    }

    /**
     * @return 删除收藏
     */
    private boolean deletecollection() {
        isCollection = false;
        return false;
    }

}