package com.kuanquan.toastlibrary.style

import android.content.Context
import android.view.View
import com.kuanquan.toastlibrary.config.IToastStyle

/**
 * Toast 位置包装样式实现
 */
class LocationToastStyle: IToastStyle<View?> {

    var mStyle: IToastStyle<*>? = null

    var mGravity = 0
    var mXOffset = 0
    var mYOffset = 0
    var mHorizontalMargin = 0f
    var mVerticalMargin = 0f

    constructor(style: IToastStyle<*>?, gravity: Int) : this(style, gravity, 0, 0, 0f, 0f)

    constructor(
        style: IToastStyle<*>?,
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        mStyle = style
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun createView(context: Context?): View? {
        return mStyle?.createView(context)
    }

    override fun getGravity(): Int {
        return mGravity
    }

    override fun getXOffset(): Int {
        return mXOffset
    }

    override fun getYOffset(): Int {
        return mYOffset
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }
}