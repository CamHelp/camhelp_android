package com.camhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.camhelp.R;
import com.camhelp.entity.CommomPropertyDetailsVo;
import com.camhelp.myview.LookLargePicAdapter;
import com.camhelp.myview.LookLargePicViewPager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LookLargePicActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LookLargePicActivity.class.getSimpleName();
    private ImageButton top_return;
    private LookLargePicViewPager mViewPager;
    private int currentPosition;
    private LookLargePicAdapter adapter;
    private TextView mTvImageCount;
    private TextView mTvSaveImage;
    List<String> Urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_large_pic);
        initView();
        initData();
    }

    private void initView() {
        top_return = (ImageButton) findViewById(R.id.top_return);
        top_return.setOnClickListener(this);
        mViewPager = (LookLargePicViewPager) findViewById(R.id.LookLargePicViewPager);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mTvSaveImage = (TextView) findViewById(R.id.tv_save_image_photo);
        mTvSaveImage.setOnClickListener(this);

    }

    private void initData() {

        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);//得到选择图片位置
        Urls = intent.getStringArrayListExtra("listURL");//得到图片urllist

        adapter = new LookLargePicAdapter(Urls, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + Urls.size());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.tv_save_image_photo:
                Toast.makeText(this, "保存功能待完成", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}