package com.kuanquan.toastlibrary.config

import android.content.Context
import android.view.Gravity
import android.view.View

/**
 * 默认样式接口
 */
interface IToastStyle<V : View?> {
    /**
     * 创建 Toast 视图
     */
    fun createView(context: Context?): V

    /**
     * 获取 Toast 显示重心
     */
    fun getGravity(): Int = Gravity.CENTER

    /**
     * 获取 Toast 水平偏移
     */
    fun getXOffset(): Int = 0

    /**
     * 获取 Toast 垂直偏移
     */
    fun getYOffset(): Int = 0

    /**
     * 获取 Toast 水平间距
     */
    fun getHorizontalMargin(): Float = 0F

    /**
     * 获取 Toast 垂直间距
     */
    fun getVerticalMargin(): Float = 0F
}