package com.camhelp.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;
import com.camhelp.fragment.CategoryFragment;
import com.camhelp.fragment.CategoryTypeFragment;
import com.camhelp.fragment.HomeFragment;
import com.camhelp.fragment.HomeNewFragment;
import com.camhelp.fragment.HomeOnlyNewFragment;
import com.camhelp.fragment.MineFragment;
import com.camhelp.fragment.PublishFragment;
import com.camhelp.fragment.QueryFragment;
import com.camhelp.utils.NoSlideViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 将mainactivity里的五个fragment全部替换为viewpaper
 */
public class MainViewpaperActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    public static MainViewpaperActivity mInstace = null;//用于设置里关掉MainViewpaperActivity，达到退出效果

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainViewpaperActivity.class.getSimpleName();
    List<Fragment> fragments;
//    private HomeFragment homeFragment;
    private HomeOnlyNewFragment homeFragment;
    private QueryFragment queryFragment;
    private PublishFragment publishFragment;
//    private CategoryFragment categoryFragment;
    private CategoryTypeFragment categoryFragment;
    private MineFragment mineFragment;
    private long exitTime = 0;

    public NoSlideViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    private int mCurrentPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewpaper);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initcolor();
        mInstace = this;
        initview();
        setDefaultFragment();
        requestPermission();
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

    public void initview() {
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


        mViewPager = (NoSlideViewPager) findViewById(R.id.viewpager_main);
//        mViewPager.setIsCanScroll(false);//设置不允许滑动
        mDatas = new ArrayList<Fragment>();
//        homeFragment = new HomeFragment();
        homeFragment = new HomeOnlyNewFragment();
        queryFragment = new QueryFragment();
        publishFragment = new PublishFragment();
//        categoryFragment = new CategoryFragment();
        categoryFragment = new CategoryTypeFragment();
        mineFragment = new MineFragment();
        mDatas.add(homeFragment);
        mDatas.add(queryFragment);
        mDatas.add(publishFragment);
        mDatas.add(categoryFragment);
        mDatas.add(mineFragment);

        mAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                fragment = mDatas.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + position);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment fragment = (Fragment) super.instantiateItem(container, position);
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
                return fragment;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = mDatas.get(position);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }

            @Override
            public Parcelable saveState() {
                return super.saveState();
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                mCurrentPageIndex = position;
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx) {
                Log.e("TAG", position + " , " + positionOffset + " , "
                        + positionOffsetPx);


                if (mCurrentPageIndex == 0 && position == 0)// 0->1
                {
                } else if (mCurrentPageIndex == 1 && position == 0)// 1->0
                {
                } else if (mCurrentPageIndex == 1 && position == 1) // 1->2
                {
                } else if (mCurrentPageIndex == 2 && position == 1) // 2->1
                {
                } else if (mCurrentPageIndex == 2 && position == 2) // 2->3
                {
                } else if (mCurrentPageIndex == 3 && position == 2) // 3->2
                {
                } else if (mCurrentPageIndex == 3 && position == 3) // 3->4
                {
                } else if (mCurrentPageIndex == 4 && position == 3) // 4->3
                {
                } else if (mCurrentPageIndex == 4 && position == 4) // 4->5
                {
                } else if (mCurrentPageIndex == 5 && position == 4) // 5->4
                {
                } else if (mCurrentPageIndex == 5 && position == 5) // 5->5
                {
                }

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = homeFragment.newInstance("HOME", "");
//        transaction.replace(R.id.tabs, homeFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {

        // Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                break;
            case 3:
                mViewPager.setCurrentItem(3);
                break;
            case 4:
                mViewPager.setCurrentItem(4);
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
                MainViewpaperActivity.this.finish();
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
        if (ContextCompat.checkSelfPermission(MainViewpaperActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainViewpaperActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            //权限还没有授予，需要在这里写申请权限的代码
        } else if (ContextCompat.checkSelfPermission(MainViewpaperActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainViewpaperActivity.this,
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
                    Toast.makeText(MainViewpaperActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
                break;
            case MY_PERMISSIONS_REQUEST_TAKE_PHOTO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainViewpaperActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
