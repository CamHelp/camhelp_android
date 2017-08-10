package com.camhelp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.camhelp.R;

/**
 * Created by storm on 2017-08-10.
 */

public class MyProcessDialog extends Dialog {

    private Context context;
    private static MyProcessDialog dialog;
    private ImageView ivProgress;


    public MyProcessDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyProcessDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }
    //显示dialog的方法
    public static MyProcessDialog showDialog(Context context){
        dialog = new MyProcessDialog(context, R.style.MyProcessDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_process);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
            ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
            ivProgress.startAnimation(animation);
        }
    }
}