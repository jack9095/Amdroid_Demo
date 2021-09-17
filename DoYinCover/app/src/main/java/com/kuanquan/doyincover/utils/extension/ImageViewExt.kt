package com.kuanquan.doyincover.utils.extension

import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.support.annotation.RawRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kuanquan.doyincover.utils.dip
import com.kuanquan.doyincover.utils.imageUrl2WebP2

fun ImageView.load(path: String?) {
    if (!path.isNullOrEmpty()) {
        Glide.with(context).load(path).into(this)
    }
}


fun ImageView.loadWithSize(path: String?, requestSize: (Bitmap, Int, Int) -> Unit) {
    if (!path.isNullOrEmpty()) {
        Glide.with(context).asBitmap().load(path).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                resource.let {
                    requestSize.invoke(resource, resource.width, resource.height)
                }
            }
        })
    }
}


fun ImageView.load(path: String?, width: Int, height: Int, scale: Float = 1f, useDP: Boolean = true) {
    val realUrl: String?

    if (!path.isNullOrEmpty()) {
        var w: Int
        var h: Int
        if (useDP) {
            w = context.dip(width)
            h = context.dip(height)
        } else {
            w = width
            h = height
        }

        if (scale < 1) {
            w = (width * scale).toInt()
            h = (height * scale).toInt()
            realUrl = imageUrl2WebP2(path ?: "", w, h)
        } else {
            realUrl = path
        }

        Glide.with(context).load(realUrl).override(w, h).into(this)
    }
}

fun ImageView.load(@RawRes @DrawableRes resourceId: Int, width: Int, height: Int, useDP: Boolean = true) {
    if (resourceId != 0) {
        val w: Int
        val h: Int
        if (useDP) {
            w = context.dip(width)
            h = context.dip(height)
        } else {
            w = width
            h = height
        }
        Glide.with(context).load(resourceId)
            .override(w, h).into(this)
    }
}

fun ImageView.loadAsGif(path: String?, width: Int, height: Int, useDP: Boolean = true) {
    if (!path.isNullOrEmpty()) {
        val w: Int
        val h: Int
        if (useDP) {
            w = context.dip(width)
            h = context.dip(height)
        } else {
            w = width
            h = height
        }
        Glide.with(context)
                .asGif()
                .override(w, h)
                .load(path)
                .into(this)
    }
}

/*fun ImageView.loadBlur(url: String?, width: Int, height: Int, widthScale: Float = 0.005f, heightScale: Float = 0.005f, iterations: Int = 5, blurRadius: Int = 1) {
    var blurImageWidth = width
    var blurImageHeight = height
    if (width > 100 || height > 100) {
        blurImageWidth = (width * widthScale).toInt()
        blurImageHeight = (height * heightScale).toInt()
    }
    val previewUrl = imageUrl2WebP2(url ?: "", blurImageWidth, blurImageHeight)

    Glide.with(context)
            .load(previewUrl)
            .apply(RequestOptions.bitmapTransform(SupportRSBlurTransformation(blurRadius,iterations)))
            .into(this)
}*/





