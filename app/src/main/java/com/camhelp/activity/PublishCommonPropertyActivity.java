package com.camhelp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.camhelp.adapter.MineFocusAdapter;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.CommonProperty;
import com.camhelp.entity.UserVO;
import com.camhelp.entity.ZLMinePublishedCommonProperty;
import com.camhelp.utils.L;
import com.camhelp.utils.MiPictureHelper;
import com.camhelp.utils.MyProcessDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 共同的发布activity
 * 通过类型判断发布的是活动、问题、失物还是捡物
 */
public class PublishCommonPropertyActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PublishCommonActivity";
    int categoryType;//发布类型
    //    private CommonProperty mCommonProperty = new CommonProperty();
    private ZLMinePublishedCommonProperty mCommonProperty = new ZLMinePublishedCommonProperty();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;

    /*标题栏*/
    private LinearLayout top_rl_title;
    private ImageView top_return;
    private TextView top_title, top_tv_ok;

    /*拍照相关*/
    final int TAKE_PHOTO = 1, TAKE_PHOTO2 = 12, TAKE_PHOTO3 = 13, TAKE_PHOTO4 = 14;
    final int GET_PHOTO = 2;
    Uri mImageUri, mImageUri2, mImageUri3, mImageUri4;
    private Dialog photodialog = null;//拍照和相册dialog
    Boolean isPhoto1 = false, isPhoto2 = false, isPhoto3 = false, isPhoto4 = false;

    /*问题类型，活动开始时间，活动结束时间，物品联系方式*/
    private LinearLayout ll_proType, ll_time_start, ll_time_end, ll_contact;
    private View view_proType, view_time_start, view_time_end, view_contact;

    /*标题，简介，内容，问题类型，物品联系方式*/
    private EditText et_title, et_intro, et_content, et_proType, et_contact;
    private Button btn_time_start, btn_time_end;//活动开始时间，活动结束时间
    private ImageView iv_photo1, iv_photo2, iv_photo3, iv_photo4;//照片

    private String title, intro, content, contact;//标题，简介，内容，物品联系方式
    private String protype;//问题类型
    private Date startDate, endDate, createDate;//活动开始结束时间,当前创建时间
    private String sStartTime, sEndTime;//活动开始结束时间yyyy-mm-dd
    private String photopath1, photopath2, photopath3, photopath4;//照片路径

    private int muserid;
    Dialog dialogProcess;
    private String resultPhotoURL1 = "", resultPhotoURL2 = "", resultPhotoURL3 = "", resultPhotoURL4 = "";//返回服务器图片路径
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    String uploadResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_common_property);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        muserid = pref.getInt(CommonGlobal.user_id, 0);//得到自己的用户id
        initcolor();
        inittitle();
        initview();
        initgone();
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
        categoryType = this.getIntent().getIntExtra(CommonGlobal.categoryType, 0);

        top_rl_title = (LinearLayout) findViewById(R.id.top_rl_title);
        top_rl_title.setBackgroundColor(Color.parseColor(colorPrimary));

        top_return = (ImageView) findViewById(R.id.top_return);
        top_title = (TextView) findViewById(R.id.top_title);

        if (categoryType == 1) {
            top_title.setText("发布活动");
        } else if (categoryType == 2) {
            top_title.setText("发布问题");
        } else if (categoryType == 3) {
            top_title.setText("发布失物");
        } else if (categoryType == 4) {
            top_title.setText("发布捡物");
        } else if (categoryType == 5) {
            top_title.setText("发布新鲜事");
        }
        top_return.setOnClickListener(this);
        top_tv_ok = (TextView) findViewById(R.id.top_tv_ok);
        top_tv_ok.setOnClickListener(this);
    }

    public void initview() {
        dialogProcess = MyProcessDialog.showDialog(this);

        ll_proType = (LinearLayout) findViewById(R.id.ll_proType);
        ll_time_start = (LinearLayout) findViewById(R.id.ll_time_start);
        ll_time_end = (LinearLayout) findViewById(R.id.ll_time_end);
        ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
        view_proType = findViewById(R.id.view_proType);
        view_time_start = findViewById(R.id.view_time_start);
        view_time_end = findViewById(R.id.view_time_end);
        view_contact = findViewById(R.id.view_contact);

        et_title = (EditText) findViewById(R.id.et_title);
        et_intro = (EditText) findViewById(R.id.et_intro);
        et_content = (EditText) findViewById(R.id.et_content);
        et_proType = (EditText) findViewById(R.id.et_proType);
        et_contact = (EditText) findViewById(R.id.et_contact);

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

    /*隐藏类型没有的属性*/
    public void initgone() {
        if (categoryType == 1) {
            ll_proType.setVisibility(View.GONE);
            ll_contact.setVisibility(View.GONE);
            view_proType.setVisibility(View.GONE);
            view_contact.setVisibility(View.GONE);
        } else if (categoryType == 2) {
            ll_time_start.setVisibility(View.GONE);
            ll_time_end.setVisibility(View.GONE);
            ll_contact.setVisibility(View.GONE);
            view_time_start.setVisibility(View.GONE);
            view_time_end.setVisibility(View.GONE);
            view_contact.setVisibility(View.GONE);
        } else if (categoryType == 3 || categoryType == 4) {
            ll_proType.setVisibility(View.GONE);
            ll_time_start.setVisibility(View.GONE);
            ll_time_end.setVisibility(View.GONE);
            view_proType.setVisibility(View.GONE);
            view_time_start.setVisibility(View.GONE);
            view_time_end.setVisibility(View.GONE);
        } else if (categoryType == 5) {
            ll_time_start.setVisibility(View.GONE);
            ll_time_end.setVisibility(View.GONE);
            ll_contact.setVisibility(View.GONE);
            ll_proType.setVisibility(View.GONE);
            view_time_start.setVisibility(View.GONE);
            view_time_end.setVisibility(View.GONE);
            view_contact.setVisibility(View.GONE);
            view_proType.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                hintKbTwo();
                finish();
                break;
            case R.id.top_tv_ok://保存
                comsave();
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
                        sStartTime = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        btn_time_start.setText(sStartTime);
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
                        sEndTime = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        btn_time_end.setText(sEndTime);
                    }
                }, 1900 + year2, month2, day2);
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.show();
                break;
            case R.id.iv_photo1:
                isPhoto1 = true;
                showphotodialg(view);
                break;
            case R.id.iv_photo2:
                showphotodialg(view);
                isPhoto2 = true;
                break;
            case R.id.iv_photo3:
                showphotodialg(view);
                isPhoto3 = true;
                break;
            case R.id.iv_photo4:
                showphotodialg(view);
                isPhoto4 = true;
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
    public void comsave() {
        title = et_title.getText().toString();
        intro = et_intro.getText().toString();
        content = et_content.getText().toString();
        protype = et_proType.getText().toString();
        contact = et_contact.getText().toString();

        mCommonProperty.setCategoryType(categoryType);
        mCommonProperty.setCommonTitle(title);
        mCommonProperty.setCommonIntro(intro);
        mCommonProperty.setCommonContent(content);

        mCommonProperty.setCommonPic1(photopath1);
        mCommonProperty.setCommonPic2(photopath2);
        mCommonProperty.setCommonPic3(photopath3);
        mCommonProperty.setCommonPic4(photopath4);
        Date creattime = new Date();
//        mCommonProperty.setCreatetime(creattime);
        mCommonProperty.setCreatetime("" + creattime.getTime());
        mCommonProperty.setPraisenum(0);
        mCommonProperty.setBrowsenum(0);
//        mCommonProperty.setUserId(0);
        mCommonProperty.setUsermapperid(0);
        mCommonProperty.setExpstartTime(sStartTime);
        mCommonProperty.setExpendTime(sEndTime);
//        mCommonProperty.setProType(protype);
        mCommonProperty.setProType(0);//问题类型？？？
        mCommonProperty.setGoodscontact(contact);
        mCommonProperty.setUsermapperid(muserid);

        createDate = new Date();
        if ("".equals(title) && "".equals(intro) && "".equals(content)) {
            Toast.makeText(this, "请填写内容", Toast.LENGTH_SHORT).show();
        } else {
            L.d(TAG, "mCommonProperty::" + mCommonProperty.toString());
//            if (photopath1 != null && !"".equals(photopath1)) {
//                uploadImg(photopath1);
//            }
            okhttpPublish();
        }
    }

    /**
     * 上传图片到服务器，返回URL
     */
    private String uploadImg(String photopath) {
        dialogProcess.show();
        final String url = CommonUrls.SERVER_USER_UPDATE_AVATAR;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();
        File f = new File(photopath);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("id", "" + muserid);
        builder.addFormDataPart("avatar", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));

        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
                PublishCommonPropertyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadResult = "";
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out: 上传照片成功：response = {"code":0,"msg":"成功","data":{"avatar":"user/20170830_132011IMG_20170824_211308_642.jpg"}}
                String result = response.body().string();
                Log.d("TAG" + "onresponse result:", result);

                Gson gson = new Gson();
                //  获得 解析者
                JsonParser parser = new JsonParser();
                //  获得 根节点元素
                JsonElement root = parser.parse(result);
                //  根据 文档判断根节点属于 什么类型的 Gson节点对象
                // 假如文档 显示 根节点 为对象类型
                // 获得 根节点 的实际 节点类型
                JsonObject element = root.getAsJsonObject();
                //  取得 节点 下 的某个节点的 value
                // 获得 name 节点的值，name 节点为基本数据节点
                JsonPrimitive codeJson = element.getAsJsonPrimitive("code");
                int code = codeJson.getAsInt();
                JsonPrimitive msgJson = element.getAsJsonPrimitive("msg");
                final String msg = msgJson.getAsString();

                if (code == 0) {
                    final JsonObject dataJson = element.getAsJsonObject("data");
                    uploadResult = dataJson.get("avatar").toString();
                    uploadResult = uploadResult.replace("\"", "");
                    L.d(TAG, "uploadResult:" + uploadResult);
                    resultPhotoURL1 = uploadResult;
                    okhttpPublish();
                } else {
                    PublishCommonPropertyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadResult = "";
                        }
                    });
                }
            }
        });
        return uploadResult;
    }

    /**
     * 保存到服务器
     */
    private void okhttpPublish() {
        dialogProcess.show();
        String startTime, endTime;
        if (startDate != null) {
            startTime = "" + startDate.getTime();
        } else {
            startTime = "-1";
        }
        if (endDate != null) {
            endTime = "" + endDate.getTime();
        } else {
            endTime = "-1";
        }

        final String url = CommonUrls.SERVER_PUBLISH;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3000, TimeUnit.MILLISECONDS).build();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("id", "" + muserid);
        if (photopath1 != null && !"".equals(photopath1)) {
            File f1 = new File(photopath1);
            builder.addFormDataPart("picture", f1.getName(), RequestBody.create(MEDIA_TYPE_PNG, f1));
        }
        if (photopath2 != null && !"".equals(photopath2)) {
            File f2 = new File(photopath2);
            builder.addFormDataPart("picture", f2.getName(), RequestBody.create(MEDIA_TYPE_PNG, f2));
        }
        if (photopath3 != null && !"".equals(photopath3)) {
            File f3 = new File(photopath3);
            builder.addFormDataPart("picture", f3.getName(), RequestBody.create(MEDIA_TYPE_PNG, f3));
        }
        if (photopath4 != null && !"".equals(photopath4)) {
            File f4 = new File(photopath4);
            builder.addFormDataPart("picture", f4.getName(), RequestBody.create(MEDIA_TYPE_PNG, f4));
        }
        builder.addFormDataPart("categoryType", "" + categoryType)
                .addFormDataPart("commonTitle", "" + title)
                .addFormDataPart("commonIntro", "" + intro)
                .addFormDataPart("commonContent", "" + content)
