package com.kuanquan.sinaweibopraise

import android.R
import android.content.Context
import android.os.Vibrator
import android.util.TypedValue
import kotlin.math.roundToInt

object ScreenUtil {

    fun getScreenHeight(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 2160
    }

    fun getScreenWidth(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 1080
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    // 震动
    fun vibrate(context: Context, time: Long) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(time)
    }

    fun getActionBarHeight(context: Context?): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context?.theme?.resolveAttribute(R.attr.actionBarSize, tv, true) == true) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return actionBarHeight
    }

    fun dp2px(context: Context?, dp: Float): Int {
        context ?: (dp * 3).toInt()
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context?.resources?.displayMetrics
        )
        return px.roundToInt()
    }
}