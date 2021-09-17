package com.kuanquan.doyincover.utils

import com.facebook.drawee.view.SimpleDraweeView

fun loadImage(imageView: SimpleDraweeView, url: String?, width: Int, height: Int) {
  if (url == null) return
  val buildUrl = StringBuilder(url).apply {
   if (!url.contains("?imageMogr2")) {
     append("?imageMogr2")
     append("/strip")
     append("/gravity/center")
     append("/thumbnail")
     append("/${width}x$height")
     append("/format/webp")
   }
  }
  imageView.setImageURI(buildUrl.toString())
}