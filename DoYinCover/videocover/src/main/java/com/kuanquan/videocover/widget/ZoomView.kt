package com.kuanquan.videocover.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatImageView
import com.kuanquan.videocover.util.ScreenUtils

/**
 * 手指触摸放大的 View
 */
class ZoomView(context: Context) : AppCompatImageView(context) {

    private var mPaint: Paint? = null
    private var mBitmap: Bitmap? = null

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = -0x222223
            strokeWidth = ScreenUtils.dip2px(getContext(), 2F).toFloat()
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
//        scaleType = ScaleType.CENTER_CROP
//        setImageBitmap(bitmap)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, mPaint) }

        val right: Float = measuredWidth - ScreenUtils.dip2px(context, 1F).toFloat()
        mPaint?.let {
            canvas?.drawRect(
                ScreenUtils.dip2px(context, 1F).toFloat(),
                ScreenUtils.dip2px(context, 1F).toFloat(),
                right,
                measuredHeight - ScreenUtils.dip2px(context, 1F).toFloat(),
                it
            )
        }
    }
}