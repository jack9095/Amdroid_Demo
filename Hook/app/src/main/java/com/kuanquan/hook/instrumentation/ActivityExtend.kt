package com.kuanquan.hook.instrumentation

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.util.Log

/*
* Android 8.0 透明主题引起的 crash 问题
 */
fun Activity.isTranslucentOrFloating(): Boolean {
    if (!isActivityAlive(this)) return false
    val isTranslucentOrFloating: Boolean
    try {
        val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window")[null] as? IntArray ?: return false
        val ta = obtainStyledAttributes(styleableRes)
        val m = ActivityInfo::class.java.getMethod("isTranslucentOrFloating", TypedArray::class.java)
        m.isAccessible = true
        isTranslucentOrFloating = (m.invoke(null, ta) as? Boolean) ?: false
        m.isAccessible = false
    } catch (e: Exception) {
        Log.e("isTranslucentOrFloating", e.message.toString())
        return false
    }
    return isTranslucentOrFloating
}

fun Activity.fixOrientation() {
    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O || !isTranslucentOrFloating()) return
    try {
        val field = Activity::class.java.getDeclaredField("mActivityInfo")
        field.isAccessible = true
        val o = field[this] as ActivityInfo
        o.screenOrientation = -1
        field.isAccessible = false
    } catch (e: Exception) {
        Log.e("fixOrientation", e.toString())
    }
}

fun isActivityAlive(activity: Activity?): Boolean {
    return (activity != null && !activity.isFinishing
            && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed))
}