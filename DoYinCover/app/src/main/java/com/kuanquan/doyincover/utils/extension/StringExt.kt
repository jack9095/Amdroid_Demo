package com.kuanquan.doyincover.utils.extension

fun String?.getRealId(): String? {
    return if (this == null) {
        this
    } else {
        this.split('l').getOrNull(0) ?: this
    }
}
