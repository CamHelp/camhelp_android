package com.camhelp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by storm on 2017-09-06.
 * 调用PicCompression实现压缩
 */

public class CompressionFileUtils {
    private String TAG = "CompressionFileUtils";
    PicCompression picCompression = new PicCompression();
    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                L.d(TAG, "压缩前：savelocalFile 复制成功");
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public File yasuo(String photopath) {
        Date date = new Date();
        File f = new File(photopath);
        // 先拼接好一个路径：在内存卡/或是手机内存上做好文件夹
        File dir = new File(Environment.getExternalStorageDirectory() + "/camhelp/campus/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = Environment.getExternalStorageDirectory() + "/camhelp/campus/" + date.getTime() + ".png";
        File savelocalFile = new File(filePath);
        L.d(TAG, "上传图片原大小：" + f.length());
        if (f.length() / 1000.0 < 300) {//小于300k，直接上传
            L.d(TAG, "上传图片大小：" + f.length());
            return f;
        } else if (f.length() / 1000.0 < 1000) {//小于1000k
            Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
            picCompression.compressImageToFile(bitmap, savelocalFile);
            L.d(TAG, "上传图片大小：" + savelocalFile.length());
            return savelocalFile;
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
            picCompression.compressImageToFile(bitmap, savelocalFile);
            L.d(TAG, "上传图片压缩1次大小：" + savelocalFile.length());
            Bitmap bitmap2 = BitmapFactory.decodeFile(savelocalFile.getPath());
            picCompression.compressBitmapToFile(bitmap2, savelocalFile);
            L.d(TAG, "上传图片压缩2次大小：" + savelocalFile.length());
            picCompression.compressBitmap(savelocalFile.getPath(), savelocalFile);
            L.d(TAG, "上传图片压缩3次大小：" + savelocalFile.length());
            L.d(TAG, "上传图片大小：" + savelocalFile.length());
            return savelocalFile;
        }
    }
}
