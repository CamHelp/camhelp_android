package com.camhelp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.camhelp.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by storm on 2017-08-09.
 */

public class LookLargeImg {
    private final static String TAG = "IcsTestActivity";
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/campusdownload/";
    private ImageView mImageView;
    private Button mBtnSave;
    private ProgressDialog mSaveDialog = null;
    private Bitmap mBitmap;
    private String mFileName;
    private String mSaveMessage;
    Context mcontext;
    String url;

    public void looklargeimg(String imgurl, Context context) {
        mcontext = context;
        url = imgurl;
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

        ImageView iv_download = (ImageView) imgEntryView.findViewById(R.id.iv_download);
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mcontext, "下载", Toast.LENGTH_SHORT).show();
//                mSaveDialog = ProgressDialog.show(mcontext, "保存图片", "图片正在保存中，请稍等...", true);
//                new Thread(saveFileRunnable).start();
            }
        });
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * Get data from stream
     *
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                saveFile(mBitmap, mFileName);
                mSaveMessage = "图片保存成功！";
            } catch (IOException e) {
                mSaveMessage = "图片保存失败！";
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }

    };

    private Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSaveDialog.dismiss();
            Log.d(TAG, mSaveMessage);
            Toast.makeText(mcontext, mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };

    /*
     * 连接网络
     * 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
     */
    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = url;
                Date date = new Date();
                mFileName = ""+date.getTime()+".jpg";

                //以下是取得图片的两种方法
                //////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
                byte[] data = getImage(filePath);
                if (data != null) {
                    mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                } else {
                    Toast.makeText(mcontext, "Image error!", Toast.LENGTH_SHORT).show();
                }
                ////////////////////////////////////////////////////////

                //******** 方法2：取得的是InputStream，直接从InputStream生成bitmap ***********/
                mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
                //********************************************************************/

                // 发送消息，通知handler在主线程中更新UI
                connectHanlder.sendEmptyMessage(0);
                Log.d(TAG, "set image ...");
            } catch (Exception e) {
                Toast.makeText(mcontext, "无法链接网络！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    };

    private Handler connectHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "display image");
            // 更新UI，显示图片
            if (mBitmap != null) {
                mImageView.setImageBitmap(mBitmap);// display image
            }
        }
    };

}