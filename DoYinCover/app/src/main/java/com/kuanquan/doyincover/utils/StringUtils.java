package com.kuanquan.doyincover.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author:  lijie
 * Date:   2018/12/27
 * Email:  2607401801@qq.com
 */
public class StringUtils {
  public static String replaceBlank(String src) {
    String dest = "";
    if (src != null) {
      Pattern pattern = Pattern.compile("\t|\r|\n|\\s*");
      Matcher matcher = pattern.matcher(src);
      dest = matcher.replaceAll("");
    }
    return dest;
  }

  public static boolean isEmpty(String src) {
    return src == null || src.isEmpty();
  }

  public static String md5(String string) {
    if (TextUtils.isEmpty(string)) {
      return "";
    }
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
      byte[] bytes = md5.digest(string.getBytes());
      StringBuilder result = new StringBuilder();
      for (byte b : bytes) {
        String temp = Integer.toHexString(b & 0xff);
        if (temp.length() == 1) {
          temp = "0" + temp;
        }
        result.append(temp);
      }
      return result.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }
}
