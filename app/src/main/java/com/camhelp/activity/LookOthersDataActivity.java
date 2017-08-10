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
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.User;
import com.camhelp.utils.L;
import com.camhelp.utils.LookLargeImg;
import com.camhelp.utils.MiPictureHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class LookOthersDataActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LookOthersDataActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    User mUser = new User();

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
    private TextView tv_sex,tv_birthday;

    private LookLargeImg lookLargeImg = new LookLargeImg();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_others_data);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userInit();
        initcolor();
        inittitle();
        initview();
        initdata();
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
        top_tv_ok.setVisibility(View.GONE);

        top_title.setText("查看资料");
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
        sex = mUser.getSex();

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
            tv_birthday.setText(birthday);
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
    public void userInit() {
        mUser = (User) getIntent().getSerializableExtra(CommonGlobal.userobj);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.iv_others_back://背景图
                lookLargeImg.looklargeimg(photo1path,this);
                break;
            case R.id.cimg_other_avatar://头像
                lookLargeImg.looklargeimg(photo2path,this);
                break;
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

}
