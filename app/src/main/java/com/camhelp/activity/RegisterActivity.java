package com.camhelp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView top_return;
    private TextView top_title;

    private TextView tv_result_label;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inittitle();
        initview();
    }

    public void inittitle() {
        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);

        top_title.setText("注册");
        top_return.setOnClickListener(this);
    }

    public void initview() {
        tv_result_label = (TextView) findViewById(R.id.tv_result_label);
        tv_result_label.setVisibility(View.GONE);
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
                Toast.makeText(this, "待完成！", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
