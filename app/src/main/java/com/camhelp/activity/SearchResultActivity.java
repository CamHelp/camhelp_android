package com.camhelp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.camhelp.R;
import com.camhelp.common.CommonGlobal;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener{

    private String searchContent;
    private ImageButton top_return;
    private TextView top_title,top_tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        searchContent = intent.getStringExtra(CommonGlobal.searchContent);
        initTitle(searchContent);
    }

    private void initTitle(String searchContent){
        top_return= (ImageButton) findViewById(R.id.top_return);
        top_return.setOnClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        top_title.setText(searchContent);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);
        top_tv_ok.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_return:
                finish();
                break;
        }
    }
}
