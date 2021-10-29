package com.kuanquan.videocover.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.*
import com.kuanquan.videocover.R
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.util.GetAllFrame
import com.kuanquan.videocover.util.GetFrameBitmap
import com.kuanquan.videocover.util.ScreenUtils
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import kotlin.math.roundToLong

@SuppressLint("ViewConstructor", "ObjectAnimatorBinding")
class CoverContainer : FrameLayout, LifecycleObserver {

    private val mImageViews = arrayOfNulls<ImageView>(10) // 把视频几等份的图片集合，这里是 10等份
    private var mImageViewHeight = 0 // 展示图片控件的高度，写死的 60dp
    private var mImageViewWidth = 0 // 展示图片控件的宽度
    private var mMaskView: View? = null // 未被选择的图片上面的蒙层View，百分之70 的透明度
    private var mZoomView: ZoomView? = null // 选中图片上面的蒙板View,可以跟着手指滑动
    private var startedTrackingX = 0 // X轴跟踪手指移动的坐标
    private var scrollHorizontalPosition = 0f // 当前实时水平滑动的位置（x轴坐标）
    private var mOnSeekListener: onSeekListener? = null
    private var mLocalMedia: LocalMedia? = null // 传进来的数据，包含视频的路径
    private var mChangeTime: Long = 0  // 记录系统临时时间变量
    private var mCurrentPercent = 0f // 当前的百分比
    var mLiveData = MutableLiveData<String>()
    private var startClickX = 0 // 点击确认选中图片上面的蒙板View的x轴位置
    private val mainScope = MainScope()
    private val mGetAllFrame: GetAllFrame by lazy {
        GetAllFrame()
    }
    private val mGetFrameBitmap: GetFrameBitmap by lazy {
        GetFrameBitmap()
    }

    fun addLifeCycleObserver(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    constructor(context: Context, media: LocalMedia) : super(context) {
        mLocalMedia = media
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        mImageViewHeight = ScreenUtils.dip2px(context, 60F)
        // 创建展示图片的 View ,并添加到容器中，这里会创建10个出来
        for (i in mImageViews.indices) {
            mImageViews[i] = ImageView(context)
            mImageViews[i]?.scaleType = ImageView.ScaleType.CENTER_CROP
            mImageViews[i]?.setImageResource(R.drawable.picture_image_placeholder)
            addView(mImageViews[i])
        }
        // 创建未被选择的图片上面的蒙层View，百分之 77 的透明度
        mMaskView = View(context)
        mMaskView?.setBackgroundColor(0x77FFFFFF)
        addView(mMaskView)
        // 选中图片上面的蒙板View,可以跟着手指滑动
        mZoomView = ZoomView(context)
        addView(mZoomView)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mainScope.cancel()
        mGetAllFrame.setStop(true)
    }

    fun getFrame(context: Context, media: LocalMedia) {
        mLocalMedia = media

        // 创建一个默认参数的协程，其默认的调度模式为Main 也就是说该协程的线程环境是Main线程
        mainScope.launch {

            // 这里就是协程体


            // 开启新的协程 async 主要用于获取返回值和并发
            val job = async(Dispatchers.IO) {
                // 给手指触摸移动的选中view设置显示的图片 一进来mZoomView初始值
                mGetFrameBitmap.setParams(
                    context, media, false, 0, mImageViewWidth, mImageViewHeight
                )
                mGetFrameBitmap.zoomSync()
            }
            // 获取异步的结果
            val await = job.await()
            await?.let { mZoomView?.setBitmap(it) }

            // 创建一个指定了调度模式的协程，该协程的运行线程为IO线程
            val job2 = mainScope.launch(Dispatchers.IO) {

                // 此处是IO线程模式
                Log.e("fei.wang", "io -> ${Thread.currentThread().name}")

                // 切线程 将协程所处的线程环境切至指定的调度模式Main
                withContext(Dispatchers.Main) {
                    Log.e("fei.wang", "main -> ${Thread.currentThread().name}")
                    // 现在这里就是Main线程了  可以在此进行UI操作了
                }
            }
        }

        mainScope.launch(Dispatchers.IO) {
            mGetAllFrame.setParams(
                context, media, mImageViews.size, 0, media.duration,
                OnSingleBitmapListenerImpl(this@CoverContainer)
            )
            mGetAllFrame.doInBackground()
        }
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
            mImageViews[i]?.layout(
                viewLeft, viewTop, viewLeft + (mImageViews[i]?.measuredWidth ?: 0),
                viewTop + (mImageViews[i]?.measuredHeight ?: 0)
            )
        }
        viewLeft = ScreenUtils.dip2px(context, 20F)
        mMaskView?.layout(
            viewLeft, viewTop, viewLeft + mMaskView!!.measuredWidth,
            viewTop + mMaskView!!.measuredHeight
        )
        mZoomView?.layout(
            viewLeft, viewTop, viewLeft + mZoomView!!.measuredWidth,
            viewTop + mZoomView!!.measuredHeight
        )
    }