//                .addFormDataPart("commonPic1", resultPhotoURL1)
//                .addFormDataPart("commonPic2", resultPhotoURL2)
//                .addFormDataPart("commonPic3", resultPhotoURL3)
//                .addFormDataPart("commonPic4", resultPhotoURL4)
//                .addFormDataPart("expstartTime", startTime)
//                .addFormDataPart("expendTime", endTime)
                .addFormDataPart("proType", "" + protype)
                .addFormDataPart("goodscontact", "" + contact)
                .addFormDataPart("usermapperid", "" + muserid)
                .build();
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "onFailure" + e.toString());
                PublishCommonPropertyActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProcess.dismiss();
                        Toast.makeText(PublishCommonPropertyActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.d("TAG" + "onresponse result:", result);

                Gson gson = new Gson();
                //  获得 解析者
                JsonParser parser = new JsonParser();
                //  获得 根节点元素
                JsonElement root = parser.parse(result);
                //  根据 文档判断根节点属于 什么类型的 Gson节点对象
                // 假如文档 显示 根节点 为对象类型
                // 获得 根节点 的实际 节点类型
                JsonObject element = root.getAsJsonObject();
                //  取得 节点 下 的某个节点的 value
                // 获得 name 节点的值，name 节点为基本数据节点
                JsonPrimitive codeJson = element.getAsJsonPrimitive("code");
                int code = codeJson.getAsInt();
                JsonPrimitive msgJson = element.getAsJsonPrimitive("msg");
                final String msg = msgJson.getAsString();

                if (code == 0) {
                    PublishCommonPropertyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            Toast.makeText(PublishCommonPropertyActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                    hintKbTwo();
                    finish();
                } else {
                    PublishCommonPropertyActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProcess.dismiss();
                            Toast.makeText(PublishCommonPropertyActivity.this, "发布失败:" + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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

    /*关闭软键盘*/
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
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
                mImageUri = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
            } else if (isPhoto2) {
                mImageUri2 = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
            } else if (isPhoto3) {
                mImageUri3 = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
            } else if (isPhoto4) {
                mImageUri4 = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
            }
        } else {
            if (isPhoto1) {
                mImageUri = Uri.fromFile(mFile);
            } else if (isPhoto2) {
                mImageUri2 = Uri.fromFile(mFile);
            } else if (isPhoto3) {
                mImageUri3 = Uri.fromFile(mFile);
            } else if (isPhoto4) {
                mImageUri4 = Uri.fromFile(mFile);
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
        if (isPhoto1) {
            uri = mImageUri;
            id = TAKE_PHOTO;
        } else if (isPhoto2) {
            uri = mImageUri2;
            id = TAKE_PHOTO2;
        } else if (isPhoto3) {
            uri = mImageUri3;
            id = TAKE_PHOTO3;
        } else {
            uri = mImageUri4;
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
                    photopath1 = mImageUri.getPath();
                    Glide.with(this).load(mImageUri).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo1);
                }
                isPhoto1 = false;
                if (photopath1 != null) {
                    iv_photo2.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo2.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath2 = mImageUri2.getPath();
                    Glide.with(this).load(mImageUri2).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo2);
                }
                isPhoto2 = false;
                if (photopath2 != null) {
                    iv_photo3.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO3:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo3.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath3 = mImageUri3.getPath();
                    Glide.with(this).load(mImageUri3).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo3);
                }
                isPhoto3 = false;
                if (photopath3 != null) {
                    iv_photo4.setVisibility(View.VISIBLE);
                }
                break;
            case TAKE_PHOTO4:
                if (resultCode == RESULT_OK) {
                    if (!iv_photo4.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                        iv_photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                    photopath4 = mImageUri4.getPath();
                    Glide.with(this).load(mImageUri4).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo4);
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
                        mImageUri = selectedImage;
                        photopath1 = path;
                        isPhoto1 = false;
                        if (photopath1 != null) {
                            iv_photo2.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto2) {
                        if (!iv_photo2.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo2);
                        mImageUri2 = selectedImage;
                        photopath2 = path;
                        isPhoto2 = false;
                        if (photopath2 != null) {
                            iv_photo3.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto3) {
                        if (!iv_photo3.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo3);
                        mImageUri3 = selectedImage;
                        photopath3 = path;
                        isPhoto3 = false;
                        if (photopath3 != null) {
                            iv_photo4.setVisibility(View.VISIBLE);
                        }
                    } else if (isPhoto4) {
                        if (!iv_photo4.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            iv_photo4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv_photo4);
                        mImageUri4 = selectedImage;
                        photopath4 = path;
                        isPhoto4 = false;
                    }
                }
                break;
        }
    }

}
