package com.example.guaranty.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020 15:08
 */
@Slf4j
public class DateUtils {

    public static long getExpireTime(long currentTime, long milliSeconds) {
        return currentTime + milliSeconds;
    }

    public static long getExpireTimeOfSeconds(long currentTime, int seconds) {
        return currentTime + seconds * 1000;
    }

    public static String getDateWithFile() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(new Date());
    }

    public static long dateToTimestamp(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        if (StringUtils.isBlank(dateStr)) {
            return 0L;
        }
        Date date = df.parse(dateStr);
        return date.getTime() ;
    }

    public static void main(String[] args) throws ParseException {
        String str = "20200804";
        System.out.println(dateToTimestamp(str, "yyyyMMdd"));
    }


    /**
     * 获取年月 ：2020-07
     */
    public static String getYearAndMonth(Long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(new Date(time));
    }

    public static String subSignYear(String yyyyMM) {
        if (StringUtils.isNotBlank(yyyyMM)) {
            return yyyyMM.split("-")[0];
        }
        return null;
    }

    public static String subSignmonth(String yyyyMM) {
        if (StringUtils.isNotBlank(yyyyMM)) {

            Integer m = Integer.parseInt(yyyyMM.split("-")[1]);

            return String.valueOf(m);
        }
        return null;
    }


    /**
     * date2比date1多的天数
     */
    public static int differentDays(Long startTime, Long endTime) {

        Date date1 = new Date(startTime);

        Date date2 = new Date(endTime);

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            return day2 - day1;
        }
    }

    /**
     * 获取当前是当月的第几天
     */
    public static Integer getMDay() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(formatDate(sdf.format(new Date())));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getYear() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(formatDate(sdf.format(new Date())));

        return calendar.get(Calendar.YEAR);
    }

    public static Integer getMonth() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(formatDate(sdf.format(new Date())));

        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getWeek() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(formatDate(sdf.format(new Date())));

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static Integer getHour() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(formatDate(sdf.format(new Date())));

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前周的开始时间（周一 00:00）
     */
    public static Long getWeekStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 1);

        long result = cal.getTime().getTime();

        // 毫秒的时间戳会有误差，这里做截取处理
        return Long.valueOf(String.valueOf(result).trim().substring(0, 10) + "000");
    }

    /**
     * 本周结束时间戳
     */
    public static Long getWeekEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23,
                59, 59);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        cal.add(Calendar.DATE, -day_of_week + 7);
        long result = cal.getTime().getTime();
        // 毫秒的时间戳会有误差，这里做截取处理
        return Long.valueOf(String.valueOf(result).trim().substring(0, 10) + "000");
    }

    /**
     * 获取当月开始时间戳
     */
    public static Long getMonthStartTime() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当月的结束时间戳
     */
    public static Long getMonthEndTime() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String formatDate(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static Date formatDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException ignored) {
        }
        return new Date(date);
    }

    public static Date formatDate(String date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Long time) {
        return formatDate(time, "yyyy-MM-dd HH:mm:ss");
    }

}
