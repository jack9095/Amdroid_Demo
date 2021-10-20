package com.kuanquan.videocover.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.kuanquan.videocover.bean.LocalMedia
import java.io.File
import java.lang.ref.WeakReference

class GetAllFrame {
    private var mContextWeakReference: WeakReference<Context>? = null
    private var mLocalMedia: LocalMedia? = null // 视频路径等数据
    private var mTotalThumbsCount = 0  // 总共需要几份封面图片数据
    private var mStartPosition: Long = 0 // 解析第一张图片的起点
    private var mEndPosition: Long = 0 // 解析最后一张图片的终点
    private var mOnSingleBitmapListener: OnSingleBitmapListener? = null //给封面控件设置图片显示数据的回掉
    private var isStop = false // 是否停止解析图片数据 true 停止解析

    fun setParams(
        context: Context, media: LocalMedia,
        totalThumbsCount: Int, startPosition: Long, endPosition: Long,
        onSingleBitmapListener: OnSingleBitmapListener?
    ) {
        mContextWeakReference = WeakReference(context)
        mLocalMedia = media
        mTotalThumbsCount = totalThumbsCount
        mStartPosition = startPosition
        mEndPosition = endPosition
        mOnSingleBitmapListener = onSingleBitmapListener
    }

    fun setStop(stop: Boolean) {
        isStop = stop
    }

    fun doInBackground() {
        val context = mContextWeakReference!!.get()
        if (context != null) {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                val uri: Uri =
                    if (SdkVersionUtils.checkedAndroid_Q() && SdkVersionUtils.isContent(mLocalMedia?.path)) {
                        Uri.parse(mLocalMedia?.path)
                    } else {
                        Uri.fromFile(File(mLocalMedia?.path!!))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                val interval = (mEndPosition - mStartPosition) / (mTotalThumbsCount - 1)
                for (i in 0 until mTotalThumbsCount) {
                    if (isStop) {
                        break
                    }
                    val frameTime = mStartPosition + interval * i
                    var bitmap = mediaMetadataRetriever.getFrameAtTime(
                        frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    if (bitmap == null) {
                        continue
                    }
                    try {
                        val cropWidth = bitmap.width.coerceAtMost(bitmap.height)
                        val cropOffsetX = (bitmap.width - cropWidth) / 2
                        val cropOffsetY = (bitmap.height - cropWidth) / 2
                        bitmap = Bitmap.createBitmap(
                            bitmap, cropOffsetX, cropOffsetY, cropWidth, cropWidth)
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }

                    mOnSingleBitmapListener?.onSingleBitmapComplete(bitmap)
                }
                mediaMetadataRetriever.release()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    interface OnSingleBitmapListener {
        fun onSingleBitmapComplete(bitmap: Bitmap?)
    }
}