package com.kuanquan.videocover.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kuanquan.videocover.R

class InstagramTitleBar: FrameLayout {

    private var mLeftView: ImageView? = null
    private var mCenterView: TextView? = null
    private var mRightView: TextView? = null
    private var mClickListener: OnTitleBarItemOnClickListener? = null

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){}

    constructor(context: Context): super(context) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.black))
        mLeftView = ImageView(context).apply {
            setImageResource(R.drawable.discover_return)
            setPadding(
                ScreenUtils.dip2px(context, 15F),
                0,
                ScreenUtils.dip2px(context, 15F),
                0
            )
            colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(getContext(), R.color.white),
                PorterDuff.Mode.MULTIPLY
            )
            setOnClickListener { v: View? ->
                if (mClickListener != null) {
                    mClickListener?.onLeftViewClick()
                }
            }
            addView(this)
        }

        mCenterView = TextView(context).apply {
            setPadding(
                ScreenUtils.dip2px(context, 10F),
                0,
                ScreenUtils.dip2px(context, 10F),
                0
            )
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 18F
            gravity = Gravity.CENTER
            text = context.getString(R.string.cover)
            addView(this)
        }

        mRightView = TextView(context).apply {
            height = ScreenUtils.dip2px(context, 30F)
            width = ScreenUtils.dip2px(context, 70F)
            setPadding(
                ScreenUtils.dip2px(context, 14F),
                ScreenUtils.dip2px(context, 5F),
                ScreenUtils.dip2px(context, 14F),
                ScreenUtils.dip2px(context, 5F)
            )
            setTextColor(ContextCompat.getColor(context, R.color.white))
            textSize = 14F
            text = context.getString(R.string.next)
            gravity = Gravity.CENTER
            background = CommonShapeBuilder()
                .setColor(ContextCompat.getColor(context, R.color.color_ff4338))
                .setCornerRadius(pxValue = ScreenUtils.dip2px(context, 15F).toFloat())
                .build()
            setOnClickListener {
                if (mClickListener != null) {
                    mClickListener?.onRightViewClick()
                }
            }
            addView(this)
        }
    }

    fun setRightViewText(text: String?) {
        mRightView?.text = text
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = ScreenUtils.dip2px(context, 48F)
        mLeftView?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
        if (mCenterView?.visibility == VISIBLE) {
            mCenterView?.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }
        val rightHeight = ScreenUtils.dip2px(context, 30F)
        mRightView?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(rightHeight, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var viewTop = (measuredHeight - (mLeftView?.measuredHeight ?: 0)) / 2
        var viewLeft = 0
        mLeftView?.layout(
            viewLeft,
            viewTop,
            viewLeft + (mLeftView?.measuredWidth ?: 0),
            viewTop + (mLeftView?.measuredHeight ?: 0)
        )
        if (mCenterView?.visibility == VISIBLE) {
            viewTop = (measuredHeight - (mCenterView?.measuredHeight ?: 0)) / 2
            viewLeft = (measuredWidth - (mCenterView?.measuredWidth ?: 0)) / 2
            mCenterView?.layout(
                viewLeft,
                viewTop,
                viewLeft + (mCenterView?.measuredWidth ?: 0),
                viewTop + (mCenterView?.measuredHeight ?: 0)
            )
        }
        viewTop = (measuredHeight - (mRightView?.measuredHeight ?: 0)) / 2
        val rightMargin = ScreenUtils.dip2px(context, 16F)
        viewLeft = measuredWidth - (mRightView?.measuredWidth ?: 0) - rightMargin
        mRightView?.layout(
            viewLeft,
            viewTop,
            viewLeft + (mRightView?.measuredWidth ?: 0),
            viewTop + (mRightView?.measuredHeight ?: 0)
        )
    }

    fun setClickListener(clickListener: OnTitleBarItemOnClickListener?) {
        mClickListener = clickListener
    }

    interface OnTitleBarItemOnClickListener {
        fun onLeftViewClick()
        fun onRightViewClick()
    }

}