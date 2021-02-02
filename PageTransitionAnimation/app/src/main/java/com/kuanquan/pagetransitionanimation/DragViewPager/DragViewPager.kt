package com.kuanquan.pagetransitionanimation.DragViewPager

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import androidx.viewpager.widget.ViewPager
//import com.bx.core.utils.ScaleUtils
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DragViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    companion object {
        const val STATUS_NORMAL = 0 //正常浏览状态
        const val STATUS_MOVING = 1 //滑动状态
        const val STATUS_RESETTING = 2 //返回中状态
        const val MIN_SCALE_SIZE = 0.3f //最小缩放比例
        const val BACK_DURATION = 300 //ms
        const val DRAG_GAP_PX = 50
    }

    private var currentStatus = STATUS_NORMAL
    private var currentPageStatus = 0

    private var mDownX = 0f
    private var mDownY = 0f
    private var screenHeight = 0f

    // 要缩放的View
    private var currentShowView: View? = null

    // 设置背景颜色的View
    private var backView: View? = null

    // 滑动速度检测类
    private var mVelocityTracker: VelocityTracker? = null
    private var iAnimClose: IAnimClose? = null

    init {
//        screenHeight = ScaleUtils.getScreenHeight(context).toFloat()
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) { currentPageStatus = state }
        })
    }

    fun setCurrentShowView(currentShowView: View?, backView: View?) {
        this.currentShowView = currentShowView
        this.backView = backView
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (currentShowView != null) {
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        mDownX = ev.rawX
                        mDownY = ev.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = abs((ev.rawX - mDownX).toInt())
                        val deltaY = (ev.rawY - mDownY).toInt()
                        if (deltaY > DRAG_GAP_PX && deltaX <= DRAG_GAP_PX) {
                            return true
                        }
                    }
                }
            }
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private var isMove = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (currentStatus == STATUS_RESETTING) {
            return false
        }
        try {
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = ev.rawX
                    mDownY = ev.rawY
                    addIntoVelocity(ev)
                }
                MotionEvent.ACTION_MOVE -> {
                    addIntoVelocity(ev)
                    val deltaY = (ev.rawY - mDownY).toInt()
                    //手指往上滑动
                    if (deltaY <= DRAG_GAP_PX && currentStatus != STATUS_MOVING) {
                        return super.onTouchEvent(ev)
                    }
                    //viewpager不在切换中，并且手指往下滑动，开始缩放
                    if (currentPageStatus != SCROLL_STATE_DRAGGING && (deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING)) {
                        if (isMove) {
                            isMove = false
                            iAnimClose?.onPictureClick()
                        }
                        iAnimClose?.onPictureClick()
                        moveView(ev.rawX, ev.rawY)
                        return true
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isMove = true
                    if (currentStatus != STATUS_MOVING) {
                        return super.onTouchEvent(ev)
                    }
                    val mUpX = ev.rawX
                    val mUpY = ev.rawY
                    val vY = computeYVelocity()
                    if (vY >= 1200 || mUpY - mDownY > screenHeight / 5) {
                        if (iAnimClose != null) {
                            iAnimClose?.onPictureRelease()
                        }
                    } else {
                        iAnimClose?.resetReviewState()
                        resetReviewState(mUpX, mUpY)
                    }
                }
            }
            return super.onTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //返回浏览状态
    private fun resetReviewState(mUpX: Float, mUpY: Float) {
        currentStatus = STATUS_RESETTING
        if (mUpY != mDownY) {
            val valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY)
            valueAnimator.duration = BACK_DURATION.toLong()
            valueAnimator.addUpdateListener { animation: ValueAnimator ->
                val mY = animation.animatedValue as Float
                val percent = (mY - mDownY) / (mUpY - mDownY)
                val mX = percent * (mUpX - mDownX) + mDownX
                moveView(mX, mY)
                if (mY == mDownY) {
                    mDownY = 0f
                    mDownX = 0f
                    currentStatus = STATUS_NORMAL
                }
            }
            valueAnimator.start()
        } else if (mUpX != mDownX) {
            val valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX)
            valueAnimator.duration = BACK_DURATION.toLong()
            valueAnimator.addUpdateListener { animation: ValueAnimator ->
                val mX = animation.animatedValue as Float
                val percent = (mX - mDownX) / (mUpX - mDownX)
                val mY = percent * (mUpY - mDownY) + mDownY
                moveView(mX, mY)
                if (mX == mDownX) {
                    mDownY = 0f
                    mDownX = 0f
                    currentStatus = STATUS_NORMAL
                }
            }
            valueAnimator.start()
        }
    }

    //移动View
    private fun moveView(movingX: Float, movingY: Float) {
        currentStatus = STATUS_MOVING
        val deltaX = movingX - mDownX
        val deltaY = movingY - mDownY
        var scale = 1f
        var alphaPercent = 1f
        if (deltaY > 0) {
            scale = 1 - abs(deltaY) / screenHeight
            alphaPercent = 1 - abs(deltaY) / (screenHeight / 6)
        }
        currentShowView?.translationX = deltaX
        currentShowView?.translationY = deltaY
        scaleView(scale)
        backView?.setBackgroundColor(getBlackAlpha(alphaPercent))
    }

    //缩放View
    private fun scaleView(scale: Float) {
        var scale = scale
        scale = min(max(scale, MIN_SCALE_SIZE), 1f)
        currentShowView?.scaleX = scale
        currentShowView?.scaleY = scale
    }

    private fun getBlackAlpha(percent: Float): Int {
        var percent = percent
        percent = min(1f, max(0f, percent))
        val intAlpha = (percent * 255).toInt()
        return Color.argb(intAlpha, 0, 0, 0)
    }

    private fun addIntoVelocity(event: MotionEvent) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker?.addMovement(event)
    }

    private fun computeYVelocity(): Float {
        var result = 0f
        if (mVelocityTracker != null) {
            mVelocityTracker?.computeCurrentVelocity(1000)
            result = mVelocityTracker?.yVelocity ?: 0f
            releaseVelocity()
        }
        return result
    }

    private fun releaseVelocity() {
        mVelocityTracker?.clear()
        mVelocityTracker?.recycle()
    }

    fun setIAnimClose(iAnimClose: IAnimClose?) {
        this.iAnimClose = iAnimClose
    }

    interface IAnimClose {
        fun onPictureClick()
        fun onPictureRelease()
        fun resetReviewState()
    }
}