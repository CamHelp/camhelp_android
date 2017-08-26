package com.camhelp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by storm on 2017-08-13.
 * 日期格式转换，数据库存的是字符串型的日期秒数
 */

public class DateConversionUtils {
    public String sdateToString(String stime){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        String formatTime = formatter.format(calendar.getTime());
        return formatTime;
    }
    public String sdateToStringBirthday(String stime){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        String formatTime = formatter.format(calendar.getTime());
        return formatTime;
    }
}
