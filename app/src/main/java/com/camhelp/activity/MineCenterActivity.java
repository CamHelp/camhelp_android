package com.camhelp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;
import com.camhelp.common.CommonGlobal;
import com.camhelp.entity.User;
import com.camhelp.utils.L;
import com.camhelp.utils.MiPictureHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineCenterActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MineCenterActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    User mUser = new User();

    final int TAKE_PHOTO = 1;
    final int TAKE_PHOTO2 = 12;
    final int GET_PHOTO = 2;
    Boolean BACK = false;
    Uri imageUri, imageUri2;
    private Dialog photodialog = null;//拍照和相册dialog

    private LinearLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title, top_tv_ok;

    private String photo1path, photo2path;//背景图和头像

    private ImageView iv_mine_back;
    private CircleImageView cimg_mine_avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_center);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userInit();
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
        top_rl_title.setBackgroundColor(Color.parseColor("#11E1E1E1"));//

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);

        top_title.setText("个人资料");
        top_return.setOnClickListener(this);
        top_tv_ok.setOnClickListener(this);
    }

    public void initview() {
        iv_mine_back = (ImageView) findViewById(R.id.iv_mine_back);
        cimg_mine_avatar = (CircleImageView) findViewById(R.id.cimg_mine_avatar);
        Glide.with(this).load(mUser.getBgpicture())
                .error(R.drawable.mine_center_back)
                .placeholder(R.drawable.mine_center_back)
                .into(iv_mine_back);
        Glide.with(this).load(mUser.getAvatar())
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(cimg_mine_avatar);
        iv_mine_back.setOnClickListener(this);
        cimg_mine_avatar.setOnClickListener(this);
    }

    /**
     * 获取用户
     */
    public void userInit() {
        mUser = getUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.top_tv_ok://保存
                minesave();
                break;
            case R.id.iv_mine_back://背景图
                showphotodialg(view);
                BACK = true;
                break;
            case R.id.cimg_mine_avatar://头像
                showphotodialg(view);
                break;
            case R.id.takePhoto:
                creatFile();
                takePhoto();
                photodialog.dismiss();
                break;
            case R.id.choosePhoto:
                creatFile();
                takePhotoFromAlbum();
                photodialog.dismiss();
                break;
            case R.id.btn_cancel:
                photodialog.dismiss();
                break;
        }
    }

    public void saveUser(User user) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public User getUser() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        L.d(TAG, temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            user = (User) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return user;
    }

    public void minesave() {

        Toast.makeText(this, "保存功能待做", Toast.LENGTH_SHORT).show();
    }

    /*显示dialog*/
    public void showphotodialg(View view) {
        photodialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_photo_layout, null);
        //初始化控件
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //将布局设置给Dialog
        photodialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = photodialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置Dialog距离底部的距离
        lp.y = 20;
        //设置dialog的宽为match_content
        lp.width = -1;
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        //显示对话框
        photodialog.show();
    }

    /**
     * 拍照相册相关
     */
    public void creatFile() {
        File mFile = new File(this.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        try {
            if (mFile.exists()) {
                mFile.delete();
                mFile.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            if (BACK) {
                imageUri = FileProvider.getUriForFile(this, "com.camhelp.fileprovider", mFile);
            } else {
                imageUri2 = FileProvider.getUriForFile(this, "com.camhelp.fileprovider", mFile);
            }
        } else {
            if (BACK) {
                imageUri = Uri.fromFile(mFile);
            } else {
                imageUri2 = Uri.fromFile(mFile);
            }
        }
    }

    private void takePhotoFromAlbum() {
        Intent mIntent = new Intent("android.intent.action.GET_CONTENT");
        mIntent.setType("image/*");
        startActivityForResult(mIntent, GET_PHOTO);
    }

    private void takePhoto() {
        Uri uri = null;
        int id;
        Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (BACK) {
            uri = imageUri;
            id = TAKE_PHOTO;
        } else {
            uri = imageUri2;
            id = TAKE_PHOTO2;
        }
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.startActivityForResult(mIntent, id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (!iv_mine_back.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_mine_back.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photo1path = imageUri.getPath();
                    Glide.with(this).load(imageUri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_mine_back);
                }
                BACK = false;
                break;
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    if (!cimg_mine_avatar.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        cimg_mine_avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photo2path = imageUri2.getPath();
                    Glide.with(this).load(imageUri2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(cimg_mine_avatar);
                }
                break;
            case GET_PHOTO:
                if (resultCode == RESULT_OK && data != null) {

                    Uri selectedImage = data.getData();
                    String path = "";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        path = MiPictureHelper.getPath(this, selectedImage);
                    }
                    if (selectedImage.getPath() != null) {
                    }
                    if (BACK) {
                        if (!iv_mine_back.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_mine_back.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_mine_back);
                        imageUri = selectedImage;
                        photo1path = path;
                        BACK = false;
                    } else {
                        if (!cimg_mine_avatar.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            cimg_mine_avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(cimg_mine_avatar);
                        imageUri2 = selectedImage;
                        photo2path = path;
                    }
                }
                break;
        }
    }

}
