package com.camhelp.activity;

import android.graphics.Color;
import android.os.Bundle;
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

public class SetupColorChangeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private RelativeLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title;

    private CheckBox cb_color_01, cb_color_02, cb_color_03;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_color_change);
        inittitle();
        initview();
    }

    public void inittitle() {
        top_rl_title = (RelativeLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(CommonGlobal.MYCOLOR_PRIMARY));

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

        int chosn = CommonGlobal.MY_COLOR_CHOSN;
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
        String colorPrimary = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimary));
        String colorPrimaryBlew = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew));
        String colorPrimaryDark = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark));
        String colorAccent = "#"+Integer.toHexString(getResources().getColor(R.color.colorAccent));

        if (cb_color_01.isChecked()) {
            CommonGlobal.MY_COLOR_CHOSN = 1;
        } else if (cb_color_02.isChecked()) {
            CommonGlobal.MY_COLOR_CHOSN = 2;
            colorPrimary = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimary2));
            colorPrimaryBlew = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew2));
            colorPrimaryDark = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark2));
            colorAccent = "#"+Integer.toHexString(getResources().getColor(R.color.colorAccent2));
        } else if (cb_color_03.isChecked()) {
            CommonGlobal.MY_COLOR_CHOSN = 3;
            colorPrimary = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimary3));
            colorPrimaryBlew = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryBlew3));
            colorPrimaryDark = "#"+Integer.toHexString(getResources().getColor(R.color.colorPrimaryDark3));
            colorAccent = "#"+Integer.toHexString(getResources().getColor(R.color.colorAccent3));
        }

        CommonGlobal.MYCOLOR_PRIMARY = colorPrimary;
        CommonGlobal.MYCOLOR_PRIMARY_BLEW= colorPrimaryBlew;
        CommonGlobal.MYCOLOR_PRIMARY_DARK = colorPrimaryDark;
        CommonGlobal.MYCOLOR_ACCENT = colorAccent;

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
