package com.camhelp.activity;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.fragment.CategoryFragment;
import com.camhelp.fragment.HomeFragmentTest;
import com.camhelp.fragment.MineFragment;
import com.camhelp.fragment.PublishFragment;
import com.camhelp.fragment.QueryFragment;
import com.camhelp.utils.L;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary,colorPrimaryBlew,colorPrimaryDark,colorAccent;

    public static MainActivity mInstace = null;//用于设置里关掉MainActivity，达到退出效果

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private HomeFragmentTest homeFragment;
    private QueryFragment queryFragment;
    private PublishFragment publishFragment;
    private CategoryFragment categoryFragment;
    private MineFragment mineFragment;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initcolor();
        mInstace = this;
        initview();
        setDefaultFragment();
        requestPermission();
    }

    /*获取主题色*/
    public void initcolor(){
        String defaultColorPrimary = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String defaultColorPrimaryBlew = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String defaultColorPrimaryDark = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String defaultColorAccent = "#"+Integer.toHexString(getResources().getColor(R.color.colorAccent));

        colorPrimary = pref.getString(CommonGlobal.colorPrimary,defaultColorPrimary);
        colorPrimaryBlew = pref.getString(CommonGlobal.colorPrimaryBlew,defaultColorPrimaryBlew);
        colorPrimaryDark = pref.getString(CommonGlobal.colorPrimaryDark,defaultColorPrimaryDark);
        colorAccent = pref.getString(CommonGlobal.colorAccent,defaultColorAccent);
    }

    public void initview(){
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        /*记得一定把模式和背景样式设置在获取Item前面，否则会不生效*/
        //设置BottomNavigationBar的模式
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //设置BottomNavigationBar的背景风格
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        bottomNavigationBar.setActiveColor(R.color.white)//选中颜色
                .setInActiveColor(colorPrimaryBlew)//未选中颜色
                .setBarBackgroundColor(colorPrimary);//背景色

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.icon_query, "搜索"))
                .addItem(new BottomNavigationItem(R.drawable.icon_publish, "发布"))
                .addItem(new BottomNavigationItem(R.drawable.icon_category, "分类"))
                .addItem(new BottomNavigationItem(R.drawable.icon_mine, "我的"))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();


        bottomNavigationBar.setTabSelectedListener(this);
    }
    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = homeFragment.newInstance("HOME", "");
        transaction.replace(R.id.tabs, homeFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
//        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = homeFragment.newInstance("HOME", "");
                }
                transaction.replace(R.id.tabs, homeFragment);
                break;
            case 1:
                if (queryFragment == null) {
                    queryFragment = queryFragment.newInstance("QUERY", "");
                }
                transaction.replace(R.id.tabs, queryFragment);
                break;
            case 2:
                if (publishFragment == null) {
                    publishFragment = publishFragment.newInstance("PUBLISH", "");
                }
                transaction.replace(R.id.tabs, publishFragment);
                break;
            case 3:
                if (categoryFragment == null) {
                    categoryFragment = categoryFragment.newInstance("CATEGORY", "");
                }
                transaction.replace(R.id.tabs, categoryFragment);
                break;
            case 4:
                if (mineFragment == null) {
                    mineFragment = mineFragment.newInstance("MINE", "");
                }
                transaction.replace(R.id.tabs, mineFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }


    /*再按一次返回键退出程序*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initcolor();
        bottomNavigationBar.setActiveColor(R.color.white)//选中颜色
                .setInActiveColor(colorPrimaryDark)//未选中颜色
                .setBarBackgroundColor(colorPrimary);//背景色

    }

    /**
     * 请求获取权限,目前只申请了获取照相权限和写入tf卡权限
     */
    private static final int MY_PERMISSIONS_REQUEST_TAKE_PHOTO = 4;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 5;
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            //权限还没有授予，需要在这里写申请权限的代码
        } else if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_TAKE_PHOTO);
        } else {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
            case MY_PERMISSIONS_REQUEST_TAKE_PHOTO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
