package com.kuanquan.toastutil

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

private val sTmpValue: TypedValue by lazy { TypedValue() }

fun <T : Any> T.getString(@StringRes resId: Int): String? = resId.res2String()

fun Int.res2Color(): Int = ContextCompat.getColor(WorkApplication.mApplication!!, this)

fun Int.res2String(): String? = WorkApplication.mApplication?.getString(this)

fun Int.res2String(vararg args: Any): String? = WorkApplication.mApplication?.getString(this, *args)

fun Int.res2Drawable(): Drawable? = ContextCompat.getDrawable(WorkApplication.mApplication!!, this)

fun Int.res2StringArray(): Array<String>? =
        WorkApplication.mApplication?.resources?.getStringArray(this)

fun Int.res2IntArray(): IntArray? = WorkApplication.mApplication?.resources?.getIntArray(this)

fun Int.res2Dimension(): Int = WorkApplication.mApplication?.resources?.getDimensionPixelSize(this) ?: 0

fun Int.resAttr2Color(theme: Resources.Theme?): Int {
    if (theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return 0
    }

    return if (sTmpValue.type == TypedValue.TYPE_ATTRIBUTE) {
        sTmpValue.data.resAttr2Color(theme)
    } else sTmpValue.data
}

fun Int.resAttr2Color(context: Context?): Int {
    return this.resAttr2Color(context?.theme)
}

fun Int.resAttr2FloatValue(context: Context?): Float {
    return this.resAttr2FloatValue(context?.theme)
}

fun Int.resAttr2FloatValue(theme: Resources.Theme?): Float {
    return if (theme?.resolveAttribute(this, sTmpValue, true) != true) {
        0F
    } else sTmpValue.float
}

fun Int.resAttr2String(context: Context?): CharSequence? {
    if (context?.theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return null
    }
    return sTmpValue.string
}

@JvmOverloads
fun Int.resAttr2Int(context: Context?, dftValue: Int = -1): Int {
    if (context?.theme?.resolveAttribute(this, sTmpValue, true) != true) {
        return dftValue
    }
    return sTmpValue.data
}

private fun dp2px(context: Context?, dp: Float): Int {
    if (context == null) {
        return (dp * 3).toInt()
    }
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
    return px.roundToInt()
}

private fun dp2px_f(context: Context?, dp: Float): Float {
    context ?: return dp * 3
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

val Number.dp2px
    get() = dp2px(WorkApplication.mApplication, this.toFloat())

val Number.dp2px_f
    get() = dp2px_f(WorkApplication.mApplication, this.toFloat())







