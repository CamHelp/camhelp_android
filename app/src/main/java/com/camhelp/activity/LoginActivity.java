package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

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

}
