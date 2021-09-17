package com.kuanquan.doyincover.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/08/11
 * Description:
 */
public class TimeUtil {
  private final static long minute = 60 * 1000;// 1分钟
  private final static long hour = 60 * minute;// 1小时
  private final static long day = 24 * hour;// 1天
  private final static long month = 31 * day;// 月
  private final static long year = 12 * month;// 年

  /**
   * 返回文字描述的日期
   *
   * @param date
   * @return
   */
  public static String getTimeFormatText(Date date) {
    if (date == null) {
      return null;
    }
    long diff = new Date().getTime() - date.getTime();
    long r;
    if (diff > year) {
      return getStringDate(date);
    }
    if (diff > month) {
      r = (diff / month);
      return r + "个月前";
    }
    if (diff > day) {
      r = (diff / day);
      return r + "天前";
    }
    if (diff > hour) {
      r = (diff / hour);
      return r + "小时前";
    }
    if (diff > minute) {
      r = (diff / minute);
      return r + "分钟前";
    }
    return "刚刚";
  }

  /**
   * 获取现在时间
   *
   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
   */
  public static String getStringDate(Date time) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    return formatter.format(time);
  }

  /**
   * 返回 格式化 日期 yyyy-MM-dd
   */
  public static String format2YMD(long when) {
    SimpleDateFormat mouth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    return mouth.format(when);
  }

  /**
   * 获取当前时间字符串类型
   * 时间格式yyyy-MM-dd hh:mm:ss
   *
   * @return
   */
  public static String getCurrentTime() {
    return getCurrentTime("yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 获取当前时间字符串类型
   * 时间格式yyyy-MM-dd hh:mm:ss
   *
   * @return
   */
  public static String getCurrentTime(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String ret = sdf.format(new Date());
    return ret;
  }

  public static String formatSysTime(long when) {
    SimpleDateFormat mouth = new SimpleDateFormat("hh:mm", Locale.getDefault());

    Date date = new Date(when);
    long diff = new Date().getTime() - date.getTime();
    long r = 0;
    if (diff > year) {
      r = (diff / year);
      return r + "年前 " + mouth.format(when);
    }
    if (diff > month) {
      r = (diff / month);
      return r + "个月前 " + mouth.format(when);
    }
    if (diff > day) {
      r = (diff / day);
      return r + "天前 " + mouth.format(when);
    }
    return " " + mouth.format(when);
  }

  public static String formateSysTimeTwo(long when) {
    SimpleDateFormat mouth = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
    return mouth.format(when);
  }

  /**
   * 判断两个时间是不是同一天
   */
  public static boolean checkIfSameDay(long timeOne, long timeTwo) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String one = sdf.format(new Date(timeOne));
    String two = sdf.format(new Date(timeTwo));
      return one.equals(two);
  }
}
