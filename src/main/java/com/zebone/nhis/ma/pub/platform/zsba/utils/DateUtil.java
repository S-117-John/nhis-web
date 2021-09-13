package com.zebone.nhis.ma.pub.platform.zsba.utils;

import java.util.Calendar;

public class DateUtil {

    /**
     * 获取小时数
     *
     * @param timestamp 时间戳
     * @return 时
     */
    public static int getHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 根据时间戳获取当天起始的时间戳
     *
     * @param timestamp 时间戳，单位为ms
     * @return 当天开始的时间戳
     */
    public static long getDayStartTime(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }
}