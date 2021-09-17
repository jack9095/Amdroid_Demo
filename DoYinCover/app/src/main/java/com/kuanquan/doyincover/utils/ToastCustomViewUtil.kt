package com.kuanquan.doyincover.utils

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.kuanquan.doyincover.R


/**
 * Toast 工具类
 *
 * Author: ZhaiDongyang
 * Date: 2019/2/21
 */
/**
 * 图片和文字提示-居中显示
 */
fun toastCustomLayout(context: Context, layoutId: Int) {
  val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  val view = inflater.inflate(layoutId, null)
  var toast: Toast? = null
  if (toast == null) {
    toast = Toast(context)
  }
  toast.setGravity(Gravity.CENTER, 0, 0)
  toast.duration = Toast.LENGTH_LONG
  toast.view = view
  toast.show()
}

/**
 * 只有文字提示-居中
 */
fun toastCustomText(context: Context, text: String) {
  val textView = TextView(context)
  textView.text = text
  textView.setBackgroundResource(R.drawable.toast_bg_shape_around)
  textView.setPadding(ScreenUtils.dip2px(context, 10f), ScreenUtils.dip2px(context, 5f),
      ScreenUtils.dip2px(context, 10f), ScreenUtils.dip2px(context, 5f))
  textView.setTextColor(Color.parseColor("#ffffff"))
  textView.gravity = Gravity.CENTER
  var toast: Toast? = null
  if (toast == null) {
    toast = Toast(context)
  }
  toast.setGravity(Gravity.CENTER, 0, 0)
  toast.duration = Toast.LENGTH_SHORT
  toast.view = textView
  toast.show()
}