package com.kuanquan.doyincover.utils

import java.text.DecimalFormat

/**
 * Created by april on 2018/6/27.
 */
object StringFromat {

    val FORMAT_DECIMAL_3 = "#000"

    fun getDecimalString(str: Any, fromat: String): String {
        return DecimalFormat(fromat).format(str)
    }

    fun get3DecimalStr(obj: Int): String {
        return getDecimalString(obj, FORMAT_DECIMAL_3)
    }


}