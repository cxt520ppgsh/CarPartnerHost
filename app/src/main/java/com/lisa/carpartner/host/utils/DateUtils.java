package com.lisa.carpartner.host.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDateUnFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getDateFormatSeconds() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getDateFormatYear() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getDateFormatDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static long getStringtoLong(String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dateFormat);
            Long timestamp = date.getTime();
            return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getLongToString(String longTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String time = sdf.format(Long.valueOf(longTime));
            return time;
        } catch (Exception e) {
            return longTime;
        }
    }

    public static String getLongToHHMMSS(long timeMilles) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String time = sdf.format(timeMilles);
            time = "00:" + time.substring(3, time.length());
            return time;
        } catch (Exception e) {
            return timeMilles + "";
        }
    }

    public static String getDateFormatMilles() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static long getStringtoLong() {
        return System.currentTimeMillis();
    }

    public static String getToday() {
        return DateUtils.getDateFormatSeconds().split(" ")[0];
    }

    public static long getDateLong() {
        return System.currentTimeMillis();
    }
}
