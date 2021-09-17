package com.kuanquan.doyincover.publisher.utils

import android.content.Context
import com.kuanquan.doyincover.R

object StringUtil {
    fun getChineseName(name: String, context: Context): String {
        return when (name) {
            "none" -> context.getString(R.string.string_filter_none)
            "camomile" -> context.getString(R.string.string_filter_camomile)
            "cold" -> context.getString(R.string.string_filter_cold)
            "habana" -> context.getString(R.string.string_filter_habana)
            "lzp" -> context.getString(R.string.string_filter_lzp)
            "happy" -> context.getString(R.string.string_filter_happy)
            "elegance" -> context.getString(R.string.string_filter_elegance)
            "harvest" -> context.getString(R.string.string_filter_harvest)
            "miss" -> context.getString(R.string.string_filter_miss)
            "red" -> context.getString(R.string.string_filter_red)
            "tasty" -> context.getString(R.string.string_filter_tasty)
            "turkish" -> context.getString(R.string.string_filter_turkish)
            "waltz" -> context.getString(R.string.string_filter_waltz)
            "west" -> context.getString(R.string.string_filter_west)
            "print" -> context.getString(R.string.string_filter_print)
            "railway" -> context.getString(R.string.string_filter_railway)
            "retro" -> context.getString(R.string.string_filter_retro)
            "1960s" -> context.getString(R.string.string_filter_1960s)
            "gray" -> context.getString(R.string.string_filter_gray)
            else -> ""
        }
    }
}