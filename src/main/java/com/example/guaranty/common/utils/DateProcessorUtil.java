package com.example.guaranty.common.utils;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间处理工具类
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/8/22 17:27
 **/
@Slf4j
public class DateProcessorUtil {
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    private final static String DATE_TIME_PATTERN = DatePattern.NORM_DATETIME_PATTERN;

    private static Calendar calendar = Calendar.getInstance();

    public static long dateToTimestamp(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = df.parse(dateStr);
        return date.getTime() ;
    }


    public static Date formatDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return new Date(date);
    }

    public static String formatDate(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }
    /**
     * 距离今天多久
     *
     * @param date 时间
     * @return string
     */
    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR) {
            return ago / ONE_MINUTE + "分钟前";
        } else if (ago <= ONE_DAY) {
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE)
                    + "分钟前";
        } else if (ago <= ONE_DAY * 2) {
            String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "";
            return "昨天 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + minute;
        } else if (ago <= ONE_DAY * 3) {
            String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "";
            return "前天 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + minute;
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "";
            return day + "天前 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + minute;
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            String minute = calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE) + "";
            return month + "个月" + day + "天前 "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + minute;
        } else {
            long year = ago / ONE_YEAR;
            // JANUARY which is 0 so month+1
            int month = calendar.get(Calendar.MONTH) + 1;
            return year + "年前" + month + "月" + calendar.get(Calendar.DATE)
                    + "日";
        }

    }

    /**
     * 距离截止日期还有多长时间
     *
     * @param date 时间对象
     * @return String
     */
    public static String fromDeadline(Date date) {
        long deadline = date.getTime() / 1000;
        long now = (System.currentTimeMillis()) / 1000;
        long remain = deadline - now;
        if (remain <= ONE_HOUR) {
            return "只剩下" + remain / ONE_MINUTE + "分钟";
        } else if (remain <= ONE_DAY) {
            return "只剩下" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        } else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }

    }

    /**
     * 距离今天的绝对时间
     *
     * @param date
     * @return
     */
    public static String toToday(Date date) {
        long time = date.getTime() / 1000;
        long now = (System.currentTimeMillis()) / 1000;
        long ago = now - time;
        if (ago <= ONE_HOUR) {
            return ago / ONE_MINUTE + "分钟";
        } else if (ago <= ONE_DAY) {
            return ago / ONE_HOUR + "小时" + (ago % ONE_HOUR / ONE_MINUTE) + "分钟";
        } else if (ago <= ONE_DAY * 2) {
            return "昨天" + (ago - ONE_DAY) / ONE_HOUR + "点" + (ago - ONE_DAY)
                    % ONE_HOUR / ONE_MINUTE + "分";
        } else if (ago <= ONE_DAY * 3) {
            long hour = ago - ONE_DAY * 2;
            return "前天" + hour / ONE_HOUR + "点" + hour % ONE_HOUR / ONE_MINUTE
                    + "分";
        } else if (ago <= ONE_MONTH) {
            long day = ago / ONE_DAY;
            long hour = ago % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return day + "天前" + hour + "点" + minute + "分";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            long hour = ago % ONE_MONTH % ONE_DAY / ONE_HOUR;
            long minute = ago % ONE_MONTH % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return month + "个月" + day + "天" + hour + "点" + minute + "分前";
        } else {
            long year = ago / ONE_YEAR;
            long month = ago % ONE_YEAR / ONE_MONTH;
            long day = ago % ONE_YEAR % ONE_MONTH / ONE_DAY;
            return year + "年前" + month + "月" + day + "天";
        }

    }


    public static String getYear() {
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth() {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }

    public static String getDay() {
        return calendar.get(Calendar.DATE) + "";
    }

    public static String get24Hour() {
        return calendar.get(Calendar.HOUR_OF_DAY) + "";
    }

    public static String getMinute() {
        return calendar.get(Calendar.MINUTE) + "";
    }

    /**
     * 获取秒
     *
     * @return String 秒
     */
    public static String getSecond() {
        return calendar.get(Calendar.SECOND) + "";
    }

    /**
     * 获取本月日期
     */
    public static List getDayListOfMonth() {
        List<String> list = new ArrayList<>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        //月份
        int month = aCalendar.get(Calendar.MONTH) + 1;
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String darStr = month + "月" + i + "日";
            list.add(darStr);
        }
        return list;
    }

    /**
     * 获取当前月的天数
     */
    public static int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        return aCalendar.getActualMaximum(Calendar.DATE);
    }

    public static String format(Date date, String pattern) {
        if (null != date) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 将时间戳转换成自定格式的字符串
     *
     * @param time    时间戳
     * @param pattern 格式字符串
     * @return
     */
    public static String timeToStr(Long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (time.toString().length() < 13) {
            time = time * 1000L;
        }
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    /**
     * 字符串转时间
     *
     * @param timeStr 时间字符串
     * @return long 时间戳
     */
    public static long strToTime(String timeStr) {
        Date time = strToDate(timeStr);
        return time.getTime() / 1000;
    }

    /**
     * @param date 时间对象
     * @return String
     */
    public static String dateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        return sdf.format(date);
    }

    /**
     * 转换为时间类型格式
     *
     * @param strDate 日期
     * @return Date
     */
    public static Date strToDate(String strDate) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_PATTERN);
            return new Date((sf.parse(strDate).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将字符串类型的转换成Date类型
     *
     * @param dateStr 字符串类型的日期 yyyy-MM-dd
     * @return Date类型的日期
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateStr) {
        // 返回的日期
        Date resultDate = null;
        try {
            // 日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            resultDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    private static List<Date> dateSplit(Date startDate, Date endDate) throws Exception {
        if (!startDate.before(endDate)) {
            throw new Exception("开始时间应该在结束时间之后");
        }
        Long spi = endDate.getTime() - startDate.getTime();
        // 相隔天数
        Long step = spi / (24 * 60 * 60 * 1000);

        List<Date> dateList = new ArrayList<>();
        dateList.add(endDate);
        for (int i = 1; i <= step; i++) {
            // 比上一天减一
            dateList.add(new Date(dateList.get(i - 1).getTime() - (24 * 60 * 60 * 1000)));
        }
        return dateList;
    }

    public static List<Date> dateForeach(String beginDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(beginDate);
            Date end = sdf.parse(endDate);
            List<Date> lists = dateSplit(start, end);
            Collections.reverse(lists);
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<Date> dateForeach(String dateFormat, String beginDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date start = sdf.parse(beginDate);
            Date end = sdf.parse(endDate);
            List<Date> lists = dateSplit(start, end);
            Collections.reverse(lists);
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 计算几个月之后的时间  到当天的23::59
     *
     * @param date  当前时间
     * @param month 几个月
     * @return string
     */
    public static Date addMonth(Date date, int month) {
        String nowDate = null;
        String dateStr = dateToStr(date);
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            Date parse = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);

            // 设置时间为当天的23:59
            calendar.add(Calendar.MONTH, month);

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            nowDate = format.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strToDate(nowDate);
    }
}
