package com.camhelp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
import com.camhelp.utils.L;
import com.camhelp.utils.MyProcessDialog;
import com.camhelp.utils.SharePrefUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    User user = new User();
    UserVO userVO = new UserVO();
    Dialog dialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initTestUserInfo();//填写默认信息，方便调试
    }

    public void initTestUserInfo(){
        etUsername.setText("18084938391");
        etPassword.setText("123456");
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
            case R.id.btn_login_admin://调试默认登录
//                login("18084938391", "123456");
                initTestuser();
                editor = pref.edit();
                if (checkboxAutologin.isChecked()) {                           //自动登录验证
                    editor.putBoolean(CommonGlobal.isAutoLogin, true);
                }
                editor.putInt(CommonGlobal.user_id, userVO.getUserID());
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainViewpaperActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.tv_forget:
                Intent forgetIntent = new Intent(this, ForgetPasswordActivity.class);
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
        dialogProcess = MyProcessDialog.showDialog(LoginActivity.this);
        dialogProcess.show();
        okhttpLogin(username, password);
    }

    private void initTestuser() {
        userVO.setUserID(0);
        userVO.setAccount("18084938391");
        userVO.setAvatar("http://www.stormstone.xin/img/avatar-storm.jpg");
        userVO.setBgpicture("http://www.stormstone.xin/img/about-bg.jpg");
        userVO.setBirthday("1997-03-10");
        userVO.setEmail("stormstone@qq.com");
        userVO.setTelephone("18883747347");
        userVO.setNickname("石头人m");
        userVO.setIntro("撵上一个时代");
        userVO.setSex(0);
        userVO.setAddress("swpu");
        userVO.setRoleID(1);

        saveUserVO(userVO);
    }

    public void saveUser(User user) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        editor = pref.edit();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        editor.apply();
    }
    public void saveUserVO(UserVO userVO) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        editor = pref.edit();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(userVO);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        editor.apply();
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

    boolean loginsuccess;
    private boolean okhttpLogin(String telephone, String password) {
        final String url = CommonUrls.SERVER_LOGIN;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().add("telephone", telephone).add("password", password).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                dialogProcess.dismiss();
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResultLabel.setText("无法连接到服务器！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG", result);

                loginsuccess = true;
                L.d(TAG, "" + loginsuccess);

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

                if (code==0) {

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            Toast.makeText(LoginActivity.this, "登录"+msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                    final JsonObject dataJson = element.getAsJsonObject("data");
                    userVO = gson.fromJson(dataJson, UserVO.class);

                    saveUserVO(userVO);
                    L.d(TAG, "得到的userVO:" + userVO.toString());
                    editor = pref.edit();
                    if (checkboxAutologin.isChecked()) {                           //自动登录验证
                        editor.putBoolean(CommonGlobal.isAutoLogin, true);
                    }
                    editor.putInt(CommonGlobal.user_id, userVO.getUserID());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainViewpaperActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            tvResultLabel.setText(msg);
                        }
                    });
                }
            }
        });
        return loginsuccess;
    }
}
