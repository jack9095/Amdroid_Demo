package com.kuanquan.doyincover.utils

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.checkSelfPermission
import java.io.File

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2019/01/02
 * Description:
 */
private const val CACHE_DIR_NAME = "duck_cache_videos"

fun getVideoCacheDir(context: Context): String {
  val cache: File = if (checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
    File(context.externalCacheDir, CACHE_DIR_NAME)
  } else {
    File(context.cacheDir, CACHE_DIR_NAME)
  }
  if (!cache.exists()) {
    // For some reason the cache directory doesn't exist. Make a best effort to create it.
    cache.mkdirs()
  }
  return cache.absolutePath
}

fun getCacheDir(context: Context): String {
  val cache: File = if (checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
    context.externalCacheDir!!
  } else {
    context.cacheDir
  }
  if (!cache.exists()) {
    // For some reason the cache directory doesn't exist. Make a best effort to create it.
    cache.mkdirs()
  }
  return cache.absolutePath
}