    private var dxMove = 0f

    @SuppressLint("ClickableViewAccessibility", "ObjectAnimatorBinding")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rect = Rect()
        mMaskView?.getHitRect(rect)
        mOnSeekListener?.onSeekEnd()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 限制手指滑动范围的，滑动不再封面图控件上就不响应事件
                if (!rect.contains(event.getX().toInt(), event.getY().toInt())) {
                    return super.onTouchEvent(event)
                }

                startedTrackingX = event.x.toInt()
                startClickX = event.x.toInt()
                setScrollHorizontalPosition(
                    (startClickX - ScreenUtils.dip2px(
                        context,
                        20F
                    ) - mZoomView!!.measuredWidth / 2).toFloat()
                )
                Log.e("fei.wang", "手指按下")
            }
            MotionEvent.ACTION_MOVE -> {
                dxMove = (event.x - startedTrackingX)
                moveByX(dxMove)
                startedTrackingX = event.x.toInt()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                Log.e("fei.wang", "手指抬起")
                mOnSeekListener?.onSeek(mCurrentPercent, true)
                postDelayed({ moveBy() }, 100)
            }
        }
        return true
    }

    private fun moveByX(dx: Float) {
        setScrollHorizontalPosition(scrollHorizontalPosition + dx)
    }

    private fun moveBy() {
        setScrollHorizontalPositionX()
    }

    /**
     * 设置 X 轴上滑动的坐标
     */
    private fun setScrollHorizontalPositionX() {
        Log.e("当前滑动的x轴位置", "x -> $scrollHorizontalPosition")
        Log.e("当前滑动的x轴百分比", "mCurrentPercent -> " + mCurrentPercent * 100)
        mOnSeekListener?.onSeek(mCurrentPercent, false)
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
    private fun setScrollHorizontalPosition(value: Float) {
        val oldHorizontalPosition: Float = scrollHorizontalPosition
        scrollHorizontalPosition = 0f.coerceAtLeast(value)
            .coerceAtMost((mMaskView!!.measuredWidth - mZoomView!!.measuredWidth).toFloat())
        if (oldHorizontalPosition == scrollHorizontalPosition) {
            return
        }
        mZoomView?.translationX = scrollHorizontalPosition
        mCurrentPercent =
//            scrollHorizontalPosition / (mMaskView!!.measuredWidth - mZoomView!!.measuredWidth*2)
            scrollHorizontalPosition / (mMaskView!!.measuredWidth - mZoomView!!.measuredWidth) // 原来的算法
        Log.e("当前滑动的x轴位置", "x -> $scrollHorizontalPosition")
        Log.e("当前滑动的x轴百分比", "mCurrentPercent -> " + mCurrentPercent * 100)
        mOnSeekListener?.onSeek(mCurrentPercent, false)
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
//        val time = (mLocalMedia!!.duration * mCurrentPercent * 1000).roundToLong()
        val time = (mLocalMedia!!.duration * mCurrentPercent * 1000).toLong()

        // TODO 给手指触摸移动的选中view设置显示的图片 手指拖拽 mZoomView 移动的值 bitmap
        mainScope.launch {
            val job = async(Dispatchers.IO) {
                mGetFrameBitmap.setParams(
                    context, mLocalMedia, false,
                    time, mImageViewWidth, mImageViewHeight
                )
                mGetFrameBitmap.zoomSync()
            }
            val await = job.await()
            await?.let { mZoomView?.setBitmap(it) }
        }
    }

    // 最后生成封面的操作
    fun cropCover() {
        val time: Long = if (mCurrentPercent > 0) {
            (mLocalMedia!!.duration * mCurrentPercent * 1000).roundToLong()
        } else 0

        mainScope.launch {
            val job = async(Dispatchers.IO) {
                mGetFrameBitmap.setParams(
                    context, mLocalMedia, false,
                    time, ScreenUtils.dip2px(context, 500F), ScreenUtils.dip2px(context, 1000F)
                )
                mGetFrameBitmap.doInSync(mGetFrameBitmap.doInBackground())
            }
            val scanFilePath = job.await()
            mLiveData.setValue(scanFilePath)
        }
    }

    class OnSingleBitmapListenerImpl(coverContainer: CoverContainer) :
        GetAllFrame.OnSingleBitmapListener {
        private val mContainerWeakReference: WeakReference<CoverContainer> =
            WeakReference(coverContainer)
        private var index = 0
        override fun onSingleBitmapComplete(bitmap: Bitmap?) {
            val container = mContainerWeakReference.get()
            if (container != null) {
                container.post(RunnableImpl(container.mImageViews[index], bitmap))
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

    fun setOnSeekListener(onSeekListener: onSeekListener) {
        mOnSeekListener = onSeekListener
    }

    interface onSeekListener {
        /**
         * @param percent 当前x轴滑动的百分比
         * @param isStart true 设置当前的seekTo并同时播放，false 只设置当前的seekTo
         */
        fun onSeek(percent: Float, isStart: Boolean)
        fun onSeekEnd()
    }
}