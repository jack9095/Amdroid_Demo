package com.mouble.baselibrary.util

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.mouble.baselibrary.R
import java.lang.reflect.Method


class DensityUtil {

    /**
     * 根据手机的分辨率将dp的单位转成px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Float {
        val scale: Float = context.resources.displayMetrics.density
        return dpValue * scale
    }

    /**
     * 根据手机的分辨率将px(像素)的单位转成dp
     */
    fun px2dip(context: Context, pxValue: Float): Float {
        val scale: Float = context.resources.displayMetrics.density
        return pxValue / scale
    }

    /**
     * 得到设备屏幕的宽度
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 得到设备屏幕的高度
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 不管横屏还是竖屏，获取屏幕的真实宽
     *
     * @param context
     * @return
     */
    fun getPortraitScreenWidth(context: Context): Int {
        val w = getScreenWidth(context)
        val h = getScreenHeight(context)
        return Math.min(w, h)
    }

    /**
     * 不管横屏还是竖屏，获取屏幕的真实高
     *
     * @param context
     * @return
     */
    fun getPortraitScreenHeight(context: Context): Int {
        val w = getScreenWidth(context)
        val h = getScreenHeight(context)
        return Math.max(w, h)
    }

    /**
     * 得到设备的密度
     */
    fun getScreenDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 把密度转换为像素
     */
    fun dip2pX(context: Context, px: Float): Int {
        val scale = getScreenDensity(context)
        return (px * scale + 0.5f).toInt()
    }

    /**
     * sp转成px
     *
     * @param ctx
     * @param spValue
     * @return
     */
    fun sp2px(ctx: Context, spValue: Float): Int {
        val scaledDensity = ctx.resources.displayMetrics.scaledDensity
        return (spValue * scaledDensity + 0.5f).toInt()
    }

    /**
     * 设置全屏显示
     *
     * @param context
     */
    fun setFullScreen(context: Activity) {
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        val myWindow: Window = context.window
        // 设置为全屏
        myWindow.setFlags(flag, flag)
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val res: Resources = context.resources
        val resourceId: Int = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    fun getHeightDpi(context: Context): Int {
        var dpi = 0
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method: Method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            dpi = displayMetrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dpi
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    fun getWidthDpi(context: Context): Int {
        var dpi = 0
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method: Method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            dpi = displayMetrics.widthPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dpi
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    fun getNavigationHeight(context: Context): Int {
        return getHeightDpi(context) - getScreenHeight(context)
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    fun getTitleHeight(activity: Activity): Int {
        return activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT)
            .top
    }

    /**
     * 获取控件到父控件的上边距
     *
     * @param myView
     * @return
     */
    fun getRelativeTop(myView: View): Int {
        return if (myView.id === R.id.content) myView.top else myView.top + getRelativeTop(
            myView.parent as View
        )
    }

    /**
     * 获取控件到父控件的左边距
     *
     * @param myView
     * @return
     */
    fun getRelativeLeft(myView: View): Int {
        return if (myView.id === R.id.content) myView.left else myView.left + getRelativeLeft(
            myView.parent as View
        )
    }

    /**
     * 设置屏幕为横屏
     *
     * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
     *
     * 不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
     *
     * 设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
     *
     * 设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
     *
     * @param activity activity
     */
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 判断是否横屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources
            .configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 判断是否竖屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isPortrait(context: Context): Boolean {
        return context.resources
            .configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    fun captureWithStatusBar(activity: Activity, withStatusBar: Boolean): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val ret: Bitmap
        ret = if (withStatusBar) {
            //包含状态栏
            Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
        } else { //不包含状态栏
            val statusBarHeight = getStatusBarHeight(activity)
            Bitmap.createBitmap(
                bmp,
                0,
                statusBarHeight,
                dm.widthPixels,
                dm.heightPixels - statusBarHeight
            )
        }
        view.destroyDrawingCache()
        return ret
    }

}