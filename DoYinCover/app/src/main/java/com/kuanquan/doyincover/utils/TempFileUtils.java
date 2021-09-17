package com.kuanquan.doyincover.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by twocity on 14-5-5.
 */
public class TempFileUtils {

  private static final long OUTOFDATE = 3 * 60 * 60 * 1000; //3hours
  private static final String TEMP_SUFFIX = "ricebooktmp";
  private static final String TEMP_PREFIX = "temp_";

  private TempFileUtils() {
  }

  public static File createTempFile(Context context) throws IOException {
    File temp = File.createTempFile(createFileName(), "." + TEMP_SUFFIX, context.getCacheDir());
    Log.d("--create temp file :%s", temp.getAbsolutePath());
    return temp;
  }

  //TODO when should call this method
  @SuppressWarnings("RxLeakedSubscription") public static void cleanup(final Context context) {
    //Observable.create(subscriber -> deleteTempFile(context))
    //    .subscribeOn(Schedulers.io())
    //    .subscribe();
  }

  private static String createFileName() {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    return TEMP_PREFIX + timeStamp + "_";
  }

  private static void deleteTempFile(Context context) {
    File[] tempFiles = context.getCacheDir().listFiles();
    if (tempFiles == null || tempFiles.length == 0) {
      return;
    }
    for (File file : tempFiles) {
      String suffix;
      String fileName = file.getName();
      int dotIndex = fileName.lastIndexOf('.');
      suffix = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);

      if (TextUtils.equals(suffix, TEMP_SUFFIX)) {
        final long now = System.currentTimeMillis();
        final long diff = now - file.lastModified();
        if (diff >= OUTOFDATE) {
          Log.d("**delete file:%s", String.valueOf(file.getAbsoluteFile()));
          file.delete();
        }
      }
    }
  }
}
