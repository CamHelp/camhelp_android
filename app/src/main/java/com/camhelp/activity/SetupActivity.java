package com.camhelp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camhelp.R;
import com.camhelp.basic.ActivityCollector;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;

public class SetupActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    public static SetupActivity mInstace = null;//用于其他activity关闭此activity

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;


    LinearLayout ll_check_version_update, ll_exit_system, ll_log_out,
            ll_feedback, ll_color_change;
    Button btn_log_out;

    private Dialog choosedialog = null;//确认框
    private int EXITORLOGOUT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mInstace =this;
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initcolor();
        inittitle();
        initview();
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

        top_title.setText("设置");
        top_return.setOnClickListener(this);
    }

    public void initview() {
        ll_check_version_update = (LinearLayout) findViewById(R.id.ll_check_version_update);
        ll_exit_system = (LinearLayout) findViewById(R.id.ll_exit_system);
        btn_log_out = (Button) findViewById(R.id.btn_log_out);
        ll_log_out = (LinearLayout) findViewById(R.id.ll_log_out);
        ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
        ll_color_change = (LinearLayout) findViewById(R.id.ll_color_change);

        ll_check_version_update.setOnClickListener(this);
        ll_exit_system.setOnClickListener(this);
        btn_log_out.setOnClickListener(this);
        ll_log_out.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
        ll_color_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.ll_check_version_update://检查更新
                break;
            case R.id.ll_feedback://反馈
                Intent feedback = new Intent(SetupActivity.this, SetupFeedbackActivity.class);
                startActivity(feedback);
                break;
            case R.id.ll_color_change://改变主题色
                Intent colorchange = new Intent(SetupActivity.this, SetupColorChangeActivity.class);
                startActivity(colorchange);
                break;
            case R.id.ll_exit_system://退出系统
                EXITORLOGOUT = 0;
                showchoosedialog(view, "退出系统");
                break;
            case R.id.ll_log_out://注销用户
                EXITORLOGOUT = 1;
                showchoosedialog(view, "注销");
                break;
        }
    }

    public void showchoosedialog(View view, String hint) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("提示").setMessage("确定" + hint).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (EXITORLOGOUT == 0) {
                            MainActivity.mInstace.finish();
                            finish();
                        } else if (EXITORLOGOUT == 1) {
                            editor = pref.edit();
                            editor.putBoolean(CommonGlobal.isAutoLogin, false);
                            editor.putInt(CommonGlobal.user_id, -1);
                            editor.apply();
                            Intent loginIntent = new Intent(SetupActivity.this, LoginActivity.class);
                            startActivity(loginIntent);//注销后跳转到登录界面重新登录
                            MainActivity.mInstace.finish();
                            finish();
                        }
                    }
                }).setNegativeButton("取消", null);
        alert.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initcolor();
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));
    }
}
