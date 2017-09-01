package com.camhelp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;
import com.camhelp.common.CommonGlobal;
import com.camhelp.common.CommonUrls;
import com.camhelp.entity.User;
import com.camhelp.entity.UserVO;
import com.camhelp.fragment.MineFragment;
import com.camhelp.utils.DateConversionUtils;
import com.camhelp.utils.L;
import com.camhelp.utils.MiPictureHelper;
import com.camhelp.utils.MyProcessDialog;
import com.camhelp.utils.NativeUtil;
import com.camhelp.utils.PicCompression;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 个人资料编辑
 */
public class MineCenterActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MineCenterActivity";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String colorPrimary, colorPrimaryBlew, colorPrimaryDark, colorAccent;
    //    User mUser = new User();
    UserVO mUser = new UserVO();

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
    private String username, intro, phone, email, address, birthday;
    private int sex;
    private Date birthdayDate;

    private ImageView iv_mine_back;
    private CircleImageView cimg_mine_avatar;
    private EditText et_mine_username, et_mine_intro, et_mine_phone, et_mine_email, et_mine_address;
    private RadioGroup radiogroup_sex;
    private RadioButton radiobtn_male, radiobtn_fmale, radiobtn_secret;
    private Button btn_birthday;

    Dialog dialogProcess;
    private DateConversionUtils dateConversionUtils = new DateConversionUtils();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final String externalStorageDirectory = Environment.getExternalStorageDirectory().getPath()+"/camhelp/";
    String uploadResult = "";
    private boolean isUpdateAvatar, isBg;
    PicCompression picCompression = new PicCompression();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_center);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userInit();
        initcolor();
        inittitle();
        initview();
        initdata();
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
        dialogProcess = MyProcessDialog.showDialog(this);
        iv_mine_back = (ImageView) findViewById(R.id.iv_mine_back);
        cimg_mine_avatar = (CircleImageView) findViewById(R.id.cimg_mine_avatar);
        iv_mine_back.setOnClickListener(this);
        cimg_mine_avatar.setOnClickListener(this);

        et_mine_username = (EditText) findViewById(R.id.et_mine_username);
        et_mine_intro = (EditText) findViewById(R.id.et_mine_intro);
        et_mine_phone = (EditText) findViewById(R.id.et_mine_phone);
        et_mine_email = (EditText) findViewById(R.id.et_mine_email);
        et_mine_address = (EditText) findViewById(R.id.et_mine_address);
        btn_birthday = (Button) findViewById(R.id.btn_birthday);
        btn_birthday.setOnClickListener(this);

        radiogroup_sex = (RadioGroup) findViewById(R.id.radiogroup_sex);
        radiobtn_male = (RadioButton) findViewById(R.id.radiobtn_male);
        radiobtn_fmale = (RadioButton) findViewById(R.id.radiobtn_fmale);
        radiobtn_secret = (RadioButton) findViewById(R.id.radiobtn_secret);
    }

    public void initdata() {
        photo1path = mUser.getBgpicture();
        photo2path = mUser.getAvatar();
        username = mUser.getNickname();
        intro = mUser.getIntro();
        phone = mUser.getTelephone();
        email = mUser.getEmail();
        address = mUser.getAddress();
        birthday = mUser.getBirthday();

        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + photo1path)
                .error(R.drawable.mine_bg)
                .placeholder(R.drawable.mine_bg)
                .into(iv_mine_back);
        Glide.with(this).load(CommonUrls.SERVER_ADDRESS_PIC + photo2path)
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(cimg_mine_avatar);

        if (mUser.getSex() == null) {
            sex = -1;
        } else {
            sex = mUser.getSex();
        }

        if (username != null || "".equals(username)) {
            et_mine_username.setText(username);
        }
        if (intro != null || "".equals(intro)) {
            et_mine_intro.setText(intro);
        }
        if (phone != null || "".equals(phone)) {
            et_mine_phone.setText(phone);
        }
        if (email != null || "".equals(email)) {
            et_mine_email.setText(email);
        }
        if (address != null || "".equals(address)) {
            et_mine_address.setText(address);
        }
        if (birthday != null || "".equals(birthday)) {
            btn_birthday.setText(dateConversionUtils.sdateToStringBirthday(birthday));
        }
        if (sex == 0) {
            radiobtn_male.setChecked(true);
        } else if (sex == 1) {
            radiobtn_fmale.setChecked(true);
        } else if (sex == -1) {
            radiobtn_secret.setChecked(true);
        }


    }


    /**
     * 上传图片到服务器，返回URL
     */
    private void uploadImg() {
        dialogProcess.show();
        final String url;
        File f;
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30000, TimeUnit.MILLISECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("id", "" + mUser.getUserID());
        if (!isUpdateAvatar) {
            url = CommonUrls.SERVER_USER_UPDATE_BG;
            f = new File(photo2path);
            Bitmap bitmap= BitmapFactory.decodeFile(photo2path);
            picCompression.compressImageToFile(bitmap,f);
            Bitmap bitmap2= BitmapFactory.decodeFile(f.getPath());
            picCompression.compressBitmapToFile(bitmap2,f);
            picCompression.compressBitmap(f.getPath(),f);

            builder.addFormDataPart("bgpicture", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        } else {
            url = CommonUrls.SERVER_USER_UPDATE_AVATAR;
            f = new File(photo1path);
            /*压缩图片*/
//            NativeUtil.compressBitmap(f.getPath(),externalStorageDirectory+"/yasuo.jpg");
            Bitmap bitmap= BitmapFactory.decodeFile(photo1path);
            picCompression.compressImageToFile(bitmap,f);
            Bitmap bitmap2= BitmapFactory.decodeFile(f.getPath());
            picCompression.compressBitmapToFile(bitmap2,f);
            picCompression.compressBitmap(f.getPath(),f);

            builder.addFormDataPart("avatar", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
        }

        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
                MineCenterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MineCenterActivity.this, "上传失败，无法连接到服务器", Toast.LENGTH_SHORT).show();
                        isUpdateAvatar = false;
                        dialogProcess.dismiss();
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
                    if (!isUpdateAvatar) {
                        uploadResult = dataJson.get("bgpicture").toString();
                        uploadResult = uploadResult.replace("\"", "");
                        mUser.setBgpicture(uploadResult);
                    }else {
                        uploadResult = dataJson.get("avatar").toString();
                        uploadResult = uploadResult.replace("\"", "");
                        mUser.setAvatar(uploadResult);
                    }
                    saveUserVO(mUser);
                    isUpdateAvatar = false;
                    dialogProcess.dismiss();
                    MineCenterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MineCenterActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    MineCenterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MineCenterActivity.this, "上传失败：" + msg, Toast.LENGTH_SHORT).show();
                            isUpdateAvatar = false;
                            dialogProcess.dismiss();
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取用户
     */
    public void userInit() {
        mUser = getUserVO();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_return:
                hintKbTwo();
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
                isUpdateAvatar = true;
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
                BACK = false;
                isUpdateAvatar = false;
                break;
            case R.id.btn_birthday://生日
                Date date = new Date(System.currentTimeMillis());
                int year = date.getYear();
                int month = date.getMonth();
                int day = date.getDate();
                L.e(TAG, "year is : " + year + " month is " + month + " day is " + day);
                DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birthdayDate = new Date(year - 1900, month, dayOfMonth);
                        birthday = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        btn_birthday.setText(birthday);
                    }
                }, 1900 + year, month, day);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                break;
        }
    }

    public UserVO getUserVO() {
        String temp = pref.getString(CommonGlobal.userobj, "");
        L.d(TAG, temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        UserVO userVO = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            userVO = (UserVO) ois.readObject();
        } catch (IOException e) {
            L.d(TAG, e.toString());
        } catch (ClassNotFoundException e1) {
            L.d(TAG, e1.toString());
        }
        return userVO;
    }

    public void saveUserVO(UserVO userVO) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        editor = pref.edit();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(userVO);
            String temp = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            editor.putString(CommonGlobal.userobj, temp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        editor.apply();
    }

    /*保存*/
    public void minesave() {
        username = et_mine_username.getText().toString();
        intro = et_mine_intro.getText().toString();
        phone = et_mine_phone.getText().toString();
        email = et_mine_email.getText().toString();
        address = et_mine_address.getText().toString();
        if (radiobtn_male.isChecked()) {
            sex = 0;
        } else if (radiobtn_fmale.isChecked()) {
            sex = 1;
        } else {
            sex = -1;
        }
        mUser.setNickname(username);
        mUser.setIntro(intro);
        if (birthdayDate != null) {
            mUser.setBirthday("" + birthdayDate.getTime());
        }
        mUser.setTelephone(phone);
        mUser.setEmail(email);
        mUser.setAddress(address);
        mUser.setSex(sex);
        saveUserVO(mUser);

        hintKbTwo();
        Toast.makeText(this, "保存本地成功", Toast.LENGTH_SHORT).show();
        finish();
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
                imageUri = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
            } else {
                imageUri2 = FileProvider.getUriForFile(this, CommonUrls.FILEPROVIDER_PACKAGE_NAME, mFile);
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
                    uploadImg();//更新头像到服务器
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
                    uploadImg();//更新背景到服务器
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
                        imageUri2 = selectedImage;
                        photo2path = path;
                        BACK = false;
                    } else {
                        if (!cimg_mine_avatar.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            cimg_mine_avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        Glide.with(this).load(selectedImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(cimg_mine_avatar);
                        imageUri = selectedImage;
                        photo1path = path;
                    }
                    uploadImg();//更新到服务器
                }
                break;
        }
    }

}
