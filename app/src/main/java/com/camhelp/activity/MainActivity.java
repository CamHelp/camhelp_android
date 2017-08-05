package com.camhelp.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.camhelp.R;
import com.camhelp.fragment.CategoryFragment;
import com.camhelp.fragment.HomeFragment;
import com.camhelp.fragment.MineFragment;
import com.camhelp.fragment.PublishFragment;
import com.camhelp.fragment.QueryFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private HomeFragment homeFragment;
    private QueryFragment queryFragment;
    private PublishFragment publishFragment;
    private CategoryFragment categoryFragment;
    private MineFragment mineFragment;
    private TextView tvTitle;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_home, "首页").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.icon_query, "搜索").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.icon_publish, "发布").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.icon_category, "分类").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.icon_mine, "我的").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setBarBackgroundColor(R.color.white);
        setDefaultFragment();
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
        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = homeFragment.newInstance("HOME", "");
                }
                tvTitle.setText("校园帮");
                transaction.replace(R.id.tabs, homeFragment);
                break;
            case 1:
                if (queryFragment == null) {
                    queryFragment = queryFragment.newInstance("QUERY", "");
                }
                tvTitle.setText("搜索");
                transaction.replace(R.id.tabs, queryFragment);
                break;
            case 2:
                if (publishFragment == null) {
                    publishFragment = publishFragment.newInstance("PUBLISH", "");
                }
                tvTitle.setText("发布内容");
                transaction.replace(R.id.tabs, publishFragment);
                break;
            case 3:
                if (categoryFragment == null) {
                    categoryFragment = categoryFragment.newInstance("CATEGORY", "");
                }
                tvTitle.setText("分类");
                transaction.replace(R.id.tabs, categoryFragment);
                break;
            case 4:
                if (mineFragment == null) {
                    mineFragment = mineFragment.newInstance("MINE", "");
                }
                tvTitle.setText("个人主页");
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
}
