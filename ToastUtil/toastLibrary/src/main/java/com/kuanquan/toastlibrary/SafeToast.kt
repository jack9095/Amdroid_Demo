package com.kuanquan.toastlibrary

import android.app.Application
import android.os.Handler
import android.widget.Toast

/**
 * Toast 显示安全处理
 */
class SafeToast(application: Application?) : SystemToast(application) {
    init {

        // 反射 Toast 中的字段
        try {
            // 获取 mTN 字段对象
            val mTNField = Toast::class.java.getDeclaredField("mTN")
            mTNField.isAccessible = true
            val mTN = mTNField[this]

            // 获取 mTN 中的 mHandler 字段对象
            val mHandlerField = mTNField.type.getDeclaredField("mHandler")
            mHandlerField.isAccessible = true
            val mHandler = mHandlerField[mTN] as Handler

            // 偷梁换柱
            mHandlerField[mTN] = SafeHandler(mHandler)
        } catch (e: IllegalAccessException) {
            // Android 9.0 上反射会出现报错
            // Accessing hidden field Landroid/widget/Toast;->mTN:Landroid/widget/Toast$TN;
            // java.lang.NoSuchFieldException: No field mTN in class Landroid/widget/Toast;
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
}