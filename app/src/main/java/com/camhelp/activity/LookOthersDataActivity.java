package com.camhelp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
import com.camhelp.utils.DateConversionUtils;
import com.camhelp.utils.L;
import com.camhelp.utils.LookLargeImg;
import com.camhelp.utils.MiPictureHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LookOthersDataActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LookOthersDataActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    //    User mUser = new User();
    UserVO mUser = new UserVO();
    private int user_id;//用户id，从上一个activity得到

    private LinearLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title, top_tv_ok;

    private String photo1path, photo2path;//背景图和头像
    private String username, intro, phone, email, address, birthday;
    private int sex;
    private Date birthdayDate;

    private ImageView iv_others_back;
    private CircleImageView cimg_other_avatar;
    private TextView tv_username, tv_intro, tv_phone, tv_email, tv_address;
    private TextView tv_sex, tv_birthday;

    private LookLargeImg lookLargeImg = new LookLargeImg();
    private DateConversionUtils dateConversionUtils = new DateConversionUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_others_data);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
//        userInit();
        user_id = getIntent().getIntExtra(CommonGlobal.user_id, 0);
        okhttpGetUser(user_id);
        initcolor();
        inittitle();
        initview();
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
        top_rl_title = (LinearLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor("#11E1E1E1"));//

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);

        top_title.setText("查看资料");
        top_tv_ok.setText("关注");
        top_return.setOnClickListener(this);
        top_tv_ok.setOnClickListener(this);
    }

    public void initview() {
        iv_others_back = (ImageView) findViewById(R.id.iv_others_back);
        cimg_other_avatar = (CircleImageView) findViewById(R.id.cimg_other_avatar);
        iv_others_back.setOnClickListener(this);
        cimg_other_avatar.setOnClickListener(this);

        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
    }

    public void initdata() {
        photo1path = mUser.getBgpicture();
        photo2path = mUser.getAvatar();
        username = mUser.getNickname();
        intro = mUser.getIntro();
        phone = mUser.getTelephone();
        email = mUser.getEmail();
        address = mUser.getAddress();
        birthday = mUser.getBirthday();

        if (mUser.getSex() == null) {
            sex = -1;
        } else {
            sex = mUser.getSex();
        }

        if (username != null || "".equals(username)) {
            tv_username.setText(username);
        }
        if (intro != null || "".equals(intro)) {
            tv_intro.setText(intro);
        }
        if (phone != null || "".equals(phone)) {
            tv_phone.setText(phone);
        }
        if (email != null || "".equals(email)) {
            tv_email.setText(email);
        }
        if (address != null || "".equals(address)) {
            tv_address.setText(address);
        }
        if (birthday != null || "".equals(birthday)) {
            tv_birthday.setText(dateConversionUtils.sdateToStringBirthday(birthday));
        }
        if (sex == 0) {
            tv_sex.setText("男");
        } else if (sex == 1) {
            tv_sex.setText("女");
        } else {
            tv_sex.setText("保密");
        }

        Glide.with(this).load(photo1path)
                .error(R.drawable.mine_bg)
                .placeholder(R.drawable.mine_bg)
                .into(iv_others_back);
        Glide.with(this).load(photo2path)
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(cimg_other_avatar);
    }

    /**
     * 获取用户
     */
    private void okhttpGetUser(int userid) {
        final String url = CommonUrls.SERVER_USER_ONE;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder()
                .add("userid", "" + userid)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                LookOthersDataActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LookOthersDataActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG", result);

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
                    mUser = gson.fromJson(dataJson, UserVO.class);

                    LookOthersDataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mUser.getUserID() != null) {
                                initdata();
                            }
                        }
                    });
                } else {
                    LookOthersDataActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LookOthersDataActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
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
            case R.id.top_tv_ok://关注
                addAttention(mUser.getUserID());
                Toast.makeText(this, "关注功能待做", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_others_back://背景图
                lookLargeImg.looklargeimg(CommonUrls.SERVER_ADDRESS_PIC + photo1path, this);
                break;
            case R.id.cimg_other_avatar://头像
                lookLargeImg.looklargeimg(CommonUrls.SERVER_ADDRESS_PIC + photo2path, this);
                break;
        }
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

    public boolean addAttention(int userid) {

        return false;
    }
}
