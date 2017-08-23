package com.camhelp.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.camhelp.R;
import com.camhelp.common.CommonGlobal;

/**
 * 反馈
 * */
public class SetupFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private LinearLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title,top_tv_ok;

    private Spinner spinner_feedback_type;
    private EditText et_feedback_content,et_feedback_contact;

    private String feedbackType,feedbackContent,feedbackContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_feedback);
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
        top_rl_title = (LinearLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);

        top_title.setText("反馈");
        top_tv_ok.setText("提交");
        top_tv_ok.setOnClickListener(this);
        top_return.setOnClickListener(this);
    }

    public void initview() {
        spinner_feedback_type = (Spinner) findViewById(R.id.spinner_feedback_type);
        et_feedback_content = (EditText) findViewById(R.id.et_feedback_content);
        et_feedback_contact = (EditText) findViewById(R.id.et_feedback_contact);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.top_tv_ok://提交反馈
                saveFeedback();
                break;
        }
    }

    private void saveFeedback(){
        feedbackType = spinner_feedback_type.getSelectedItem().toString();
        feedbackContent = et_feedback_content.getText().toString();
        feedbackContact = et_feedback_contact.getText().toString();
        if ("".equals(feedbackContent)){//未填写反馈内容
            et_feedback_content.setBackgroundResource(R.drawable.edit_shape_feedback_error);
        }else if (feedbackContent.length()<10){//描述太短
            et_feedback_content.setBackgroundResource(R.drawable.edit_shape_feedback_error);
        }else if ("".equals(feedbackContact)){//未填写联系方式
            et_feedback_content.setBackgroundResource(R.drawable.edit_shape_feedback);
            et_feedback_contact.setBackgroundResource(R.drawable.edit_shape_feedback_error);
        }else {
            et_feedback_content.setBackgroundResource(R.drawable.edit_shape_feedback);
            et_feedback_contact.setBackgroundResource(R.drawable.edit_shape_feedback);
            Toast.makeText(this, "待完成提交功能", Toast.LENGTH_SHORT).show();
        }
    }
}
