package com.kuanquan.videocover.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.kuanquan.videocover.bean.LocalMedia
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch

object GetFrameBitmap {
    private var mContextWeakReference: WeakReference<Context>? = null
    private var mLocalMedia: LocalMedia? = null
    private var isAspectRatio = false
    private var mTime: Long = 0
    private var mCropWidth = 0
    private var mCropHeight = 0

    fun setParams(
        context: Context?,
        media: LocalMedia?,
        isAspectRatio: Boolean,
        time: Long,
        cropWidth: Int = 0,
        cropHeight: Int = 0,
    ) {
        mCropWidth = cropWidth
        mCropHeight = cropHeight
        mContextWeakReference = WeakReference(context)
        mLocalMedia = media
        this.isAspectRatio = isAspectRatio
        mTime = time
    }

    fun doInBackground(): Bitmap? {

        val context: Context? = mContextWeakReference?.get()
        if (context != null) {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                val uri =
                    if (SdkVersionUtils.checkedAndroid_Q() && SdkVersionUtils.isContent(mLocalMedia?.path)) {
                        Uri.parse(mLocalMedia?.path)
                    } else {
                        Uri.fromFile(File(mLocalMedia?.path))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                var frame = mediaMetadataRetriever.getFrameAtTime(mTime)
                if (isAspectRatio) {
                    val width = frame!!.width
                    val height = frame!!.height
                    val instagramAspectRatio: Float = SdkVersionUtils.getInstagramAspectRatio(width, height)
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
                        val scale: Float = if (frame!!.width > frame.height) {
                            mCropHeight * 1f / frame.height
                        } else {
                            mCropWidth * 1f / frame.width
                        }
                        frame = Bitmap.createScaledBitmap(
                            frame,
                            Math.round(frame.width * scale),
                            Math.round(frame.height * scale), false
                        )
                    }
                    val cropWidth = Math.min(frame!!.width, frame.height)
                    val cropOffsetX = (frame.width - cropWidth) / 2
                    val cropOffsetY = (frame.height - cropWidth) / 2
                    // TODO 获取滑动到某处的帧图片 width = cropWidth / 2
                    frame =
                        Bitmap.createBitmap(frame, cropOffsetX, cropOffsetY, cropWidth, cropWidth)
                }
                mediaMetadataRetriever.release()
                return frame
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun doInSync(bitmap: Bitmap?, count: CountDownLatch): String? {
        var scanFilePath: String? = null // 保存后扫描到的图片路径
        val context: Context? = mContextWeakReference?.get()
        context ?: return null
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val path = context.applicationContext
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(path, "Covers/$fileName")
        var outputStream: OutputStream? = null
        try {
            file.parentFile.mkdirs()
            outputStream = context.applicationContext.contentResolver
                .openOutputStream(Uri.fromFile(file))
            // 压缩图片 80 是压缩率，表示压缩20%; 如果不压缩是100，表示压缩率为0，把图片压缩到 outputStream 所在的文件夹下
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            bitmap?.recycle()
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf(file.toString()), null
            ) { path1: String?, uri: Uri? ->
                scanFilePath = path1
                EventBus.getDefault().post(path1)
                mLocalMedia?.coverPath = path1
//                count.countDown()
            }
            return scanFilePath
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            SdkVersionUtils.close(outputStream)
        }
        return null
    }

}