package com.kuanquan.videocover.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.kuanquan.videocover.R
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.util.GetAllFrame
import com.kuanquan.videocover.util.GetFrameBitmap
import com.kuanquan.videocover.util.ScreenUtils
import com.kuanquan.videocover.util.SdkVersionUtils
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream
import java.lang.Runnable
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch

class CoverContainer: FrameLayout {

    private val mImageViews = arrayOfNulls<ImageView>(10) // 把视频几等份的图片集合，这里是 10等份

    private var mImageViewHeight = 0 // 展示图片控件的高度，写死的 60dp

    private var mImageViewWidth = 0 // 展示图片控件的宽度

//    private var mFrameTask: GetAllFrameTask? = null
    private var mMaskView: View? = null // 未被选择的图片上面的蒙层View，百分之70 的透明度

    private var mZoomView: ZoomView? = null // 选中图片上面的蒙板View,可以跟着手指滑动

    private var startedTrackingX = 0 // X轴跟踪手指移动的坐标

    private var scrollHorizontalPosition = 0f // 当前实时水平滑动的位置（x轴坐标）

    private var mOnSeekListener: onSeekListener? = null
    private var mLocalMedia // 传进来的数据，包含视频的路径
            : LocalMedia? = null
    private var mChangeTime: Long = 0
//    private var mGetFrameBitmapTask // 解析视频帧图片的任务
//            : GetFrameBitmapTask? = null
    private var mCurrentPercent = 0f // 当前的百分比

    var mLiveData = MutableLiveData<String>()
    private var startClickX = 0 // 点击确认选中图片上面的蒙板View的x轴位置
    private val mainScope = MainScope()


    constructor(context: Context, media: LocalMedia): super(context) {
        mLocalMedia = media
        mImageViewHeight = ScreenUtils.dip2px(getContext(), 60F)
        // 创建展示图片的 View ,并添加到容器中，这里会创建10个出来
        for (i in mImageViews.indices) {
            mImageViews[i] = ImageView(context)
            mImageViews[i]?.scaleType = ImageView.ScaleType.CENTER_CROP
            mImageViews[i]?.setImageResource(R.drawable.picture_image_placeholder)
            addView(mImageViews[i])
        }
        // 创建未被选择的图片上面的蒙层View，百分之70 的透明度
        mMaskView = View(context)
        mMaskView?.setBackgroundColor(0x77FFFFFF)
        addView(mMaskView)
        // 选中图片上面的蒙板View,可以跟着手指滑动
        mZoomView = ZoomView(context)
        addView(mZoomView)
    }

