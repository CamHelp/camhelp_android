package com.camhelp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by storm on 2017-08-13.
 * 日期格式转换，数据库存的是字符串型的日期秒数
 */

public class DateConversionUtils {

    /*判断日期和当前是否在同一天*/
    public static boolean isToday(String stime){
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        if(fmt.format(calendar.getTime()).toString().equals(fmt.format(new Date()).toString())){//格式化为相同格式
            return true;
        }else {
            return false;
        }
    }/*判断日期和当前是否在同一年*/
    public static boolean isThisYear(String stime){
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy");
        if(fmt.format(calendar.getTime()).toString().equals(fmt.format(new Date()).toString())){//格式化为相同格式
            return true;
        }else {
            return false;
        }
    }

    public String sdateToString(String stime) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//HH为24小时制，hh是12小时制！
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        String formatTime = formatter.format(calendar.getTime());
        return formatTime;
    }

    public String sdateToString2(String stime) {
        String formatTime = "";//返回格式
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat formatter3 = new SimpleDateFormat("MM-dd HH:mm");
        DateFormat formatter2 = new SimpleDateFormat("HH:mm");
        Date nowTime = new Date();
        long ltime = Long.parseLong(stime);
        double fromNowMin = (nowTime.getTime() - ltime) / 60000.0; //距离现在分钟数

        if (fromNowMin < 1) {//距现在时间少于1分钟
            formatTime = "刚刚";
        } else if (fromNowMin < 10) {//距现在时间少于10分钟
            formatTime = "" + (int) fromNowMin + "分钟前";
        } else if (fromNowMin < 60) {//距现在时间少于60分钟
            formatTime = "" + (int) fromNowMin/10 + "0分钟前";
        }else if (isToday(stime)) {//在同一天，分
            Calendar calendar = Calendar.getInstance();
            long now = Long.parseLong(stime);
            calendar.setTimeInMillis(now);
            formatTime = formatter2.format(calendar.getTime());
        }else if (isThisYear(stime)) {//在同一年，月日分
            Calendar calendar = Calendar.getInstance();
            long now = Long.parseLong(stime);
            calendar.setTimeInMillis(now);
            formatTime = formatter3.format(calendar.getTime());
        } else {//显示具体日期，年月日分
            Calendar calendar = Calendar.getInstance();
            long now = Long.parseLong(stime);
            calendar.setTimeInMillis(now);
            formatTime = formatter.format(calendar.getTime());
        }

        return formatTime;
    }

    public String sdateToStringBirthday(String stime) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        String formatTime = formatter.format(calendar.getTime());
        return formatTime;
    }

    public static void main(String[] args) {
        DateConversionUtils dateConversionUtils = new DateConversionUtils();
        Date nowTime = new Date();
        System.out.println("nowtime:" + nowTime.getTime());
        System.out.println(dateConversionUtils.sdateToString2("1504453389400"));
    }
}
