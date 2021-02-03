package com.kuanquan.commentutillibrary

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorInt
import androidx.annotation.IntDef

/**
 * 创建shape
 * 使用：val drawable = ShapeBuilder()
                    .setCornerRadius(ShapeBuilder.ANGLE_TOP_LEFT, 100)
                    .setCornerRadius(ShapeBuilder.ANGLE_BOTTOM_LEFT, 100)
                    .setGradientColors(intArrayOf(Color.parseColor("#000000"), Color.parseColor("#999999"), R.color.c_999.res2Color()))
                    .setOrientation(GradientDrawable.Orientation.LEFT_RIGHT).build()
                    linearLayout?.background = drawable
 */
class ShapeBuilder {
    companion object {
        const val ANGLE_ALL = 0xff
        const val ANGLE_TOP_LEFT = 0x00
        const val ANGLE_TOP_RIGHT = 0x01
        const val ANGLE_BOTTOM_RIGHT = 0x02
        const val ANGLE_BOTTOM_LEFT = 0x03
    }

    private var mRadii: FloatArray? = null

    private var mColor: Int? = null

    private var mStrokeWidth: Int = 0

    private var mStrokeColor: Int = Color.WHITE

    private var mGradientColors: IntArray? = null

    private var mOrientation: GradientDrawable.Orientation =
        GradientDrawable.Orientation.LEFT_RIGHT

    @Shape
    private var mShape: Int = GradientDrawable.RECTANGLE

    @IntDef(
        GradientDrawable.RECTANGLE, GradientDrawable.OVAL,
        GradientDrawable.LINE, GradientDrawable.RING)
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    annotation class Shape

    @JvmOverloads
    fun setCornerRadius(angle: Int = ANGLE_ALL, pxValue: Float): ShapeBuilder {
        if (mRadii == null) {
            mRadii = FloatArray(8)
        }

        if (angle == ANGLE_ALL) {
            mRadii!!.forEachIndexed { index, _ ->
                mRadii!![index] = pxValue
            }
            return this
        }

        if (angle < ANGLE_TOP_LEFT || angle > ANGLE_BOTTOM_LEFT) {
            return this
        }

        mRadii!![2 * angle] = pxValue
        mRadii!![2 * angle + 1] = pxValue
        return this
    }

    fun setShape(@Shape shape: Int): ShapeBuilder {
        mShape = shape
        return this
    }

    fun setColor(@ColorInt color: Int): ShapeBuilder {
        mColor = color
        return this
    }

    fun setStroke(width: Int, @ColorInt color: Int): ShapeBuilder {
        mStrokeColor = color
        mStrokeWidth = width
        return this
    }

    fun setGradientColors(color: IntArray?): ShapeBuilder {
        mGradientColors = color
        return this
    }

    fun setOrientation(orientation: GradientDrawable.Orientation): ShapeBuilder {
        mOrientation = orientation
        return this
    }

    fun build() = GradientDrawable().apply {
        cornerRadii = mRadii
        this.shape = mShape

        if (mGradientColors?.size == 1) {
            mColor = mGradientColors!![0]
            mGradientColors = null
        }

        mColor?.let { setColor(it) }
        mGradientColors?.let { colors = it }

        orientation = mOrientation
        setStroke(mStrokeWidth, mStrokeColor)
    }
}