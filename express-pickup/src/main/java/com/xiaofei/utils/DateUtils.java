package com.xiaofei.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tuaofei
 * @description TODO
 * @date 2024/7/31
 */
public class DateUtils {

    // 默认的年月日的格式. yyyy-MM-dd
    public static final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期
     */
    public static String format(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATETIME);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }


    /**
     * 判断当前时间是否为当前
     */
    public static Boolean isNowDay(Date date) {
        // 当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(PATTEN_DEFAULT_YMD);
        //获取今天的日期
        String nowDay = sf.format(now);
        //对比的时间
        String day = sf.format(date);
        return day.equals(nowDay);
    }

    /**
     * 在原日期的基础上增加小时数
     *
     * @param date
     * @param addHours
     * @return
     */
    public static Date addHour(Date date, Integer addHours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, addHours);
        Date newDate = c.getTime();
        return newDate;
    }

    /**
     * 在原日期的基础上增加天数
     *
     * @param date
     * @param addDays
     * @return
     */
    public static Date addDay(Date date, Integer addDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, addDays);
        Date newDate = c.getTime();
        return newDate;
    }
}
