package com.kuanquan.doyincover.publisher.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.kuanquan.doyincover.utils.ScreenUtils


/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/07
 * Description:
 */
class RecordButton @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(mContext, attrs, defStyleAttr) {

  private lateinit var mRectPaint: Paint

  private lateinit var mCirclePaint: Paint

  private var circleRadius: Float = 0.toFloat()
  private var circleStrokeWidth: Float = 0.toFloat()
  private var rectWidth: Float = 0.toFloat()

  private var mMaxRectWidth: Float = 0.toFloat()

  private var mMinCircleRadius: Float = 0.toFloat()
  private var mMaxCircleRadius: Float = 0.toFloat()
  private var mMinCircleStrokeWidth: Float = 0.toFloat()
  private var mMaxCircleStrokeWidth: Float = 0.toFloat()


  private val mRectF = RectF()

  private var mRecordMode = RecordMode.ORIGIN

  private val mBeginAnimatorSet = AnimatorSet()

  private val mEndAnimatorSet = AnimatorSet()

  private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

  private val mHandler = Handler()

  private val mClickRunnable = ClickRunnable()

  private var mOnRecordStateChangedListener: OnRecordStateChangedListener? = null

  private var mInitX: Float = 0.toFloat()

  private var mInitY: Float = 0.toFloat()

  private var mDownRawX: Float = 0.toFloat()

  private var mDownRawY: Float = 0.toFloat()

  private var mInfectionPoint: Float = 0.toFloat()

  private var mScrollDirection: ScrollDirection? = null

  private var mHasCancel = false

  init {
    init()
  }

  private fun init() {
    setLayerType(View.LAYER_TYPE_HARDWARE, null)
    mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    mRectPaint.style = Paint.Style.FILL

    mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    mMaxRectWidth = ScreenUtils.dip2px(context,28f).toFloat()

    mMinCircleStrokeWidth = 10f
    mMaxCircleStrokeWidth = 20f
    mCirclePaint.strokeWidth = circleStrokeWidth
  }

  @SuppressLint("DrawAllocation")
  override fun onDraw(canvas: Canvas) {
    val width = measuredWidth
    val height = measuredHeight

    val backGradient = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(),
        Color.parseColor("#ffef30"), Color.parseColor("#59ff5a"), Shader.TileMode.CLAMP)
    mCirclePaint.shader = backGradient

    val centerX = width / 2
    val centerY = height / 2

    mMinCircleRadius = width / 2 * 0.8f - mMaxCircleStrokeWidth
    mMaxCircleRadius = width / 2 - mMaxCircleStrokeWidth

    if (circleRadius == 0f) {
      circleRadius = mMinCircleRadius
    }
    if (circleStrokeWidth == 0f) {
      circleStrokeWidth = mMaxCircleStrokeWidth
    }

    canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius, mCirclePaint)
    mCirclePaint.xfermode = mXfermode

    canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), circleRadius - circleStrokeWidth, mCirclePaint)
    mCirclePaint.xfermode = null

    mRectF.left = centerX - rectWidth / 2
    mRectF.right = centerX + rectWidth / 2
    mRectF.top = centerY - rectWidth / 2
    mRectF.bottom = centerY + rectWidth / 2


    val centerGradient = LinearGradient(mRectF.left, mRectF.top, mRectF.right, mRectF.bottom,
      Color.parseColor("#ffef30"), Color.parseColor("#59ff5a"), Shader.TileMode.CLAMP)
    mRectPaint.shader = centerGradient

    canvas.drawRoundRect(mRectF, ScreenUtils.dip2px(context,4f).toFloat(), ScreenUtils.dip2px(context,4f).toFloat(), mRectPaint)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {

    when (event.action) {
      MotionEvent.ACTION_DOWN -> if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
        mDownRawX = event.rawX
        mDownRawY = event.rawY
        startBeginAnimation()
        mHandler.postDelayed(mClickRunnable, 200)
        mOnRecordStateChangedListener!!.onRecordStart()
      }
      MotionEvent.ACTION_MOVE -> if (!mHasCancel) {
        if (mRecordMode == RecordMode.LONG_CLICK) {
          val mOldDirection = mScrollDirection
          val oldY = y
          x = mInitX + event.rawX - mDownRawX
          y = mInitY + event.rawY - mDownRawY
          val newY = y

          if (newY <= oldY) {
            mScrollDirection = ScrollDirection.UP
          } else {
            mScrollDirection = ScrollDirection.DOWN
          }

          if (mOldDirection != mScrollDirection) {
            mInfectionPoint = oldY
          }
          val zoomPercentage = (mInfectionPoint - y) / mInitY
          mOnRecordStateChangedListener!!.onZoom(zoomPercentage)
        }
      }
      MotionEvent.ACTION_UP -> if (!mHasCancel) {
        if (mRecordMode == RecordMode.LONG_CLICK) {
          mOnRecordStateChangedListener!!.onRecordStop()
          resetLongClick()
        } else if (mRecordMode == RecordMode.ORIGIN && inBeginRange(event)) {
          mHandler.removeCallbacks(mClickRunnable)
          mRecordMode = RecordMode.SINGLE_CLICK
        } else if (mRecordMode == RecordMode.SINGLE_CLICK && inEndRange(event)) {
          mOnRecordStateChangedListener!!.onRecordStop()
          resetSingleClick()
        }
      } else {
        mHasCancel = false
      }
      else -> {
      }
    }
    return true
  }

  private fun inBeginRange(event: MotionEvent): Boolean {
    val centerX = measuredWidth / 2
    val centerY = measuredHeight / 2
    val minX = (centerX - mMinCircleRadius).toInt()
    val maxX = (centerX + mMinCircleRadius).toInt()
    val minY = (centerY - mMinCircleRadius).toInt()
    val maxY = (centerY + mMinCircleRadius).toInt()
    val isXInRange = event.x >= minX && event.x <= maxX
    val isYInRange = event.y >= minY && event.y <= maxY
    return isXInRange && isYInRange
  }

  private fun inEndRange(event: MotionEvent): Boolean {
    val minX = 0
    val maxX = measuredWidth
    val minY = 0
    val maxY = measuredHeight
    val isXInRange = event.x >= minX && event.x <= maxX
    val isYInRange = event.y >= minY && event.y <= maxY
    return isXInRange && isYInRange
  }

  private fun resetLongClick() {
    mRecordMode = RecordMode.ORIGIN
    mBeginAnimatorSet.cancel()
    startEndAnimation()
    x = mInitX
    y = mInitY
  }

  private fun resetSingleClick() {
    mRecordMode = RecordMode.ORIGIN
    mBeginAnimatorSet.cancel()
    startEndAnimation()
  }

  fun reset() {
    if (mRecordMode == RecordMode.LONG_CLICK) {
      resetLongClick()
    } else if (mRecordMode == RecordMode.SINGLE_CLICK) {
      resetSingleClick()
    } else if (mRecordMode == RecordMode.ORIGIN) {
      if (mBeginAnimatorSet.isRunning) {
        mHasCancel = true
        mBeginAnimatorSet.cancel()
        startEndAnimation()
        mHandler.removeCallbacks(mClickRunnable)
        mRecordMode = RecordMode.ORIGIN
      }
    }
  }

  fun startClockRecord() {
    if (mRecordMode == RecordMode.ORIGIN) {
      startBeginAnimation()
      mRecordMode = RecordMode.SINGLE_CLICK
    }
  }

  @SuppressLint("AnimatorKeep")
  private fun startBeginAnimation() {
    val rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
        0f, mMaxRectWidth)
        .setDuration(500)
    val radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
        mMinCircleRadius, mMaxCircleRadius)
        .setDuration(500)
    val circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
        mMaxCircleStrokeWidth, mMinCircleStrokeWidth, mMaxCircleStrokeWidth)
        .setDuration(1500)
    circleWidthAnimator.repeatCount = ObjectAnimator.INFINITE

    mBeginAnimatorSet.playTogether(rectSizeAnimator, radiusAnimator, circleWidthAnimator)
    mBeginAnimatorSet.start()
  }

  private fun startEndAnimation() {
    val rectSizeAnimator = ObjectAnimator.ofFloat(this, "rectWidth",
        mMaxRectWidth, 0f)
        .setDuration(500)
    val radiusAnimator = ObjectAnimator.ofFloat(this, "circleRadius",
        mMaxCircleRadius, mMinCircleRadius)
        .setDuration(500)
    val circleWidthAnimator = ObjectAnimator.ofFloat(this, "circleStrokeWidth",
        mMinCircleStrokeWidth, mMaxCircleStrokeWidth)
        .setDuration(500)

    mEndAnimatorSet.playTogether(rectSizeAnimator, radiusAnimator, circleWidthAnimator)
    mEndAnimatorSet.start()
  }


  fun setCircleRadius(circleRadius: Float) {
    this.circleRadius = circleRadius
  }

  fun setCircleStrokeWidth(circleStrokeWidth: Float) {
    this.circleStrokeWidth = circleStrokeWidth
    invalidate()
  }

  fun setRectWidth(rectWidth: Float) {
    this.rectWidth = rectWidth
  }

  internal inner class ClickRunnable : Runnable {

    override fun run() {
      if (!mHasCancel) {
        mRecordMode = RecordMode.LONG_CLICK
        mInitX = x
        mInitY = y
        mInfectionPoint = mInitY
        mScrollDirection = ScrollDirection.UP
      }
    }
  }

  fun setOnRecordStateChangedListener(listener: OnRecordStateChangedListener) {
    this.mOnRecordStateChangedListener = listener
  }

  interface OnRecordStateChangedListener {

    /**
     * 开始录制
     */
    fun onRecordStart()

    /**
     * 结束录制
     */
    fun onRecordStop()

    /**
     * 缩放百分比
     *
     * @param percentage 百分比值 0%~100% 对应缩放支持的最小和最大值 默认最小1.0
     */
    fun onZoom(percentage: Float)
  }

  private enum class RecordMode() {
    /**
     * 单击录制模式
     */
    SINGLE_CLICK,
    /**
     * 长按录制模式
     */
    LONG_CLICK,
    /**
     * 初始化
     */
    ORIGIN
  }

  private enum class ScrollDirection() {
    /**
     * 滑动方向 上
     */
    UP,
    /**
     * 滑动方向 下
     */
    DOWN
  }
}