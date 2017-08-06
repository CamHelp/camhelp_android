package com.camhelp.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by 周彬 on 2017/3/13.
 * 日志打印工具类，控制日志输出
 */

public class L {
    private static final String TAG_DEBUG = "Debug";
    private static final String TAG_VERBOSE = "Verbose";
    private static final String TAG_INFO = "Information";
    private static final String TAG_WARNING = "Warning";
    private static final String TAG_ERROR = "Error";
    private static boolean verbose = true;
    private static boolean info = true;
    private static boolean debug = true;
    private static boolean warning = true;
    private static boolean error = true;

    public static void d(@NonNull String msg) {
        d(TAG_DEBUG, msg);
    }

    public static void d(String TAG, @NonNull String msg) {

        if (debug) {
            Log.d(TAG, msg);
        }
    }


    public static void i(@NonNull String msg) {
        i(TAG_INFO, msg);
    }

    public static void i(String TAG, @NonNull String msg) {

        if (info) {
            Log.d(TAG, msg);
        }
    }

    public static void v(@NonNull String msg) {
        v(TAG_VERBOSE, msg);
    }

    public static void v(String TAG, @NonNull String msg) {

        if (verbose) {
            Log.d(TAG, msg);
        }
    }

    public static void w(@NonNull String msg) {
        w(TAG_WARNING, msg);
    }

    public static void w(String TAG, @NonNull String msg) {

        if (warning) {
            Log.d(TAG, msg);
        }
    }

    public static void e(@NonNull String msg) {
        e(TAG_ERROR, msg);
    }

    public static void e(String TAG, @NonNull String msg) {

        if (error) {
            Log.d(TAG, msg);
        }
    }

}
