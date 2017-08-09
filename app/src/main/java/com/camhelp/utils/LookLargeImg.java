package com.camhelp.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;

/**
 * Created by storm on 2017-08-09.
 */

public class LookLargeImg {
    public void looklargeimg(String imgurl, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        ImageView img_large = (ImageView) imgEntryView.findViewById(R.id.large_image);
        Glide.with(context).load(imgurl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(img_large);
        dialog.setView(imgEntryView); // 自定义dialog
        dialog.show();
        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }
}
