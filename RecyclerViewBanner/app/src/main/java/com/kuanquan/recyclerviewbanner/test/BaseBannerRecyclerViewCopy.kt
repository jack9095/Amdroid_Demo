package com.kuanquan.recyclerviewbanner.test

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kuanquan.recyclerviewbanner.R

abstract class BaseBannerRecyclerViewCopy<L : LayoutManager?, A : RecyclerView.Adapter<*>?> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val WHAT_AUTO_PLAY = 1000
    }

    protected var mIndicator: Indicator? = null
    protected var mAutoPlayDuration = 4000 // 刷新间隔时间
//    protected var mShowIndicator = false  // 是否显示指示器
//    protected var mIndicatorContainer: RecyclerView? = null // 指示器，设置隐藏和显示
    protected var mSelectedDrawable: Drawable? = null
    protected var mUnselectedDrawable: Drawable? = null
    protected var mIndicatorAdapter: IndicatorAdapter? = null
//    protected var mIndicatorMargin = 0 // 指示器间距
    protected var mRecyclerView: RecyclerView? = null
    protected var mAdapter: A? = null
    protected var mLayoutManager: L? = null
    protected var mHasInit = false
    protected var mBannerSize = 1
    protected var mCurrentIndex = 0
    protected var mIsPlaying = false
    protected var mIsAutoPlaying = false
    protected var mTempUrlList: List<String> = ArrayList()
    protected val mHandler = Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == WHAT_AUTO_PLAY) {
            mRecyclerView?.smoothScrollToPosition(++mCurrentIndex)
            refreshIndicator()
            handlerSendEmptyMessageDelayed()
        }
        false
    }

    private fun handlerSendEmptyMessageDelayed() {
        mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, mAutoPlayDuration.toLong())
    }

    init {
        initView(context, attrs)
    }

    protected fun initView(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase)
//        mShowIndicator =
//            typedArray.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true)
        mAutoPlayDuration =
            typedArray.getInt(R.styleable.RecyclerViewBannerBase_interval, mAutoPlayDuration)
        mIsAutoPlaying = typedArray.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true)
        mSelectedDrawable =
            typedArray.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc)
        mUnselectedDrawable =
            typedArray.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc)
        if (mSelectedDrawable == null) {
            // 绘制默认选中状态图形
            val selectedGradientDrawable = GradientDrawable()
            selectedGradientDrawable.shape = GradientDrawable.OVAL
            selectedGradientDrawable.setColor(Color.RED)
            selectedGradientDrawable.setSize(dp2px(5), dp2px(5))
            selectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
            mSelectedDrawable = LayerDrawable(arrayOf<Drawable>(selectedGradientDrawable))
        }
        if (mUnselectedDrawable == null) {
            // 绘制默认未选中状态图形
            val unSelectedGradientDrawable = GradientDrawable()
            unSelectedGradientDrawable.shape = GradientDrawable.OVAL
            unSelectedGradientDrawable.setColor(Color.GRAY)
            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5))
            unSelectedGradientDrawable.cornerRadius = (dp2px(5) / 2).toFloat()
            mUnselectedDrawable = LayerDrawable(arrayOf<Drawable>(unSelectedGradientDrawable))
        }
//        mIndicatorMargin =
//            typedArray.getDimensionPixelSize(
//                R.styleable.RecyclerViewBannerBase_indicatorSpace, dp2px(4)
//            )
//        val marginLeft = typedArray.getDimensionPixelSize(
//            R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, dp2px(16)
//        )
//        val marginRight = typedArray.getDimensionPixelSize(
//            R.styleable.RecyclerViewBannerBase_indicatorMarginRight, dp2px(0)
//        )
//        val marginBottom = typedArray.getDimensionPixelSize(
//            R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, dp2px(11)
//        )
//        val indicatorGravity: Int = when (typedArray.getInt(
//            R.styleable.RecyclerViewBannerBase_indicatorGravity, 0
//        )) {
//            0 -> GravityCompat.START
//            2 -> GravityCompat.END
//            else -> Gravity.CENTER
//        }
        val attrOrientation = typedArray.getInt(
            R.styleable.RecyclerViewBannerBase_orientation, 0
        )
        var orientation = LinearLayoutManager.HORIZONTAL
        if (attrOrientation == 0) {
            orientation = LinearLayoutManager.HORIZONTAL
        } else if (attrOrientation == 1) {
            orientation = LinearLayoutManager.VERTICAL
        }
        typedArray.recycle()

        // recyclerView 部分
        mLayoutManager = getLayoutManager(context, orientation)
        mRecyclerView = RecyclerView(context).apply {
            PagerSnapHelper().attachToRecyclerView(this)
            layoutManager = mLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    onBannerScrolled(recyclerView, dx, dy)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    onBannerScrollStateChanged(recyclerView, newState)
                }
            })
        }

        val vpLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(mRecyclerView, vpLayoutParams)

        // 指示器部分
