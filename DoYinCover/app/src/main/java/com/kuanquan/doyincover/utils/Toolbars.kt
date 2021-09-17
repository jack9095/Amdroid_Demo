package com.kuanquan.doyincover.utils

import android.view.View
import androidx.appcompat.widget.Toolbar


fun Toolbar.navigationIcon(icon: Int, callback: (View) -> Unit) {
  setNavigationIcon(icon)
  setNavigationOnClickListener {
    callback(it)
  }
}