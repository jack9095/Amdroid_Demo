package com.kuanquan.doyincover.utils.extension

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showLongToast(msg: String) {
    this.context?.let {
        Toast.makeText(it, msg, Toast.LENGTH_LONG).show()
    }
}

fun Fragment.showShortToast(msg: String) {
    this.context?.let {
        Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
    }
}