//        mIndicatorAdapter = IndicatorAdapter()
//        mIndicatorContainer = RecyclerView(context).apply {
//            layoutManager = LinearLayoutManager(context, orientation, false)
//            adapter = mIndicatorAdapter
//        }
//        val params = LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        ).apply {
//            gravity = Gravity.BOTTOM or indicatorGravity
//            setMargins(marginLeft, 0, marginRight, marginBottom)
//        }
//        addView(mIndicatorContainer, params)
//        if (!mShowIndicator) {
//            mIndicatorContainer?.isVisible = false
//        }
    }

    protected open fun onBannerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {}
    protected open fun onBannerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {}
    protected abstract fun getLayoutManager(context: Context?, orientation: Int): L
    protected abstract fun getAdapter(
        context: Context?, list: MutableList<String>?,
        onBannerItemClickListener: OnBannerItemClickListener?
    ): A

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    @Synchronized
    protected fun setAutoPlay(playing: Boolean) {
        this.mIsAutoPlaying = playing
        if (mIsAutoPlaying && mHasInit) {
            if (!mIsPlaying && playing) {
                handlerSendEmptyMessageDelayed()
                mIsPlaying = true
            } else if (mIsPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY)
                mIsPlaying = false
            }
        }
    }

    /**
     * 设置轮播数据集
     */
    @JvmOverloads
    fun initBannerImageView(
        newList: MutableList<String>, onBannerItemClickListener: OnBannerItemClickListener? = null
    ) {
        // 解决 recyclerView 嵌套问题
        if (compareListDifferent(newList, mTempUrlList)) {
            mHasInit = false
            visibility = VISIBLE
            setAutoPlay(false)
            mAdapter = getAdapter(context, newList, onBannerItemClickListener)
            mRecyclerView?.adapter = mAdapter
            mTempUrlList = newList
            mBannerSize = mTempUrlList.size
            if (mBannerSize > 1) {
//                mIndicatorContainer?.isVisible = true
                mCurrentIndex = mBannerSize * 10000
                mRecyclerView?.scrollToPosition(mCurrentIndex)
                mIndicatorAdapter?.notifyDataSetChanged()
                setAutoPlay(true)
            } else {
//                mIndicatorContainer?.isVisible = false
                mCurrentIndex = 0
            }
            mHasInit = true
            mIndicator?.initIndicatorCount(newList.size, 0)
        }
//        if (!mShowIndicator) {
//            mIndicatorContainer?.isVisible = false
//        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> setAutoPlay(false)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> setAutoPlay(true)
        }
        //解决recyclerView嵌套问题
        try {
            return super.dispatchTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    // 解决recyclerView嵌套问题
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    //解决recyclerView嵌套问题
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setAutoPlay(true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setAutoPlay(false)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            setAutoPlay(true)
        } else {
            setAutoPlay(false)
        }
    }

    /**
     * 标示点适配器
     */
    protected inner class IndicatorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
        var currentPosition = 0
        fun setPosition(currentPosition: Int) {
            this.currentPosition = currentPosition
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val bannerPoint = ImageView(context)
            val lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
//            lp.setMargins(mIndicatorMargin, mIndicatorMargin, mIndicatorMargin, mIndicatorMargin)
            bannerPoint.layoutParams = lp
            return object : RecyclerView.ViewHolder(bannerPoint) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val bannerPoint = holder.itemView as ImageView
            bannerPoint.setImageDrawable(if (currentPosition == position) mSelectedDrawable else mUnselectedDrawable)
        }

        override fun getItemCount(): Int {
            return mBannerSize
        }
    }

    /**
     * 改变导航的指示点
     */
    @Synchronized
    protected fun refreshIndicator() {
//        if (mShowIndicator && mBannerSize > 1) {
//            mIndicatorAdapter?.setPosition(mCurrentIndex % mBannerSize)
//            mIndicatorAdapter?.notifyDataSetChanged()
//        }
    }

    interface OnBannerItemClickListener {
        fun onItemClick(position: Int)
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()
    }

    protected fun compareListDifferent(
        newTabList: List<String>, oldTabList: List<String>?
    ): Boolean {
        if (oldTabList == null || oldTabList.isEmpty()) return true
        if (newTabList.size != oldTabList.size) return true
        for (i in newTabList.indices) {
            if (TextUtils.isEmpty(newTabList[i])) return true
            if (newTabList[i] != oldTabList[i]) {
                return true
            }
        }
        return false
    }

    fun setIndicator(indicator: Indicator) {
        setIndicator(indicator, true)
    }

    fun setIndicator(indicator: Indicator?, attachToRoot: Boolean) {
        mIndicator?.let {
            removeView(it.view)
        }
        if (indicator != null) {
            mIndicator = indicator
            if (attachToRoot) {
                addView(mIndicator!!.view, mIndicator!!.params)
            }
        }
    }
}