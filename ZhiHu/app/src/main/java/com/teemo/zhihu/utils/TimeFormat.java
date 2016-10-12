package com.teemo.zhihu.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/9/7.
 */

public class TimeFormat {

    public static String formatLong(long longTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(longTime);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        return time.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        return time.format(date);
    }

    public static String getCurrentDateLong() {
        Date date = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(date);
    }

    public static String getStartDate() {
        return "1970-01-01";
    }
}
