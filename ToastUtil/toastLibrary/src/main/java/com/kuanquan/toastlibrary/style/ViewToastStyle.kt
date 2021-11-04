package com.kuanquan.toastlibrary.style

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.kuanquan.toastlibrary.config.IToastStyle

/**
 * Toast View 包装样式实现
 */
class ViewToastStyle(private val mLayoutId: Int, private val mStyle: IToastStyle<*>?) : IToastStyle<View?> {

    override fun createView(context: Context?): View? {
        return LayoutInflater.from(context).inflate(mLayoutId, null)
    }

    override fun getGravity(): Int {
        return mStyle?.getGravity() ?: Gravity.CENTER
    }

    override fun getXOffset(): Int {
        return mStyle?.getXOffset() ?: 0
    }

    override fun getYOffset(): Int {
        return mStyle?.getYOffset() ?: 0
    }

    override fun getHorizontalMargin(): Float {
        return mStyle?.getHorizontalMargin() ?: 0F
    }

    override fun getVerticalMargin(): Float {
        return mStyle?.getVerticalMargin() ?: 0F
    }

}