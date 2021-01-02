package com.kuanquan.botttomsheetdialog

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import java.text.DecimalFormat


var View.visible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.invisible: Boolean
    get() {
        return visibility == View.INVISIBLE
    }
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

var View.gone: Boolean
    get() {
        return visibility == View.GONE
    }
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

fun <T : View> T.setVisible(visible: Boolean, action: (T.() -> Unit)? = null): T {
    if (visible) {
        visibility = View.VISIBLE
        action?.invoke(this)
    } else {
        visibility = View.GONE
    }
    return this
}

fun <T : View> T.setInVisible(invisible: Boolean, trueAction: (T.() -> Unit)? = null): T {
    if (invisible) {
        visibility = View.INVISIBLE
        trueAction?.invoke(this)
    } else {
        visibility = View.VISIBLE
    }
    return this
}

fun <T : View> T.setGone(gone: Boolean, trueAction: (T.() -> Unit)? = null): T {
    if (gone) {
        visibility = View.GONE
        trueAction?.invoke(this)
    } else {
        visibility = View.VISIBLE
    }
    return this
}

var View.startMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = value
    }

var View.leftMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = value
    }

var View.topMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
    }

var View.endMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = value
    }

var View.rightMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = value
    }

var View.bottomMargin: Int
    get():Int {
        return (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0
    }
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = value
    }

var View.size: Pair<Int, Int>
    get() {
        return (layoutParams?.width ?: 0) to (layoutParams?.height ?: 0)
    }
    set(value) {
        layoutParams?.width = value.first
        layoutParams?.height = value.second
    }

var View.bottomPadding: Int
    get(): Int {
        return paddingBottom
    }
    set(value) {
        if (value != paddingBottom) {
            setPadding(paddingLeft, paddingTop, paddingRight, value)
        }
    }

var View.topPadding: Int
    get(): Int {
        return paddingTop
    }
    set(value) {
        if (value != paddingTop) {
            setPadding(paddingLeft, value, paddingRight, paddingBottom)
        }
    }

var View.rightPadding: Int
    get(): Int {
        return paddingRight
    }
    set(value) {
        if (value != paddingRight) {
            setPadding(paddingLeft, paddingTop, value, paddingBottom)
        }
    }

var View.leftPadding: Int
    get(): Int {
        return paddingLeft
    }
    set(value) {
        if (value != paddingLeft) {
            setPadding(value, paddingTop, paddingRight, paddingBottom)
        }
    }

var View.startPadding: Int
    get(): Int {
        return paddingStart
    }
    set(value) {
        if (value != paddingStart) {
            setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)
        }
    }

var View.endPadding: Int
    get(): Int {
        return paddingEnd
    }
    set(value) {
        if (value != paddingEnd) {
            setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)
        }
    }

fun Number.format(pattern: String = "00"): String {
    return DecimalFormat(pattern).format(this)
}

@JvmOverloads
fun Int?.nullOr(dft: Int = 0) = this ?: dft

@JvmOverloads
fun Long?.nullOr(dft: Long = 0) = this ?: dft

@JvmOverloads
fun Float?.nullOr(dft: Float = 0F) = this ?: dft

@JvmOverloads
fun Double?.nullOr(dft: Double = 0.toDouble()) = this ?: dft

@JvmOverloads
fun Short?.nullOr(dft: Short = 0) = this ?: dft

@JvmOverloads
fun Byte?.nullOr(dft: Byte = 0) = this ?: dft

fun <T : Number> T?.assignIf(condition: T?.() -> Boolean, value: T): T? {
    return if (condition()) {
        value
    } else {
        this
    }
}

/**
 * 获取屏幕高度
 */
fun getScreenHeight(context: Context?): Int {
    if (context == null) {
        return 2160
    }
    return context.resources.displayMetrics.heightPixels
}

fun dp2px(context: Context?, dp: Float): Int {
    if (context == null) {
        return (dp * 3).toInt()
    }
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
    return Math.round(px)
}
