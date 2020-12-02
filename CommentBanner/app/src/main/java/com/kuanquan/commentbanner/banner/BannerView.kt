package com.kuanquan.commentbanner.banner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.kuanquan.commentbanner.R
import com.kuanquan.commentbanner.banner.adapter.BannerAdapter
import com.kuanquan.commentbanner.banner.config.BannerConfig
import com.kuanquan.commentbanner.banner.indicator.Indicator
import com.kuanquan.commentbanner.banner.util.BannerUtils
import java.lang.ref.WeakReference
import kotlin.math.abs

class BannerView: FrameLayout {

    val INVALID_VALUE = -1
    private lateinit var mViewPager2: ViewPager2
    private var mAdapter: BannerAdapter<*, *>? = null
//    private var mAdapter: BaseQuickAdapter<*, *>? = null
    // 指示器
    private val mIndicator: Indicator? = null
    private var mCompositePageTransformer: CompositePageTransformer? = null

    // 自动轮播的任务
    private val mLoopTask: AutoLoopTask? = null

    // 是否允许无限轮播（即首尾直接切换）
    private val mIsInfiniteLoop = BannerConfig.IS_INFINITE_LOOP

    // 是否自动轮播
    private val mIsAutoLoop = BannerConfig.IS_AUTO_LOOP

    // 轮播切换间隔时间
    private val mLoopTime = BannerConfig.LOOP_TIME.toLong()

    // 轮播切换时间
    private val mScrollTime = BannerConfig.SCROLL_TIME

    // 轮播开始位置
    private val mStartPosition = 1

    // 滑动距离范围
    private var mTouchSlop = 0

    // 记录触摸的位置（主要用于解决事件冲突问题）
    private var mStartX = 0f
    private var mStartY = 0f

    // 记录viewpager2是否被拖动
    private var mIsViewPager2Drag = false

    // 是否要拦截事件
    private var isIntercept = true

    constructor(context: Context): super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop / 2
        mCompositePageTransformer = CompositePageTransformer()
        mViewPager2 = ViewPager2(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            offscreenPageLimit = 1
            setPageTransformer(mCompositePageTransformer)
        }

//        mViewPager2.registerOnPageChangeCallback(mPageChangeCallback)
//        ScrollSpeedManger.reflectLayoutManager(this)
        addView(mViewPager2)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!getViewPager2().isUserInputEnabled) {
            return super.dispatchTouchEvent(ev)
        }
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_UP
            || action == MotionEvent.ACTION_CANCEL
            || action == MotionEvent.ACTION_OUTSIDE) {
            start()
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!getViewPager2().isUserInputEnabled || !isIntercept) {
            return super.onInterceptTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val endY = event.y
                val distanceX = abs(endX - mStartX)
                val distanceY = abs(endY - mStartY)
                mIsViewPager2Drag = if (getViewPager2().orientation == Banner.HORIZONTAL) {
                    distanceX > mTouchSlop && distanceX > distanceY
                } else {
                    distanceY > mTouchSlop && distanceY > distanceX
                }
                parent.requestDisallowInterceptTouchEvent(mIsViewPager2Drag)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

//    internal class BannerOnPageChangeCallback : OnPageChangeCallback() {
//        private var mTempPosition = BannerCopy.INVALID_VALUE
//        private var isScrolled = false
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            val realPosition =
//                BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount())
//            if (mOnPageChangeListener != null) {
//                mOnPageChangeListener.onPageScrolled(
//                    realPosition,
//                    positionOffset,
//                    positionOffsetPixels
//                )
//            }
//            if (mIndicator != null) {
//                mIndicator.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
//            }
//        }
//
//        override fun onPageSelected(position: Int) {
//            if (isScrolled) {
//                mTempPosition = position
//                val realPosition =
//                    BannerUtils.getRealPosition(isInfiniteLoop(), position, getRealCount())
//                if (mOnPageChangeListener != null) {
//                    mOnPageChangeListener.onPageSelected(realPosition)
//                }
//                if (mIndicator != null) {
//                    mIndicator.onPageSelected(realPosition)
//                }
//            }
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            //手势滑动中,代码执行滑动中
//            if (state == ViewPager2.SCROLL_STATE_DRAGGING || state == ViewPager2.SCROLL_STATE_SETTLING) {
//                isScrolled = true
//            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                //滑动闲置或滑动结束
//                isScrolled = false
//                if (mTempPosition != BannerCopy.INVALID_VALUE && mIsInfiniteLoop) {
//                    if (mTempPosition == 0) {
//                        setCurrentItem(getRealCount(), false)
//                    } else if (mTempPosition == getItemCount() - 1) {
//                        setCurrentItem(1, false)
//                    }
//                }
//            }
//            if (mOnPageChangeListener != null) {
//                mOnPageChangeListener.onPageScrollStateChanged(state)
//            }
//            if (mIndicator != null) {
//                mIndicator.onPageScrollStateChanged(state)
//            }
//        }
//    }

    internal class AutoLoopTask(bannerView: BannerView) : Runnable {
        private val reference: WeakReference<BannerView> = WeakReference(bannerView)
        override fun run() {
            val bannerView = reference.get()
            if (bannerView != null && bannerView.mIsAutoLoop) {
                val count = bannerView.getItemCount()
                if (count == 0) {
                    return
                }
                val next = (bannerView.getCurrentItem() + 1) % count
                bannerView.setCurrentItem(next)
                bannerView.postDelayed(bannerView.mLoopTask, bannerView.mLoopTime)
            }
        }
    }

    /**
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */

    fun getCurrentItem(): Int {
        return getViewPager2().currentItem
    }

    fun getItemCount(): Int {
        return mAdapter?.itemCount ?: 0
    }


    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     * @param position
     * @return
     */
    fun setCurrentItem(position: Int) {
        setCurrentItem(position, true)
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     * @param position
     * @param smoothScroll
     * @return
     */
    fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        getViewPager2().setCurrentItem(position, smoothScroll)
//        return this
    }

//    fun getAdapter(): BA? {
//        if (mAdapter == null) {
//            LogUtils.e(context.getString(R.string.banner_adapter_use_error))
//        }
//        return mAdapter
//    }

    fun getViewPager2(): ViewPager2 {
        return mViewPager2
    }

    /**
     * 开始轮播
     */
    fun start() {
        if (mIsAutoLoop) {
            stop()
            postDelayed(mLoopTask, mLoopTime)
        }
    }

    /**
     * 停止轮播
     */
    fun stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask)
        }
    }

    fun getScrollTime(): Int {
        return mScrollTime
    }

    fun isInfiniteLoop(): Boolean {
        return mIsInfiniteLoop
    }

    /**
     * 设置banner的适配器
     */
    fun setAdapter(adapter: BannerAdapter<*, *>?) {
        if (adapter == null) {
            throw NullPointerException(context.getString(R.string.banner_adapter_null_error))
        }
        mAdapter = adapter
        if (!isInfiniteLoop()) {
            mAdapter?.setIncreaseCount(0)  // 允许无限循环
        }
//        mAdapter?.registerAdapterDataObserver(mAdapterDataObserver)
        mViewPager2.adapter = adapter
        setCurrentItem(mStartPosition, false)
    }
}