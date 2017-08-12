package com.camhelp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camhelp.R;
import com.camhelp.basic.BaseActivity;
import com.camhelp.common.CommonGlobal;

/**
 * 改变主题色
 * */
public class SetupColorChangeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    private int myColorChosn;

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private CheckBox cb_color_01, cb_color_02, cb_color_03;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_color_change);
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
        myColorChosn = pref.getInt(CommonGlobal.myColorChosn,1);
    }

    public void inittitle() {
        top_rl_title = (RelativeLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);

        top_title.setText("选择主题色");
        top_return.setOnClickListener(this);
    }

    public void initview() {
        cb_color_01 = (CheckBox) findViewById(R.id.cb_color_01);
        cb_color_02 = (CheckBox) findViewById(R.id.cb_color_02);
        cb_color_03 = (CheckBox) findViewById(R.id.cb_color_03);

        cb_color_01.setOnCheckedChangeListener(this);
        cb_color_02.setOnCheckedChangeListener(this);
        cb_color_03.setOnCheckedChangeListener(this);

        int chosn = myColorChosn;
        if (chosn == 1) {
            cb_color_01.setChecked(true);
        } else if (chosn == 2) {
            cb_color_02.setChecked(true);
        } else if (chosn == 3) {
            cb_color_03.setChecked(true);
        }

        btn_save = (Button) findViewById(R.id.btn_color_save);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.btn_color_save:
                colorsave();
                break;
        }
    }

    public void colorsave() {
        String checkcolorPrimary = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String checkcolorPrimaryBlew = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String checkcolorPrimaryDark = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String checkcolorAccent = "#" + Integer.toHexString(getResources().getColor(R.color.colorAccent));
        editor = pref.edit();
        if (cb_color_01.isChecked()) {
            editor.putInt(CommonGlobal.myColorChosn,1);
        } else if (cb_color_02.isChecked()) {
            editor.putInt(CommonGlobal.myColorChosn,2);
            checkcolorPrimary = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimary2));
            checkcolorPrimaryBlew = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew2));
            checkcolorPrimaryDark = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark2));
            checkcolorAccent = "#" + Integer.toHexString(getResources().getColor(R.color.colorAccent2));
        } else if (cb_color_03.isChecked()) {
            editor.putInt(CommonGlobal.myColorChosn,3);
            checkcolorPrimary = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimary3));
            checkcolorPrimaryBlew = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew3));
            checkcolorPrimaryDark = "#" + Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark3));
            checkcolorAccent = "#" + Integer.toHexString(getResources().getColor(R.color.colorAccent3));
        }

        editor.putString(CommonGlobal.colorPrimary,checkcolorPrimary);
        editor.putString(CommonGlobal.colorPrimaryBlew,checkcolorPrimaryBlew);
        editor.putString(CommonGlobal.colorPrimaryDark,checkcolorPrimaryDark);
        editor.putString(CommonGlobal.colorAccent,checkcolorAccent);

        editor.apply();

        /*改变主题色后跳转到MainViewpaperActivity，并关闭其他的activity*/
        Intent refreshIntent = new Intent(SetupColorChangeActivity.this, MainViewpaperActivity.class);
        startActivity(refreshIntent);
        SetupActivity.mInstace.finish();
        MainViewpaperActivity.mInstace.finish();
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_color_01:
                if (b) {
                    cb_color_02.setChecked(false);
                    cb_color_03.setChecked(false);
                }
                break;
            case R.id.cb_color_02:
                if (b) {
                    cb_color_01.setChecked(false);
                    cb_color_03.setChecked(false);
                }
                break;
            case R.id.cb_color_03:
                if (b) {
                    cb_color_01.setChecked(false);
                    cb_color_02.setChecked(false);
                }
                break;
        }
    }
}
