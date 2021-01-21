package com.kuanquan.pagetransitionanimation

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * 父控件的拉动滑动动画，更易于集成
 */
class AnimationFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), GestureDetector.OnGestureListener {

    companion object {
        const val STATUS_NORMAL = 0 // 正常浏览状态
        const val STATUS_MOVING = 1 // 滑动状态
        const val STATUS_RESETTING = 2 // 返回中状态
        const val DEFAULT_EXIT_SCALE = 0.7f // 退出进度
        const val DEFAULT_TRANSPARENCY = 0.8f // 默认透明度
        const val DEFAULT_TOUCH_VALUE = 1f // 默认触摸退出
        const val DEFAULT_MAX_TOUCH_VALUE = 2 // 最大触摸退出
        const val DEFAULT_ANIMATOR_DURATION = 500L // 默认动画执行时长
    }

    private val mGestureDetector: GestureDetector by lazy { GestureDetector(getContext(), this@AnimationFrameLayout) }

    private val mColorEvaluator by lazy {
        TypeEvaluator<Int> { fraction, startValue, endValue ->
            val alpha = (Color.alpha(startValue) + fraction * (Color.alpha(endValue) - Color.alpha(startValue))).toInt()
            val red = (Color.red(startValue) + fraction * (Color.red(endValue) - Color.red(startValue))).toInt()
            val green = (Color.green(startValue) + fraction * (Color.green(endValue) - Color.green(startValue))).toInt()
            val blue = (Color.blue(startValue) + fraction * (Color.blue(endValue) - Color.blue(startValue))).toInt()
            Color.argb(alpha, red, green, blue)
        }
    }

    //FrameLayout的第一个子view
    private var parent: View? = null

    //触摸退出进度
    private var mExitScalingRef = 0f

    //子view的高度
    private var viewHeight = 0

    //结束的监听器
    private var finishListener: FinishListener? = null

    // 是否水平滑动 true 表示水平滑动
    private var isHorizontal = false

    // 是否随着手指头开始滑动
    private var isScroll = false
    private var currentStatus = STATUS_NORMAL
    private var mDownY = 0f
    private var deltaY = 0 // 垂直移动的距离

    interface FinishListener {
        fun gofinish()
        fun setBackgroundColor(color: Int)
    }

    fun setFinishListener(finishListener: FinishListener?) {
        this.finishListener = finishListener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> deltaY = (event.rawY - mDownY).toInt()
            MotionEvent.ACTION_UP -> {
                isHorizontal = false
                isScroll = false
                if (currentStatus != STATUS_MOVING) return super.onTouchEvent(event)
                if (mExitScalingRef < DEFAULT_EXIT_SCALE) {
//                    setBackgroundColor(Color.parseColor("#00000000"))
                    finishListener?.setBackgroundColor(Color.TRANSPARENT)
                    //缩小到一定的程度，将其关闭
                    finishListener?.gofinish()
                } else {
                    //如果拉动距离不到某个角度，则将其动画返回原位置
                    ValueAnimator.ofFloat(0f, 1f).apply {
                        duration = DEFAULT_ANIMATOR_DURATION
                        addUpdateListener { animation ->
                            val p = animation.animatedValue as Float
                            parent?.run {
                                translationX += (0 - translationX) * p
                                translationY += (0 - translationY) * p
                                scaleX += (1 - scaleX) * p
                                scaleY += (1 - scaleY) * p
//                                finishListener?.setBackgroundColor(Color.WHITE)
                            }
                        }
                        start()
                    }
                }
                currentStatus = STATUS_NORMAL
            }
        }
        return mGestureDetector.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mDownY = ev.rawY
            MotionEvent.ACTION_MOVE -> {
                if (moveEvent(ev.rawY - mDownY)) {
                    requestDisallowInterceptTouchEvent(true)
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun moveEvent(deltaY: Float): Boolean {
        if (currentStatus == STATUS_RESETTING) return false
        val interceptY = deltaY > ViewConfiguration.get(context).scaledTouchSlop
        if (!interceptY && !isScroll) isHorizontal = true else isScroll = true
        return abs(deltaY) > 0
    }

    override fun onShowPress(e: MotionEvent?) {}
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onDown(e: MotionEvent?): Boolean = true
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean = false
    override fun onLongPress(e: MotionEvent?) {}

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        if (parent == null) parent = getChildAt(0)
        if (viewHeight == 0) viewHeight = parent?.height ?: 0
        val moveX = e1?.x?.let { e2?.x?.minus(it) } ?: 0f
        val moveY = e1?.y?.let { e2?.y?.minus(it) } ?: 0f
        mExitScalingRef = DEFAULT_TOUCH_VALUE
        //viewpager2 或者 RecyclerView(水平) 不在切换中，并且手指往下滑动，开始缩放
        if (moveEvent(deltaY.toFloat()) && !isHorizontal) {
            mExitScalingRef -= moveY / viewHeight
            parent?.run {
                translationX = moveX
                translationY = moveY
                scaleX = mExitScalingRef
                scaleY = mExitScalingRef
            }
            currentStatus = STATUS_MOVING
            val mExitScalingColor = if (mExitScalingRef > DEFAULT_TOUCH_VALUE) {
                DEFAULT_MAX_TOUCH_VALUE - mExitScalingRef
            } else mExitScalingRef
            val colorValue = mColorEvaluator.evaluate(mExitScalingColor.coerceAtMost(DEFAULT_TRANSPARENCY), 0x00000000, -0x1000000)
//            finishListener?.setBackgroundColor(colorValue)
            setBackgroundColor(colorValue)
        }
        return false
    }
}