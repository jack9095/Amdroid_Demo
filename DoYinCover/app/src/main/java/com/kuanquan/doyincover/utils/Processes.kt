@file:JvmName("Processes")

package com.kuanquan.doyincover.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import arrow.data.Try
import arrow.data.getOrDefault
import okio.Okio
import java.io.File

private const val DEFAULT_PROCESS_NAME = "N/A"

fun isMainProcess(context: Context): Boolean {
  val mainProcessName = context.applicationInfo.processName.valueOrDefault(context.packageName)
  val processName = getProcessName(context)
  return TextUtils.equals(mainProcessName, processName)
}

fun getProcessName(context: Context): String {
  return Try {
    getProcessNameInternal(context)
  }.getOrDefault { DEFAULT_PROCESS_NAME }
}

private fun getProcessNameInternal(context: Context): String {
  val myPid = android.os.Process.myPid()
  if (myPid <= 0) return DEFAULT_PROCESS_NAME

  val processName: String?
  val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
  val process = activityManager.runningAppProcesses?.find {
    it.pid == myPid
  }
  if (process != null) {
    processName = process.processName
  } else {
    val byteArray = Okio.buffer(Okio.source(File("/proc/$myPid/cmdline"))).readByteArray()
    var len = byteArray.size
    for (i in 0 until len) { // lots of '0' in tail , remove them
      if (byteArray[i].toInt() and 0xFF > 128 || byteArray[i] <= 0) {
        len = i
        break
      }
    }
    processName = String(byteArray, 0, len)
  }
  return processName.valueOrDefault(DEFAULT_PROCESS_NAME)
}