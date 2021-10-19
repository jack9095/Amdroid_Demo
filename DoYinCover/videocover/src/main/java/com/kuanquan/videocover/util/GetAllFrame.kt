package com.kuanquan.videocover.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.kuanquan.videocover.bean.LocalMedia
import java.io.File
import java.lang.ref.WeakReference

object GetAllFrame {
    private var mContextWeakReference: WeakReference<Context>? = null
    private var mLocalMedia: LocalMedia? = null
    private var mTotalThumbsCount = 0
    private var mStartPosition: Long = 0
    private var mEndPosition: Long = 0
    private var mOnSingleBitmapListener: OnSingleBitmapListener? = null
    private var isStop = false

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
                        Uri.fromFile(File(mLocalMedia?.path))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                val interval = (mEndPosition - mStartPosition) / (mTotalThumbsCount - 1)
                for (i in 0 until mTotalThumbsCount) {
                    if (isStop) {
                        break
                    }
                    val frameTime = mStartPosition + interval * i
                    var bitmap = mediaMetadataRetriever.getFrameAtTime(
                        frameTime * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )
                    if (bitmap == null) {
                        continue
                    }
                    try {
                        val cropWidth = Math.min(bitmap.width, bitmap.height)
                        val cropOffsetX = (bitmap.width - cropWidth) / 2
                        val cropOffsetY = (bitmap.height - cropWidth) / 2
                        bitmap = Bitmap.createBitmap(
                            bitmap,
                            cropOffsetX,
                            cropOffsetY,
                            cropWidth,
                            cropWidth
                        )
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