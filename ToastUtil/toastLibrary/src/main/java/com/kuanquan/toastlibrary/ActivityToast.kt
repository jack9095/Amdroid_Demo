package com.kuanquan.toastlibrary

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.kuanquan.toastlibrary.config.IToast

/**
 * 自定义 Toast（用于解决关闭通知栏权限之后不能弹吐司的问题和 Android 11 不能自定义吐司样式的问题）
 */
class ActivityToast(activity: Activity?) : IToast {
    /** Toast 实现类  */
    private val mToastImpl: ToastImpl = ToastImpl(activity, this)

    /** Toast 布局  */
    private var mView: View? = null

    /** Toast 消息 View  */
    private var mMessageView: TextView? = null

    /** Toast 显示重心  */
    private var mGravity = 0

    /** Toast 显示时长  */
    private var mDuration = 0

    /** 水平偏移  */
    private var mXOffset = 0

    /** 垂直偏移  */
    private var mYOffset = 0

    /** 水平间距  */
    private var mHorizontalMargin = 0f

    /** 垂直间距  */
    private var mVerticalMargin = 0f

    override fun show() {
        // 替换成 WindowManager 来显示
        mToastImpl.show()
    }

    override fun cancel() {
        // 取消 WindowManager 的显示
        mToastImpl.cancel()
    }

    override fun setText(id: Int) {
        if (mView == null) {
            return
        }
        setText(mView!!.resources.getString(id))
    }

    override fun setText(text: CharSequence?) {
        if (mMessageView == null) {
            return
        }
        mMessageView!!.text = text
    }

    override fun setView(view: View?) {
        mView = view
        if (mView == null) {
            mMessageView = null
            return
        }
        mMessageView = findMessageView(view)
    }

    override fun getView(): View? {
        return mView
    }

    override fun setDuration(duration: Int) {
        mDuration = duration
    }

    override fun getDuration(): Int {
        return mDuration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mGravity = gravity
        mXOffset = xOffset
        mYOffset = yOffset
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

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        mHorizontalMargin = horizontalMargin
        mVerticalMargin = verticalMargin
    }

    override fun getHorizontalMargin(): Float {
        return mHorizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return mVerticalMargin
    }

}