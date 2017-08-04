package com.camhelp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.common.CommonGlobal;

/**
 * 登录界面
 * 从本地数据库进行用户名和密码判断
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

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
                Toast.makeText(this, "待完成", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forget:
                Toast.makeText(this, "待完成", Toast.LENGTH_SHORT).show();
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

        if (password.equals(rePassword)) {                                   //密码正确
            if (checkboxAutologin.isChecked()) {                           //自动登录验证
                CommonGlobal.autoLogin = true;
            }
            CommonGlobal.loginUserId = reId;                //记录用户id
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            tvResultLabel.setText("用户名或密码错误！");
        }
    }

}
