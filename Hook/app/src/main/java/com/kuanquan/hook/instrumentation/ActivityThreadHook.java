package com.kuanquan.hook.instrumentation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Android 8.0系统透明主题适配解决办法
 * https://blog.csdn.net/weixin_38617084/article/details/106479506
 */
public class ActivityThreadHook {

    @SuppressLint("PrivateApi")
    public static void hook() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            try {
                // 反射获取类
                Class<?> mMainThreadClass = Class.forName("android.app.ActivityThread");
                // 获取当前主线程.
                Method getMainThread = mMainThreadClass.getDeclaredMethod("currentActivityThread");
                getMainThread.setAccessible(true);
                Object currentActivityThread = getMainThread.invoke(null);

                // The field contain instrumentation.
                Field mInstrumentationField = mMainThreadClass.getDeclaredField("mInstrumentation");
                mInstrumentationField.setAccessible(true);
                // Hook current instrumentation
                mInstrumentationField.set(currentActivityThread, new ProxyInstrumentation());
            } catch (Throwable ex) {
                Log.e("ActivityThreadHook", "hook instrumentation failed! [" + ex.getMessage() + "]");
            }
        }
    }

}
