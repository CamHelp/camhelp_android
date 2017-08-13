package com.camhelp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by storm on 2017-08-13.
 */

public class DateConversionUtils {
    public String sdateToStrign(String stime){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        long now = Long.parseLong(stime);
        calendar.setTimeInMillis(now);
        String formatTime = formatter.format(calendar.getTime());
        return formatTime;
    }
}
