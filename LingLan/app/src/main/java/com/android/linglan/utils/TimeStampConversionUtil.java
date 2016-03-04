package com.android.linglan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/23 0023.
 */
public class TimeStampConversionUtil {

    public static String getDate(String publishtime) {
        // TODO Auto-generated method stub
        if (publishtime != null && !publishtime.equals("")) {
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
            return data.format(new Date(Long.parseLong(publishtime) * 1000L));
        }
        return "";
    }

    public static String getDateTime(String publishtime) {
        // TODO Auto-generated method stub
        if (publishtime != null && !publishtime.equals("")) {
            SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            return data.format(new Date(Long.parseLong(publishtime) * 1000L));
        }
        return "";
    }
}
