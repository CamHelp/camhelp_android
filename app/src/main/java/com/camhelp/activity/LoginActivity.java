package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.entity.User;
import com.camhelp.utils.L;
import com.camhelp.utils.SharePrefUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "LoginActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvResultLabel;
    private CheckBox checkboxAutologin;
    private TextView tv_register, tv_forget;
    private TextView btnLoginAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        pref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void initView() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnLoginAdmin = (TextView) findViewById(R.id.btn_login_admin);
        btnLoginAdmin.setOnClickListener(this);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        tvResultLabel = (TextView) findViewById(R.id.tv_result_label);
        tvResultLabel.setText("");

        checkboxAutologin = (CheckBox) findViewById(R.id.checkbox_autologin);

        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_register.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                tvResultLabel.setText(null);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
                break;
            case R.id.btn_login_admin:
                login("admin", "admin1");
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.tv_forget:
                Intent forgetIntent = new Intent(this,ForgetPasswordActivity.class);
                startActivity(forgetIntent);
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     */
    private void login(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            tvResultLabel.setText("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tvResultLabel.setText("密码不能为空");
            return;
        }
        /**
         * 处理登陆
         * */
        String rePassword = "admin1";
        int reId = 0;             //得到用户id

        editor = pref.edit();
        if (password.equals(rePassword)) {                                   //密码正确
            if (checkboxAutologin.isChecked()) {                           //自动登录验证
                editor.putBoolean(CommonGlobal.isAutoLogin,true);
            }
            inituser();
            editor.putInt(CommonGlobal.user_id,reId);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            tvResultLabel.setText("用户名或密码错误！");
            editor.clear();
        }
        editor.apply();
    }

    private void inituser(){
        User user = new User();
        user.setUserID(0);
        user.setAccount("18084938391");
        user.setAvatar("http://www.stormstone.xin/img/avatar-storm.jpg");
        user.setBgpicture("http://www.stormstone.xin/img/about-bg.jpg");
        user.setBirthday("1997-03-10");
        user.setEmail("stormstone@qq.com");
        user.setTelephone("18883747347");
        user.setNickname("石头人m");
        user.setIntro("撵上一个时代");
        user.setSex(0);
        user.setAddress("swpu");
        user.setRoleID(1);

        saveUser(user);
    }

    public void saveUser(User user) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public User getUser() {
        String temp = pref.getString(CommonGlobal.userobj, "");
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