    fun getFrame(context: Context, media: LocalMedia) {
        mainScope.launch {
            val job = async(Dispatchers.IO) {
                GetFrameBitmap.setParams(
                    context,
                    media,
                    false,
                    0,
                    mImageViewHeight,
                    mImageViewHeight)
                GetFrameBitmap.doInBackground()
            }
            val await = job.await()
            await?.let { mZoomView?.setBitmap(it) }
        }
        // 给手指触摸移动的选中view设置显示的图片 一进来mZoomView初始值
//        mGetFrameBitmapTask = GetFrameBitmapTask(
//            context,
//            media,
//            false,
//            0,
//            mImageViewHeight,
//            mImageViewHeight,
//            OnCompleteListenerImpl(mZoomView)
//        )
//        mGetFrameBitmapTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        mainScope.launch {
            val job = async(Dispatchers.IO) {
                GetAllFrame.setParams(
                    context,
                    media,
                    mImageViews.size,
                    0,
                    media.duration,
                    OnSingleBitmapListenerImpl(this@CoverContainer))
                GetFrameBitmap.doInBackground()
            }
            val await = job.await()
        }
//        mFrameTask = GetAllFrameTask(
//            context,
//            media,
//            mImageViews.size,
//            0,
//            media.getDuration() as Int,
//            OnSingleBitmapListenerImpl(this)
//        )
//        mFrameTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        mImageViewWidth = (width - ScreenUtils.dip2px(context, 40F)) / mImageViews.size
        for (imageView in mImageViews) {
            imageView?.measure(
                MeasureSpec.makeMeasureSpec(mImageViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mImageViewHeight, MeasureSpec.EXACTLY)
            )
        }
        val maskViewWidth: Int = width - ScreenUtils.dip2px(context, 40F) + mImageViews.size - 1
        mMaskView?.measure(
            MeasureSpec.makeMeasureSpec(maskViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(mImageViewHeight, MeasureSpec.EXACTLY)
        )
        mZoomView?.measure(
            MeasureSpec.makeMeasureSpec(mImageViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(mImageViewHeight, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewTop: Int = (measuredHeight - mImageViewHeight) / 2
        var viewLeft: Int

        // 布局，使控件距离左右各20dp
        for (i in mImageViews.indices) {
            viewLeft = i * (mImageViewWidth + 1) + ScreenUtils.dip2px(context, 20F)
            mImageViews.get(i)?.layout(
                viewLeft,
                viewTop,
                viewLeft + (mImageViews[i]?.measuredWidth ?: 0),
                viewTop + (mImageViews[i]?.measuredHeight ?: 0)
            )
        }
        viewLeft = ScreenUtils.dip2px(context, 20F)
        mMaskView?.layout(
            viewLeft,
            viewTop,
            viewLeft + mMaskView!!.measuredWidth,
            viewTop + mMaskView!!.measuredHeight
        )
        mZoomView?.layout(
            viewLeft,
            viewTop,
            viewLeft + mZoomView!!.measuredWidth,
            viewTop + mZoomView!!.measuredHeight
        )
    }

    var dxMove = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rect = Rect()
        mMaskView?.getHitRect(rect)

        // 限制手指滑动范围的，滑动不再封面图控件上就不响应事件
//        if (!rect.contains((int) (event.getX()), (int) (event.getY()))) {
//            return super.onTouchEvent(event);
//        }
            mOnSeekListener?.onSeekEnd()
        if (event.action == MotionEvent.ACTION_DOWN) {
            startedTrackingX = event.x.toInt()
            startClickX = event.x.toInt()
            setScrollHorizontalPosition(
                (startClickX - ScreenUtils.dip2px(context, 20F) - mZoomView!!.measuredWidth / 2).toFloat()
            )
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            dxMove = (event.x - startedTrackingX)
            moveByX(dxMove)
            startedTrackingX = event.x.toInt()
                mOnSeekListener?.onSeekEnd()
        } else if (event.action == MotionEvent.ACTION_CANCEL) {
                mOnSeekListener?.onSeekEnd()
        } else if (event.action == MotionEvent.ACTION_UP) {
                mOnSeekListener?.onSeek(mCurrentPercent, true)
            postDelayed({ moveByXX(dxMove) }, 100)
        }
        return true
    }

    fun moveByX(dx: Float) {
        setScrollHorizontalPosition(scrollHorizontalPosition + dx)
    }

    fun moveByXX(dx: Float) {
        setScrollHorizontalPositionX(scrollHorizontalPosition + dx)
    }

    /**
     * 设置 X 轴上滑动的坐标
     * @param value x轴坐标值
     */
    fun setScrollHorizontalPositionX(value: Float) {
        Log.e("当前滑动的x轴位置", "x -> $scrollHorizontalPosition")
        Log.e("当前滑动的x轴百分比", "mCurrentPercent -> " + mCurrentPercent * 100)
            mOnSeekListener?.onSeek(mCurrentPercent, true)
        if (mCurrentPercent * 100 <= 10) {
            getZoomViewBitmap()
            return
        } else if (mCurrentPercent * 100 >= 95) {
            getZoomViewBitmap()
            return
        }
        if (SystemClock.uptimeMillis() - mChangeTime > 200) {
            mChangeTime = SystemClock.uptimeMillis()
            getZoomViewBitmap()
        }
    }

    /**
     * 设置 X 轴上滑动的坐标
     * @param value x轴坐标值
     */
    fun setScrollHorizontalPosition(value: Float) {
        val oldHorizontalPosition: Float = scrollHorizontalPosition
        scrollHorizontalPosition = Math.min(
            Math.max(0f, value), (mMaskView!!.measuredWidth - mZoomView!!.measuredWidth).toFloat()
        )
        if (oldHorizontalPosition == scrollHorizontalPosition) {
            return
        }
        mZoomView?.translationX = scrollHorizontalPosition
        mCurrentPercent =
            scrollHorizontalPosition / (mMaskView!!.measuredWidth - mZoomView!!.measuredWidth)
        Log.e("当前滑动的x轴位置", "x -> $scrollHorizontalPosition")
        Log.e("当前滑动的x轴百分比", "mCurrentPercent -> " + mCurrentPercent * 100)
            mOnSeekListener?.onSeek(mCurrentPercent, true)
        if (mCurrentPercent * 100 <= 10) {
            getZoomViewBitmap()
            return
        } else if (mCurrentPercent * 100 >= 95) {
            getZoomViewBitmap()
            return
        }
        if (SystemClock.uptimeMillis() - mChangeTime > 200) {
            mChangeTime = SystemClock.uptimeMillis()
            getZoomViewBitmap()
        }
    }

    private fun getZoomViewBitmap() {
        // 返回一个数字四舍五入后最接近的整数，获取到当前视屏点的毫秒值
        val time = Math.round(mLocalMedia!!.duration * mCurrentPercent * 1000).toLong()

        // TODO 给手指触摸移动的选中view设置显示的图片 手指拖拽 mZoomView 移动的值 bitmap
        mainScope.launch {
            val job = async(Dispatchers.IO) {
                GetFrameBitmap.setParams(
                    context,
                    mLocalMedia,
                    false,
                    time,
                    mImageViewHeight,
                    mImageViewHeight)
                GetFrameBitmap.doInBackground()
            }
            val await = job.await()
            await?.let { mZoomView?.setBitmap(it) }
        }
//        mGetFrameBitmapTask = GetFrameBitmapTask(
//            context,
//            mLocalMedia,
//            false,
//            time,
//            mImageViewHeight,
//            mImageViewHeight,
//            OnCompleteListenerImpl(mZoomView)
//        )
//        mGetFrameBitmapTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun cropCover(count: CountDownLatch) {
        val time: Long
        time = if (mCurrentPercent > 0) {
            Math.round(mLocalMedia!!.duration * mCurrentPercent * 1000).toLong()
        } else {
            -1
        }

        mainScope.launch {
            val job = async(Dispatchers.IO) {
                GetFrameBitmap.setParams(
                    context,
                    mLocalMedia,
                    false,
                    time,
                    mImageViewHeight,
                    mImageViewHeight)

                GetFrameBitmap.doInSync(GetFrameBitmap.doInBackground(), count)
            }
            val scanFilePath = job.await()
            // TODO finish 当前页面
            mLiveData.setValue(scanFilePath)
        }
//        GetFrameBitmapTask(context, mLocalMedia, false, time) label@
//        { bitmap ->
//            PictureThreadUtils.executeByIo(object : SimpleTask<File?>() {
//                fun doInBackground(): File? {
//                    val fileName = System.currentTimeMillis().toString() + ".jpg"
//                    val path = context.applicationContext
//                        .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                    val file = File(path, "Covers/$fileName")
//                    var outputStream: OutputStream? = null
//                    try {
//                        file.parentFile.mkdirs()
//                        outputStream = context.applicationContext.contentResolver
//                            .openOutputStream(Uri.fromFile(file))
//                        // 压缩图片 80 是压缩率，表示压缩20%; 如果不压缩是100，表示压缩率为0，把图片压缩到 outputStream 所在的文件夹下
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
//                        bitmap.recycle()
//                        MediaScannerConnection.scanFile(
//                            context.applicationContext,
//                            arrayOf(file.toString()),
//                            null
//                        ) { path1: String?, uri: Uri? ->
//                            scanFilePath = path1
//                            EventBus.getDefault().post(path1)
//                            mLocalMedia.coverPath = path1
//                            count.countDown()
//                        }
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    } finally {
//                        SdkVersionUtils.close(outputStream)
//                    }
//                    return@label null
//                }
//
//                fun onSuccess(result: File?) {
//                    // TODO finish 当前页面
//                    mLiveData.setValue(scanFilePath)
//                }
//            })
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun onDestroy() {
//        if (mFrameTask != null) {
//            mFrameTask.setStop(true)
//            mFrameTask.cancel(true)
//            mFrameTask = null
//        }
//        if (mGetFrameBitmapTask != null) {
//            mGetFrameBitmapTask.cancel(true)
//            mGetFrameBitmapTask = null
//        }
    }

    class OnSingleBitmapListenerImpl(coverContainer: CoverContainer) :
        GetAllFrame.OnSingleBitmapListener {
        private val mContainerWeakReference: WeakReference<CoverContainer> = WeakReference(coverContainer)
        private var index = 0
       override fun onSingleBitmapComplete(bitmap: Bitmap?) {
            val container = mContainerWeakReference.get()
            if (container != null) {
                container.post(RunnableImpl(container.mImageViews.get(index), bitmap))
                index++
            }
        }

        inner class RunnableImpl(imageView: ImageView?, bitmap: Bitmap?) : Runnable {
            private val mViewWeakReference: WeakReference<ImageView> = WeakReference(imageView)
            private val mBitmap: Bitmap? = bitmap
            override fun run() {
                val imageView = mViewWeakReference.get()
                imageView?.setImageBitmap(mBitmap)
            }
        }
    }

//    class OnCompleteListenerImpl(view: ZoomView) : GetFrameBitmapTask.OnCompleteListener {
//        private val mViewWeakReference: WeakReference<ZoomView>
//        fun onGetBitmapComplete(bitmap: Bitmap?) {
//            val view = mViewWeakReference.get()
//            view?.setBitmap(bitmap!!)
//        }
//
//        init {
//            mViewWeakReference = WeakReference(view)
//        }
//    }

    fun setOnSeekListener(onSeekListener: onSeekListener) {
        mOnSeekListener = onSeekListener
    }

    interface onSeekListener {
        /**
         *
         * @param percent 当前x轴滑动的百分比
         * @param isStart true 设置当前的seekTo并同时播放，false 只设置当前的seekTo
         */
        fun onSeek(percent: Float, isStart: Boolean)
        fun onSeekEnd()
    }
}