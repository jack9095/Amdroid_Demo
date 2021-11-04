package com.kuanquan.toastlibrary.style

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.kuanquan.toastlibrary.config.IToastStyle

/**
 * 默认黑色样式实现
 */
open class BlackToastStyle : IToastStyle<TextView?> {
    override fun createView(context: Context?): TextView? {
        context ?: return null
        val textView = TextView(context)
        textView.id = android.R.id.message
        textView.gravity = getTextGravity(context)
        textView.setTextColor(getTextColor(context))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(context))
        val horizontalPadding = getHorizontalPadding(context)
        val verticalPadding = getVerticalPadding(context)

        // 适配布局反方向特性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        } else {
            textView.setPadding(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
            )
        }
        textView.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        val background = getBackgroundDrawable(context)
        // 设置背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.background = background
        } else {
            textView.setBackgroundDrawable(background)
        }

        // 设置 Z 轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.z = getTranslationZ(context)
        }

        // 设置最大显示行数
        textView.maxLines = getMaxLines(context)
        return textView
    }

    open fun getTextGravity(context: Context?): Int {
        return Gravity.CENTER
    }

    open fun getTextColor(context: Context?): Int {
        return -0x11000001
    }

    open fun getTextSize(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            14f,
            context.resources.displayMetrics
        )
    }

    open fun getHorizontalPadding(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            context.resources.displayMetrics
        )
            .toInt()
    }

    open fun getVerticalPadding(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            context.resources.displayMetrics
        )
            .toInt()
    }

    open fun getBackgroundDrawable(context: Context): Drawable {
        val drawable = GradientDrawable()
        // 设置颜色
        drawable.setColor(-0x78000000)
        // 设置圆角
        drawable.cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            10f,
            context.resources.displayMetrics
        )
        return drawable
    }

    open fun getTranslationZ(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics
        )
    }

    open fun getMaxLines(context: Context?): Int {
        return 5
    }

}