package com.camhelp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;
import com.camhelp.common.CommonGlobal;
import com.camhelp.utils.L;
import com.camhelp.utils.MiPictureHelper;

import java.io.File;
import java.util.Date;

import static org.litepal.LitePalApplication.getContext;

/**
 * 发布活动
 * */
public class PublishExperienceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PublishExperienceActivity";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    private LinearLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title, top_tv_ok;

    private EditText et_title, et_intro, et_content;
    private Button btn_time_start, btn_time_end;
    private ImageView iv_photo1, iv_photo2, iv_photo3, iv_photo4;

    private String title, intro, content;
    private Date startDate, endDate;
    private String photopath1, photopath2, photopath3, photopath4;

    final int TAKE_PHOTO = 1,TAKE_PHOTO2 = 12,TAKE_PHOTO3 = 13,TAKE_PHOTO4 = 14;
    final int GET_PHOTO = 2;
    Uri imageUri, imageUri2, imageUri3, imageUri4;
    private Dialog photodialog = null;//拍照和相册dialog
    Boolean isPhoto1,isPhoto2,isPhoto3,isPhoto4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_experience);
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

        top_title.setText("发布活动");
        top_return.setOnClickListener(this);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);
        top_tv_ok.setOnClickListener(this);
    }

    public void initview() {
        et_title = (EditText) findViewById(R.id.et_title);
        et_intro = (EditText) findViewById(R.id.et_intro);
        et_content = (EditText) findViewById(R.id.et_content);

        btn_time_start = (Button) findViewById(R.id.btn_time_start);
        btn_time_end = (Button) findViewById(R.id.btn_time_end);
        btn_time_start.setOnClickListener(this);
        btn_time_end.setOnClickListener(this);

        iv_photo1 = (ImageView) findViewById(R.id.iv_photo1);
        iv_photo2 = (ImageView) findViewById(R.id.iv_photo2);
        iv_photo3 = (ImageView) findViewById(R.id.iv_photo3);
        iv_photo4 = (ImageView) findViewById(R.id.iv_photo4);
        iv_photo1.setOnClickListener(this);
        iv_photo2.setOnClickListener(this);
        iv_photo3.setOnClickListener(this);
        iv_photo4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                finish();
                break;
            case R.id.top_tv_ok://保存
                expsave();
                break;
            case R.id.btn_time_start://开始时间
                Date date = new Date(System.currentTimeMillis());
                int year = date.getYear();
                int month = date.getMonth();
                int day = date.getDate();
                L.e(TAG, "year is : " + year + " month is " + month + " day is " + day);
                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate = new Date(year - 1900, month, dayOfMonth);
                        btn_time_start.setText("" + year + " 年 " + (month + 1) + " 月 " + dayOfMonth + " 日 ");
                    }
                }, 1900 + year, month, day);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
            case R.id.btn_time_end://结束时间
                Date date2 = new Date(System.currentTimeMillis());
                int year2 = date2.getYear();
                int month2 = date2.getMonth();
                int day2 = date2.getDate();
                L.e(TAG, "year is : " + year2 + " month is " + month2 + " day is " + day2);
                DatePickerDialog dialog2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate = new Date(year - 1900, month, dayOfMonth);
                        btn_time_end.setText("" + year + " 年 " + (month + 1) + " 月 " + dayOfMonth + " 日 ");
                    }
                }, 1900 + year2, month2, day2);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.show();
                break;
            case R.id.iv_photo1:
                isPhoto1 =true;
                showphotodialg(view);
                break;
            case R.id.iv_photo2:
                isPhoto2 =true;
                showphotodialg(view);
                break;
            case R.id.iv_photo3:
                isPhoto3 =true;
                showphotodialg(view);
                break;
            case R.id.iv_photo4:
                isPhoto4 =true;
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

    /**
     * 保存
     */
    public void expsave() {
        title = et_title.getText().toString();
        intro = et_intro.getText().toString();
        content = et_content.getText().toString();

        Toast.makeText(this, "保存功能待做", Toast.LENGTH_SHORT).show();
    }

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
     * 判断四张照片中的哪一张，头一张选好，把下一张的加号显示
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
            if (isPhoto1) {
                imageUri = FileProvider.getUriForFile(this, "com.camhelp.fileprovider", mFile);
            } else if (isPhoto2){
                imageUri2 = FileProvider.getUriForFile(this, "com.camehelp.fileprovider", mFile);
            } else if (isPhoto3){
                imageUri3 = FileProvider.getUriForFile(this, "com.camehelp.fileprovider", mFile);
            } else if (isPhoto4){
                imageUri4 = FileProvider.getUriForFile(this, "com.camehelp.fileprovider", mFile);
            }
        } else {
            if (isPhoto1) {
                imageUri = Uri.fromFile(mFile);
            } else if (isPhoto2){
                imageUri2 = Uri.fromFile(mFile);
            }else if (isPhoto3){
                imageUri3 = Uri.fromFile(mFile);
            }else if (isPhoto4){
                imageUri4 = Uri.fromFile(mFile);
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
        int id ;
        Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (isPhoto1) {
            uri = imageUri;
            id = TAKE_PHOTO;
        } else if (isPhoto2){
            uri = imageUri2;
            id = TAKE_PHOTO2;
        }else if (isPhoto3){
            uri = imageUri3;
            id = TAKE_PHOTO3;
        }else{
            uri = imageUri4;
            id = TAKE_PHOTO4;
        }
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        this.startActivityForResult(mIntent, id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo1.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath1 = imageUri.getPath();
                    Glide.with(this).load(imageUri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo1);
                }
                isPhoto1 = false;
                if (photopath1 != null){
                    iv_photo2.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo2.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath2 = imageUri2.getPath();
                    Glide.with(this).load(imageUri2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo2);
                }
                isPhoto2 = false;
                if (photopath2 != null){
                    iv_photo3.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO3:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo3.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath3 = imageUri3.getPath();
                    Glide.with(this).load(imageUri3).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo3);
                }
                isPhoto3 = false;
                if (photopath3 != null){
                    iv_photo4.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO4:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo4.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath4 = imageUri4.getPath();
                    Glide.with(this).load(imageUri4).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo4);
                }
                isPhoto4 = false;
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
                    if (isPhoto1) {
                        if (!iv_photo1.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo1);
                        imageUri = selectedImage;
                        photopath1 = path;
                        isPhoto1 = false;
                        if (photopath1 != null){
                            iv_photo2.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto2) {
                        if (!iv_photo2.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo2);
                        imageUri2 = selectedImage;
                        photopath2 = path;
                        isPhoto2 = false;
                        if (photopath2 != null){
                            iv_photo3.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto3) {
                        if (!iv_photo3.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo3);
                        imageUri3 = selectedImage;
                        photopath3 = path;
                        isPhoto3 = false;
                        if (photopath3 != null){
                            iv_photo4.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto4) {
                        if (!iv_photo4.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo4);
                        imageUri4 = selectedImage;
                        photopath4 = path;
                        isPhoto4 = false;
                    }
                }
                break;
        }
    }

}
