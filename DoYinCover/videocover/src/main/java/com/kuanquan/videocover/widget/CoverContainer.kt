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

@SuppressLint("ViewConstructor")
class CoverContainer(context: Context, media: LocalMedia) : FrameLayout(context), LifecycleObserver {

    private val mImageViews = arrayOfNulls<ImageView>(10) // 把视频几等份的图片集合，这里是 10等份
    private var mImageViewHeight = 0 // 展示图片控件的高度，写死的 60dp
    private var mImageViewWidth = 0 // 展示图片控件的宽度
    private var mMaskView: View? = null // 未被选择的图片上面的蒙层View，百分之70 的透明度
    private var mZoomView: ZoomView? = null // 选中图片上面的蒙板View,可以跟着手指滑动
    private var startedTrackingX = 0 // X轴跟踪手指移动的坐标
    private var scrollHorizontalPosition = 0f // 当前实时水平滑动的位置（x轴坐标）
    private var mOnSeekListener: onSeekListener? = null
    private var mLocalMedia: LocalMedia? = media // 传进来的数据，包含视频的路径
    private var mChangeTime: Long = 0  // 记录系统临时时间变量
    private var mCurrentPercent = 0f // 当前的百分比
    var mLiveData = MutableLiveData<String>()
    private var startClickX = 0 // 点击确认选中图片上面的蒙板View的x轴位置
    private val mainScope = MainScope()

    fun addLifeCycleObserver(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    init {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mainScope.cancel()
    }

    fun getFrame(context: Context, media: LocalMedia) {
        mainScope.launch {
            val job = async(Dispatchers.IO) {
                // 给手指触摸移动的选中view设置显示的图片 一进来mZoomView初始值
                GetFrameBitmap.setParams(
                    context, media, false, 0, mImageViewHeight, mImageViewHeight)
                GetFrameBitmap.doInBackground()
            }
            val await = job.await()
            await?.let { mZoomView?.setBitmap(it) }
        }

        mainScope.launch(Dispatchers.IO) {
            GetAllFrame.setParams(
                context, media, mImageViews.size, 0, media.duration,
                OnSingleBitmapListenerImpl(this@CoverContainer))
            GetAllFrame.doInBackground()
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
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rect = Rect()
        mMaskView?.getHitRect(rect)

        // 限制手指滑动范围的，滑动不再封面图控件上就不响应事件
//        if (!rect.contains((int) (event.getX()), (int) (event.getY()))) {
//            return super.onTouchEvent(event);
//        }
        mOnSeekListener?.onSeekEnd()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startedTrackingX = event.x.toInt()
                startClickX = event.x.toInt()
                setScrollHorizontalPosition(
                    (startClickX - ScreenUtils.dip2px(context, 20F) - mZoomView!!.measuredWidth / 2).toFloat()
                )
            }
            MotionEvent.ACTION_MOVE -> {
                dxMove = (event.x - startedTrackingX)
                moveByX(dxMove)
                startedTrackingX = event.x.toInt()
                mOnSeekListener?.onSeekEnd()
            }
            MotionEvent.ACTION_CANCEL -> {
                mOnSeekListener?.onSeekEnd()
            }
            MotionEvent.ACTION_UP -> {
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
    private fun setScrollHorizontalPosition(value: Float) {
        val oldHorizontalPosition: Float = scrollHorizontalPosition
        scrollHorizontalPosition = 0f.coerceAtLeast(value)
            .coerceAtMost((mMaskView!!.measuredWidth - mZoomView!!.measuredWidth).toFloat())
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
        val time = (mLocalMedia!!.duration * mCurrentPercent * 1000).roundToLong()

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
    }

    fun cropCover(count: CountDownLatch) {
        val time: Long = if (mCurrentPercent > 0) {
            (mLocalMedia!!.duration * mCurrentPercent * 1000).roundToLong()
        } else -1

        mainScope.launch {
            val job = async(Dispatchers.IO) {
                GetFrameBitmap.setParams(
                    context, mLocalMedia, false,
                    time, mImageViewHeight, mImageViewHeight)
                GetFrameBitmap.doInSync(GetFrameBitmap.doInBackground(), count)
            }
            val scanFilePath = job.await()
            mLiveData.setValue(scanFilePath)
        }
    }

    class OnSingleBitmapListenerImpl(coverContainer: CoverContainer) : GetAllFrame.OnSingleBitmapListener {
        private val mContainerWeakReference: WeakReference<CoverContainer> = WeakReference(coverContainer)
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