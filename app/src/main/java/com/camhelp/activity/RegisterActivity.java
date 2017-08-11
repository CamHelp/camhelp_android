package com.camhelp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonUrls;
import com.camhelp.utils.L;
import com.camhelp.utils.MyProcessDialog;
import com.camhelp.utils.NumFormatCheckUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注册activity
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "RegisterActivity";
    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private EditText et_username, et_password;
    private TextView tv_result_label;
    private Button btn_register;

    Dialog dialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inittitle();
        initview();
    }

    public void inittitle() {
        top_rl_title = (RelativeLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);

        top_title.setText("注册");
        top_return.setOnClickListener(this);
    }

    public void initview() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        tv_result_label = (TextView) findViewById(R.id.tv_result_label);
        tv_result_label.setText(null);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                this.finish();
                break;
            case R.id.btn_register:
                tv_result_label.setText(null);
                String telephone = et_username.getText().toString();
                String password = et_password.getText().toString();
                register(telephone, password);
                break;
        }
    }

    public void register(String telephone, String password) {
        if (TextUtils.isEmpty(telephone)) {
            tv_result_label.setText("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            tv_result_label.setText("请输入密码");
            return;
        }
        if (!NumFormatCheckUtils.isPhoneLegal(telephone)){
            tv_result_label.setText("请输入正确的手机号");
            return;
        }
        if (!NumFormatCheckUtils.passwordFormat(password)){
            tv_result_label.setText("密码应为不含空格的6-16位任意字符");
            return;
        }
        /**
         * 处理登陆
         * */
        dialogProcess = MyProcessDialog.showDialog(RegisterActivity.this);
        dialogProcess.show();
        okhttpRegister(telephone, password);
    }



    boolean loginsuccess;

    private boolean okhttpRegister(String telephone, String password) {
        final String url = CommonUrls.SERVER_REGISTER;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        FormBody body = new FormBody.Builder().add("telephone", telephone).add("password", password).build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                dialogProcess.dismiss();
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_result_label.setText("无法连接到服务器！");
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

                if (code == 0) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                } else {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            tv_result_label.setText(msg);
                        }
                    });
                }
            }
        });
        return loginsuccess;
    }
}
