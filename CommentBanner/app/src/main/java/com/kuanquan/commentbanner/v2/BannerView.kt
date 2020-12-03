package com.kuanquan.commentbanner.v2

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.*
import kotlin.math.*


class BannerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var changeCallback: ViewPager2.OnPageChangeCallback? = null
    private val adapterWrapper by lazy {
        BannerAdapterWrapper()
    }
    private var indicator: LIndicator? = null
    private val viewPager2 by lazy {
        ViewPager2(context).apply {
            layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPageTransformer(CompositePageTransformer())
            registerOnPageChangeCallback(OnPageChangeCallback())
            offscreenPageLimit = 2
            adapter = adapterWrapper
        }
    }
    private var isAutoPlay = true // 是否自动滚动
    private var isBeginPagerChange = true
    private var autoTurningTime = DEFAULT_AUTO_TIME
    private var pagerScrollDuration = DEFAULT_PAGER_DURATION
    private var needPage = NORMAL_COUNT
    private var sidePage = needPage / NORMAL_COUNT
    private var tempPosition = 0
    private var startX = 0f
    private var startY = 0f
    private var lastX = 0f
    private var lastY = 0f
    // 它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
    private val scaledTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop shr 1

    companion object {
        private const val DEFAULT_AUTO_TIME: Long = 2500 // 默认自动滚动的时间值
        private const val DEFAULT_PAGER_DURATION: Long = 800 // ViewPager2 切换时长
        private const val NORMAL_COUNT = 2
    }

    init {
        initViewPagerScrollProxy()
        addView(viewPager2)
    }

    private fun startPager(startPosition: Int) {
        tempPosition = startPosition + sidePage
        viewPager2.setCurrentItem(tempPosition, false)
        adapterWrapper.notifyDataSetChanged()
        if (indicator != null) {
            indicator?.initIndicatorCount(realCount)
        }
        if (isAutoPlay()) {
            startAutoPlay()
        }
    }

    private val realCount: Int
        get() = adapterWrapper.realCount

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isAutoPlay()) {
            startAutoPlay()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isAutoPlay()) {
            stopAutoPlay()
        }
    }

    private val task: Runnable = object : Runnable {
        override fun run() {
            if (isAutoPlay()) {
                tempPosition++
                if (tempPosition == realCount + sidePage + 1) {
                    isBeginPagerChange = false
                    viewPager2.setCurrentItem(sidePage, false)
                    post(this)
                } else {
                    isBeginPagerChange = true
                    viewPager2.currentItem = tempPosition
                    postDelayed(this, autoTurningTime)
                }
            }
        }
    }

    private fun toRealPosition(position: Int): Int {
        var realPosition = 0
        if (realCount > 1) {
            realPosition = (position - sidePage) % realCount
        }
        if (realPosition < 0) {
            realPosition += realCount
        }
        return realPosition
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isAutoPlay() && viewPager2.isUserInputEnabled) {
            val action = ev.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay()
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            lastX = ev.rawX
            startX = lastX
            lastY = ev.rawY
            startY = lastY
        } else if (action == MotionEvent.ACTION_MOVE) {
            lastX = ev.rawX
            lastY = ev.rawY
            if (viewPager2.isUserInputEnabled) {
                val distanceX = abs(lastX - startX)
                val distanceY = abs(lastY - startY)
                val disallowIntercept: Boolean
                disallowIntercept = if (viewPager2.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    distanceX > scaledTouchSlop && distanceX > distanceY
                } else {
                    distanceY > scaledTouchSlop && distanceY > distanceX
                }
                parent.requestDisallowInterceptTouchEvent(disallowIntercept)
            }
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return abs(lastX - startX) > scaledTouchSlop || abs(lastY - startY) > scaledTouchSlop
        }
        return super.onInterceptTouchEvent(ev)
    }

    private inner class OnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val realPosition = toRealPosition(position)
            changeCallback?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            indicator?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            if (realCount > 1) {
                tempPosition = position
            }
            if (isBeginPagerChange) {
                val realPosition = toRealPosition(position)
                changeCallback?.onPageSelected(realPosition)
                indicator?.onPageSelected(realPosition)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                when (tempPosition) {
                    sidePage - 1 -> {
                        isBeginPagerChange = false
                        viewPager2.setCurrentItem(realCount + tempPosition, false)
                    }
                    realCount + sidePage -> {
                        isBeginPagerChange = false
                        viewPager2.setCurrentItem(sidePage, false)
                    }
                    else -> {
                        isBeginPagerChange = true
                    }
                }
            }
            changeCallback?.onPageScrollStateChanged(state)
            indicator?.onPageScrollStateChanged(state)
        }
    }

    private inner class BannerAdapterWrapper : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var adapter: RecyclerView.Adapter<*>? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return adapter!!.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val mAdapter = adapter as? RecyclerView.Adapter<RecyclerView.ViewHolder>
            mAdapter!!.onBindViewHolder(holder, toRealPosition(position))
        }

        override fun getItemViewType(position: Int): Int {
            return adapter!!.getItemViewType(toRealPosition(position))
        }

        override fun getItemCount(): Int {
            return if (realCount > 1) realCount + needPage else realCount
        }

        override fun getItemId(position: Int): Long {
            return adapter?.getItemId(toRealPosition(position)) ?: 0
        }

        val realCount: Int
            get() = adapter?.itemCount ?: 0

        fun registerAdapter(adapter: RecyclerView.Adapter<*>?) {
            this.adapter = adapter
            this.adapter?.registerAdapterDataObserver(itemDataSetChangeObserver)
        }
    }

    private val itemDataSetChangeObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart > 1) onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onChanged() {
            startPager(currentPager)
        }
    }

    private fun initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            val recyclerView = viewPager2.getChildAt(0) as RecyclerView
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val proxyLayoutManger = ProxyLayoutManger(context, viewPager2.orientation)
            recyclerView.layoutManager = proxyLayoutManger
            val layoutMangerField = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            layoutMangerField.isAccessible = true
            layoutMangerField[viewPager2] = proxyLayoutManger
            val pageTransformerAdapterField = ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
            pageTransformerAdapterField.isAccessible = true
            val mPageTransformerAdapter = pageTransformerAdapterField[viewPager2]
            if (mPageTransformerAdapter != null) {
                val aClass: Class<*> = mPageTransformerAdapter.javaClass
                val layoutManager = aClass.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager[mPageTransformerAdapter] = proxyLayoutManger
            }
            val scrollEventAdapterField = ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
            scrollEventAdapterField.isAccessible = true
            val mScrollEventAdapter = scrollEventAdapterField[viewPager2]
            if (mScrollEventAdapter != null) {
                val aClass: Class<*> = mScrollEventAdapter.javaClass
                val layoutManager = aClass.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager[mScrollEventAdapter] = proxyLayoutManger
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class ProxyLayoutManger internal constructor(context: Context?, orientation: Int) : LinearLayoutManager(context, orientation, false) {
        override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
            val linearSmoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(recyclerView.context) {
                override fun calculateTimeForDeceleration(dx: Int): Int {
                    return (pagerScrollDuration * (1 - 0.3356)).toInt()
                }
            }
            linearSmoothScroller.targetPosition = position
            startSmoothScroll(linearSmoothScroller)
        }
    }

    private fun dip2px(dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    /*--------------- 下面是对外暴露的方法 ---------------*/
    fun setAutoTurningTime(autoTurningTime: Long) {
        this.autoTurningTime = autoTurningTime
    }

    fun setOuterPageChangeListener(listener: ViewPager2.OnPageChangeCallback?) {
        changeCallback = listener
    }

    fun setOffscreenPageLimit(limit: Int) {
        viewPager2.offscreenPageLimit = limit
    }

    /**
     * 设置viewpager2的切换时长
     */
    fun setPagerScrollDuration(pagerScrollDuration: Long) {
        this.pagerScrollDuration = pagerScrollDuration
    }

    fun setAutoPlay(autoPlay: Boolean) {
        isAutoPlay = autoPlay
        if (isAutoPlay && realCount > 1) {
            startAutoPlay()
        }
    }

    fun isAutoPlay(): Boolean {
        return isAutoPlay && realCount > 1
    }

    /**
     * 设置indicator
     */
    fun setIndicator(indicator: LIndicator?) {
        if (this.indicator != null) {
            removeView(this.indicator?.getView())
        }
        if (indicator != null) {
            this.indicator = indicator
            addView(this.indicator?.getView(), this.indicator?.getParams())
        }
    }

    fun setRoundCorners(radius: Float) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, dip2px(radius))
            }
        }
        clipToOutline = true
    }

    /**
     * 返回真实位置
     */
    val currentPager: Int
        get() {
            val position = toRealPosition(tempPosition)
            return max(position, 0)
        }

    var adapter: RecyclerView.Adapter<*>?
        get() = adapterWrapper.adapter
        set(adapter) {
            setAdapter(adapter, 0)
        }

    fun startAutoPlay() {
        stopAutoPlay()
        postDelayed(task, autoTurningTime)
    }

    fun stopAutoPlay() {
        removeCallbacks(task)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?, startPosition: Int) {
        adapterWrapper.registerAdapter(adapter)
        startPager(startPosition)
    }
}