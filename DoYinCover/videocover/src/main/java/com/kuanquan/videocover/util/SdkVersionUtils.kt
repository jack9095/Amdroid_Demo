package com.kuanquan.videocover.util

import android.os.Build
import android.text.TextUtils
import java.io.Closeable
import java.io.IOException

object SdkVersionUtils {

    /**
     * 判断是否是Android Q版本
     *
     * @return
     */
    fun checkedAndroid_Q(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * is content://
     *
     * @param url
     * @return
     */
    fun isContent(url: String?): Boolean {
        return if (TextUtils.isEmpty(url)) {
            false
        } else url?.startsWith("content://") ?: false
    }

    fun close(c: Closeable?) {
        if (c != null && c is Closeable) { // java.lang.IncompatibleClassChangeError: interface not implemented
            try {
                c.close()
            } catch (e: IOException) {
                // silence
            }
        }
    }

    fun getInstagramAspectRatio(width: Int, height: Int): Float {
        var aspectRatio = 0f
        if (height > width * 1.266f) {
            aspectRatio = width / (width * 1.266f)
        } else if (width > height * 1.9f) {
            aspectRatio = height * 1.9f / height
        }
        return aspectRatio
    }
}