package com.camhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.camhelp.R;
import com.camhelp.myview.LookLargePicAdapter;
import com.camhelp.myview.LookLargePicViewPager;

import java.net.URL;
import java.util.List;

public class LookLargePicActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LookLargePicActivity.class.getSimpleName();
    private LookLargePicViewPager mViewPager;
    private int currentPosition;
    private LookLargePicAdapter adapter;
    private TextView mTvImageCount;
    private TextView mTvSaveImage;
    private List<String> Urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_large_pic);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (LookLargePicViewPager) findViewById(R.id.LookLargePicViewPager);
        mTvImageCount = (TextView) findViewById(R.id.tv_image_count);
        mTvSaveImage = (TextView) findViewById(R.id.tv_save_image_photo);
        mTvSaveImage.setOnClickListener(this);

    }

    private void initData() {

//        Intent intent = getIntent();
//        currentPosition = intent.getIntExtra("currentPosition", 0);
//        HomeQuestionListModel.DataBeanX DataBean = ((HomeQuestionListModel.DataBeanX) intent.getSerializableExtra("questionlistdataBean"));
//
//        Urls = DataBean.getAttach().getImage().getOri();
        Urls.add("http://www.stormstone8.cn/assets/img/storm_left.png");

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
            case R.id.tv_save_image_photo:
                Toast.makeText(this, "保存功能待完成", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}