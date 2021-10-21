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
import kotlin.math.roundToInt

class GetFrameBitmap {
    private var mContextWeakReference: WeakReference<Context>? = null
    private var mLocalMedia: LocalMedia? = null
    private var isAspectRatio = false
    private var mTime: Long = 0
    private var mCropWidth = 0
    private var mCropHeight = 0

    fun setParams(
        context: Context?, media: LocalMedia?, isAspectRatio: Boolean,
        time: Long, cropWidth: Int = 0, cropHeight: Int = 0
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
                        Uri.fromFile(File(mLocalMedia?.path!!))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                var frameMipmap = mediaMetadataRetriever.getFrameAtTime(mTime)
                if (isAspectRatio) {
                    val width = frameMipmap?.width ?: 0
                    val height = frameMipmap?.height ?: 0
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
                    frameMipmap = Bitmap.createScaledBitmap(
                        frameMipmap!!, (width * resizeScale).roundToInt(),
                        (height * resizeScale).roundToInt(), false
                    )
                    frameMipmap = Bitmap.createBitmap(
                        frameMipmap, cropOffsetX, cropOffsetY, adjustWidth, adjustHeight)
                } else {
                    if (mCropWidth > 0 && mCropHeight > 0) {
                        val scale: Float = if (frameMipmap!!.width > frameMipmap.height) {
                            mCropHeight * 1f / frameMipmap.height
                        } else {
                            mCropWidth * 1f / frameMipmap.width
                        }
                        frameMipmap = Bitmap.createScaledBitmap(
                            frameMipmap, (frameMipmap.width * scale).roundToInt(),
                            (frameMipmap.height * scale).roundToInt(), false)
                    } else {
                        val cropWidth = frameMipmap!!.width.coerceAtMost(frameMipmap.height)
                        val cropOffsetX = (frameMipmap.width - cropWidth) / 2
                        val cropOffsetY = (frameMipmap.height - cropWidth) / 2
                        // TODO 获取滑动到某处的帧图片
                        frameMipmap =
                            Bitmap.createBitmap(
                                frameMipmap,
                                cropOffsetX,
                                cropOffsetY,
                                cropWidth,
                                cropWidth
                            )
//                        frameMipmap =
//                            Bitmap.createBitmap(
//                                frameMipmap,
//                                0,
//                                0,
//                                frameMipmap.width,
//                                frameMipmap.height
//                            )
                    }
                }
                mediaMetadataRetriever.release()
                return frameMipmap
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    // 生成拖动的图片
    fun zoomSync(): Bitmap? {
        val context: Context? = mContextWeakReference?.get()
        if (context != null) {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                val uri =
                    if (SdkVersionUtils.checkedAndroid_Q() && SdkVersionUtils.isContent(mLocalMedia?.path)) {
                        Uri.parse(mLocalMedia?.path)
                    } else {
                        Uri.fromFile(File(mLocalMedia?.path!!))
                    }
                mediaMetadataRetriever.setDataSource(context, uri)
                var frameMipmap = mediaMetadataRetriever.getFrameAtTime(mTime)
                if (isAspectRatio) {
                    val width = frameMipmap?.width ?: 0
                    val height = frameMipmap?.height ?: 0
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
                    frameMipmap = Bitmap.createScaledBitmap(
                        frameMipmap!!, (width * resizeScale).roundToInt(),
                        (height * resizeScale).roundToInt(), false
                    )
                    frameMipmap = Bitmap.createBitmap(
                        frameMipmap, cropOffsetX, cropOffsetY, adjustWidth, adjustHeight)
                } else {
//                    if (mCropWidth > 0 && mCropHeight > 0) {
//                        val scale: Float = if (frameMipmap!!.width > frameMipmap.height) {
//                            mCropHeight * 1f / frameMipmap.height
//                        } else {
//                            mCropWidth * 1f / frameMipmap.width
//                        }

                    val scaleY: Float = mCropHeight * 1f / frameMipmap!!.height
                    val scaleX: Float = mCropWidth * 1f / frameMipmap.width

                        frameMipmap = Bitmap.createScaledBitmap(
                            frameMipmap, (frameMipmap.width * scaleX).roundToInt(),
                            (frameMipmap.height * scaleY).roundToInt(), false)
//                    } else {
//                        val cropWidth = frameMipmap!!.width.coerceAtMost(frameMipmap.height)
//                        val cropOffsetX = (frameMipmap.width - cropWidth) / 2
//                        val cropOffsetY = (frameMipmap.height - cropWidth) / 2
//                        // TODO 获取滑动到某处的帧图片
//                        frameMipmap =
//                            Bitmap.createBitmap(
//                                frameMipmap,
//                                cropOffsetX,
//                                cropOffsetY,
//                                cropWidth,
//                                cropWidth
//                            )
//                    }
                }
                mediaMetadataRetriever.release()
                return frameMipmap
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    // 最后生成封面的操作
//    fun doInSync(bitmap: Bitmap?, count: CountDownLatch): String? {
    fun doInSync(bitmap: Bitmap?): String? {
        var scanFilePath: String? = null // 保存后扫描到的图片路径
        val context: Context? = mContextWeakReference?.get()
        context ?: return null
        val fileName = System.currentTimeMillis().toString() + ".png"
        val path = context.applicationContext
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(path, "Covers/$fileName")
        var outputStream: OutputStream? = null
        try {
            file.parentFile!!.mkdirs()
            outputStream = context.applicationContext.contentResolver
                .openOutputStream(Uri.fromFile(file))
            // 压缩图片 80 是压缩率，表示压缩20%; 如果不压缩是100，表示压缩率为0，把图片压缩到 outputStream 所在的文件夹下
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            bitmap?.recycle()
            MediaScannerConnection.scanFile(
                context.applicationContext, arrayOf(file.toString()), null
            ) { path1: String?, _: Uri? ->
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