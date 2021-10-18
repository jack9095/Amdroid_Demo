package com.kuanquan.videocover.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.kuanquan.videocover.bean.LocalMedia
import java.io.File
import java.lang.ref.WeakReference

object GetFrameBitmap {
    private var mContextWeakReference: WeakReference<Context>? = null
    private var mMedia: LocalMedia? = null
    private var isAspectRatio = false
    private var mTime: Long = 0
    private var mCropWidth = 0
    private var mCropHeight = 0

    fun setParams(
        context: Context?,
        media: LocalMedia?,
        isAspectRatio: Boolean,
        time: Long,
        cropWidth: Int,
        cropHeight: Int,
    ) {
//        setParams(context, media, isAspectRatio, time)
        mCropWidth = cropWidth
        mCropHeight = cropHeight
        mContextWeakReference = WeakReference(context)
        mMedia = media
        this.isAspectRatio = isAspectRatio
        mTime = time
    }
//
//    fun setParams(
//        context: Context?,
//        media: LocalMedia?,
//        isAspectRatio: Boolean,
//        time: Long,
//    ) {
//        mContextWeakReference = WeakReference(context)
//        mMedia = media
//        this.isAspectRatio = isAspectRatio
//        mTime = time
//    }

    fun doInBackground(): Bitmap? {

        val context: Context? = mContextWeakReference?.get()
        if (context != null) {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                val uri =
                    if (SdkVersionUtils.checkedAndroid_Q() && SdkVersionUtils.isContent(mMedia?.path)) {
                        Uri.parse(mMedia?.path)
                    } else {
                        Uri.fromFile(File(mMedia?.path))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                var frame = mediaMetadataRetriever.getFrameAtTime(mTime)
                if (isAspectRatio) {
                    val width = frame!!.width
                    val height = frame!!.height
                    val instagramAspectRatio: Float = getInstagramAspectRatio(width, height)
                    val targetAspectRatio =
                        if (instagramAspectRatio > 0) instagramAspectRatio else width * 1.0f / height
                    val adjustWidth: Int
                    val adjustHeight: Int
                    val resizeScale: Float
                    var cropOffsetX = 0
                    var cropOffsetY = 0
                    if (height > width) {
                        adjustHeight = ScreenUtils.getScreenWidth(context)
                        adjustWidth = (adjustHeight * targetAspectRatio).toInt()
                        if (instagramAspectRatio > 0) {
                            resizeScale = adjustWidth * 1.0f / width
                            cropOffsetY = ((height * resizeScale - adjustHeight) / 2).toInt()
                        } else {
                            resizeScale = adjustHeight * 1.0f / height
                        }
                    } else {
                        adjustWidth = ScreenUtils.getScreenWidth(context)
                        adjustHeight = (adjustWidth / targetAspectRatio).toInt()
                        if (instagramAspectRatio > 0) {
                            resizeScale = adjustHeight * 1.0f / height
                            cropOffsetX = ((width * resizeScale - adjustWidth) / 2).toInt()
                        } else {
                            resizeScale = adjustWidth * 1.0f / width
                        }
                    }
                    frame = Bitmap.createScaledBitmap(
                        frame!!,
                        Math.round(width * resizeScale),
                        Math.round(height * resizeScale), false
                    )
                    frame = Bitmap.createBitmap(
                        frame,
                        cropOffsetX,
                        cropOffsetY,
                        adjustWidth,
                        adjustHeight
                    )
                } else {
                    if (mCropWidth > 0 && mCropHeight > 0) {
                        val scale: Float
                        scale = if (frame!!.width > frame!!.height) {
                            mCropHeight * 1f / frame!!.height
                        } else {
                            mCropWidth * 1f / frame!!.width
                        }
                        frame = Bitmap.createScaledBitmap(
                            frame!!,
                            Math.round(frame!!.width * scale),
                            Math.round(frame!!.height * scale), false
                        )
                    }
                    val cropWidth = Math.min(frame!!.width, frame!!.height)
                    val cropOffsetX = (frame!!.width - cropWidth) / 2
                    val cropOffsetY = (frame!!.height - cropWidth) / 2
                    // TODO 获取滑动到某处的帧图片 width = cropWidth / 2
                    frame =
                        Bitmap.createBitmap(frame!!, cropOffsetX, cropOffsetY, cropWidth, cropWidth)
                }
                mediaMetadataRetriever.release()
                return frame
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getInstagramAspectRatio(width: Int, height: Int): Float {
        var aspectRatio = 0f
        if (height > width * 1.266f) {
            aspectRatio = width / (width * 1.266f)
        } else if (width > height * 1.9f) {
            aspectRatio = height * 1.9f / height
        }
        return aspectRatio
    }

}