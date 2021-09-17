package com.kuanquan.doyincover.utils.extension

import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.BasePostprocessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.kuanquan.doyincover.utils.dip
import com.kuanquan.doyincover.utils.imageUrl2WebP2
import com.kuanquan.doyincover.utils.showUrlBlur


fun SimpleDraweeView.loadBlur(url: String?, width: Int, height: Int, widthScale: Float = 0.01f, heightScale: Float = 0.01f, iterations: Int = 1, blurRadius: Int = 1) {
    var blurImageWidth = width
    var blurImageHeight = height
    if (width > 100 || height > 100) {
        blurImageWidth = (width * widthScale).toInt()
        blurImageHeight = (height * heightScale).toInt()
    }
    val previewUrl = imageUrl2WebP2(url ?: "", blurImageWidth, blurImageHeight)
    showUrlBlur(this, previewUrl, iterations, blurRadius)
}


fun SimpleDraweeView.loadMatchScreenWidth(path: String?, width: Int, height: Int, scale: Float) {
    val realUrl: String?

    if (!path.isNullOrEmpty()) {
        realUrl = if (scale < 1) {
            val w = (width * scale).toInt()
            val h = (height * scale).toInt()
            imageUrl2WebP2(path ?: "", w, h)
        } else {
            path
        }

        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(controller)
                .setControllerListener(object : ControllerListener<ImageInfo> {
                    override fun onFailure(id: String?, throwable: Throwable?) {
                    }

                    override fun onRelease(id: String?) {
                    }

                    override fun onSubmit(id: String?, callerContext: Any?) {
                    }

                    override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                    }

                    override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
                    }

                    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                        val screenWidth = context.screenWidth()
                        layoutParams.width = screenWidth
                        layoutParams.height = screenWidth * height / width
                        requestLayout()
                    }
                })
                .setUri(Uri.parse(realUrl))
                .build()


        setController(controller)
    }
}

fun SimpleDraweeView.loadExpectSize(path: String?, expectWidth: Int, expectHeight: Int, useDP: Boolean = true) {
    if (!path.isNullOrEmpty()) {
        val realUrl: String
        val w: Int
        val h: Int
        if (useDP) {
            w = context.dip(expectWidth)
            h = context.dip(expectHeight)
        } else {
            w = expectWidth
            h = expectHeight
        }
        realUrl = imageUrl2WebP2(path ?: "", w, h)

        setImageURI(realUrl)
    }
}


fun SimpleDraweeView.processBitmap(url: String?, observer: (bitmap: Bitmap?) -> Unit) {

    val processor = object : BasePostprocessor() {
        override fun process(bitmap: Bitmap?) {
            observer(bitmap)
        }
    }

    val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setRotationOptions(RotationOptions.autoRotateAtRenderTime())
            .setPostprocessor(processor)
            .build()
    setImageRequest(imageRequest)

}
