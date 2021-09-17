package com.kuanquan.doyincover.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/11/19
 * Description:
 */
public class FileUtil {
  /*
   * Java文件操作 获取文件扩展名
   * */
  public static String getExtensionName(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length() - 1))) {
        return filename.substring(dot + 1);
      }
    }
    return filename;
  }

  /*
   * Java文件操作 获取不带扩展名的文件名
   * */
  public static String getFileNameNoEx(String filename) {
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        return filename.substring(0, dot);
      }
    }
    return filename;
  }

  /**
   * 复制文件
   *
   * @param source 输入文件
   * @param target 输出文件
   */
  public static String copyFile(File source, File target) {
    FileInputStream fileInputStream = null;
    FileOutputStream fileOutputStream = null;
    try {
      fileInputStream = new FileInputStream(source);
      fileOutputStream = new FileOutputStream(target);
      byte[] buffer = new byte[1024];
      while (fileInputStream.read(buffer) > 0) {
        fileOutputStream.write(buffer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (fileInputStream != null)
          fileInputStream.close();
        if (fileOutputStream != null)
          fileOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return target.getAbsolutePath();
  }

  /**
   * 删除单个文件
   *
   * @param filePath 被删除文件的文件名
   * @return 文件删除成功返回true，否则返回false
   */
  public static boolean deleteCatchFile(String filePath, Context context) {
    File file = new File(filePath);
    if (file.isFile() && file.exists() && filePath.contains(context.getApplicationContext().getPackageName())) {
      return file.delete();
    }
    return false;
  }

  /**
   * 删除单个文件
   */
  public static void deleteFile(String filePath) {
    File file = new File(filePath);
    if (file.isFile() && file.exists()) {
      file.delete();
    }
  }
}
