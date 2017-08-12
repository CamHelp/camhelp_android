package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.utils.AssetsDatabaseManager;

/**
 * 进入APP欢迎界面
 * 第一次打开应用CommonGlobal.assetsDatabaseInit==false，要进行数据库初始化
 * 第一次使用或未记录用户名密码，会跳转到登录页面
 * 记住用户名和密码后会自动登录，直接跳转到主界面
 * */
public class WelcomeActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        // 数据库assetsDatabase初始化，只需要调用一次
        //整个应用就第一次打开调用，以后都不在调用，直接通过AssetsDatabaseManager管理数据库对象
        boolean assetsDatabaseInit = pref.getBoolean(CommonGlobal.isFirstLogin,false);
        if (!assetsDatabaseInit){
//            AssetsDatabaseManager.initManager(getApplication());
            editor = pref.edit();
            editor.putBoolean(CommonGlobal.isFirstLogin,true);
            editor.apply();
//            Toast.makeText(WelcomeActivity.this,"初始化完成",Toast.LENGTH_SHORT).show();
        }

        Handler handler;
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                boolean autoLogin = pref.getBoolean(CommonGlobal.isAutoLogin,false);
                if (autoLogin){
                    Intent ss = new Intent(WelcomeActivity.this, MainViewpaperActivity.class);
//                    Toast.makeText(WelcomeActivity.this,"已自动登录",Toast.LENGTH_SHORT).show();
                    startActivity(ss);
                    WelcomeActivity.this.finish();//跳转到主界面后销毁WelcomeActivity
                }
                else {
                    Intent ss = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(ss);
                    WelcomeActivity.this.finish();//跳转到登陆界面后销毁WelcomeActivity
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 2000);//handler延迟2秒执行
    }

}
