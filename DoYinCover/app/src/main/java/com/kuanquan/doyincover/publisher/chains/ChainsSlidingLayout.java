package com.kuanquan.doyincover.publisher.chains;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kuanquan.doyincover.R;
import com.kuanquan.doyincover.publisher.view.Instrument;
import com.kuanquan.doyincover.utils.Contexts;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/13
 * Description:
 */
public class ChainsSlidingLayout extends FrameLayout {

  private int mTouchSlop;//系统允许最小的滑动判断值
  private int mBackgroundViewLayoutId = R.layout.view_chains_bottom;

  private View mBackgroundView;//背景View
  private View mTargetView;//正面View

  private boolean mIsBeingDragged;
  private float mInitialDownY;
  private float mInitialMotionY;
  private float mLastMotionY;
  private int mActivePointerId = INVALID_POINTER;

  private float mSlidingOffset = 0.6F;//滑动阻力系数

  private static final int RESET_DURATION = 200;

  public static final int SLIDING_MODE_BOTTOM = 2;

  public static final int SLIDING_POINTER_MODE_ONE = 0;
  public static final int SLIDING_POINTER_MODE_MORE = 1;

  private int mSlidingMode = SLIDING_MODE_BOTTOM;

  private int mSlidingPointerMode = SLIDING_POINTER_MODE_MORE;

  private static final int INVALID_POINTER = -1;

  private SlidingListener mSlidingListener;

  public static final int STATE_SLIDING = 2;
  public static final int STATE_IDLE = 1;

  private int mSlidingBottomMaxDistance;

  private OnTouchListener mDelegateTouchListener;

  private TextView nextVideoTitle;

  private boolean isEnableSliding = true;

  public boolean isEnableSliding() {
    return isEnableSliding;
  }

  public void setEnableSliding(boolean enableSliding) {
    isEnableSliding = enableSliding;
    mBackgroundView.findViewById(R.id.arrow_image).setVisibility(enableSliding ? View.VISIBLE : View.INVISIBLE);
  }

  public interface SlidingListener {
    void onLoadNextVideo();

    void onStateChanged(int state);
  }

  public ChainsSlidingLayout(Context context) {
    this(context, null);
  }

  public ChainsSlidingLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ChainsSlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    View view = View.inflate(getContext(), mBackgroundViewLayoutId, null);
    setBackgroundView(view);

    nextVideoTitle = mBackgroundView.findViewById(R.id.loadmore_view);

    ImageView image = mBackgroundView.findViewById(R.id.arrow_image);

    Drawable dw = ContextCompat.getDrawable(getContext(), R.drawable.animation_chains_bottom_arrow);
    if (dw != null) {
      image.setImageDrawable(dw);
      AnimationDrawable animate = (AnimationDrawable) dw;
      animate.start();
    }

    mSlidingBottomMaxDistance = -Contexts.dip(getContext(), 100f);

    mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
  }

  public TextView getNextVideoTitle() {
    return nextVideoTitle;
  }

  public void setBackgroundView(View view) {
    if (mBackgroundView != null) {
      this.removeView(mBackgroundView);
    }
    mBackgroundView = view;
    this.addView(view, 0);
  }

  public View getBackgroundView() {
    return this.mBackgroundView;
  }

  /**
   * 获得滑动幅度
   *
   * @return
   */
  public float getSlidingOffset() {
    return this.mSlidingOffset;
  }

  /**
   * 设置滑动幅度
   *
   * @param slidingOffset
   */
  public void setSlidingOffset(float slidingOffset) {
    this.mSlidingOffset = slidingOffset;
  }

  public void setSlidingListener(SlidingListener slidingListener) {
    this.mSlidingListener = slidingListener;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    //实际上整个layout只能存在一个背景和一个前景才有用途
//        if(getChildCount() > 2){
//
//        }
    if (getChildCount() == 0) {
      return;
    }
    if (mTargetView == null) {
      ensureTarget();
    }
    if (mTargetView == null) {
      return;
    }
  }

  private void ensureTarget() {
    if (mTargetView == null) {
      mTargetView = getChildAt(getChildCount() - 1);
    }
  }

  public void setTargetView(View view) {
    if (mTargetView != null) {
      this.removeView(mTargetView);
    }
    mTargetView = view;
    this.addView(view);
  }

  @Override
  public void setOnTouchListener(OnTouchListener l) {
    mDelegateTouchListener = l;
  }

  public View getTargetView() {
    return this.mTargetView;
  }

  public float getSlidingDistance() {
    return getInstrument().getTranslationY(getTargetView());
  }

  public Instrument getInstrument() {
    return Instrument.getInstance();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    ensureTarget();
    final int action = ev.getActionMasked();
    //判断拦截
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mActivePointerId = ev.getPointerId(0);
        mIsBeingDragged = false;
        final float initialDownY = getMotionEventY(ev, mActivePointerId);
        if (initialDownY == -1) {
          return false;
        }
        mInitialDownY = initialDownY;
        break;

      case MotionEvent.ACTION_MOVE:
        if (mActivePointerId == INVALID_POINTER) {
          return false;
        }

        if (!isEnableSliding) {
          return false;
        }

        final float y = getMotionEventY(ev, mActivePointerId);
        if (y == -1) {
          return false;
        }

        if (y < mInitialDownY) {
          //判断是否是下拉操作
          final float yDiff = mInitialDownY - y;
          if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollDown()) {
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mLastMotionY = mInitialMotionY;
            mIsBeingDragged = true;
          }
        }
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mIsBeingDragged = false;
        mActivePointerId = INVALID_POINTER;
        break;
    }

    return mIsBeingDragged;
  }

  private float getMotionEventY(MotionEvent ev, int activePointerId) {
    final int index = ev.findPointerIndex(activePointerId);
    if (index < 0) {
      return -1;
    }
    return ev.getY(index);
  }

  /**
   * 判断View是否可以下拉
   *
   * @return canChildScrollDown
   */
  public boolean canChildScrollDown() {
    return mTargetView.canScrollVertically(1);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent event) {
    return super.dispatchTouchEvent(event);
  }

  float delta = 0.0f;

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (mDelegateTouchListener != null && mDelegateTouchListener.onTouch(this, event)) {
      return true;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        delta = 0.0f;
        float movemment = 0.0f;
        if (mSlidingPointerMode == SLIDING_POINTER_MODE_MORE) {
          //homhom:it's different betweenn more than one pointer
          int activePointerId = event.getPointerId(event.getPointerCount() - 1);
          if (mActivePointerId != activePointerId) {
            //change pointer
//                    Log.i("onTouchEvent","change point");
            mActivePointerId = activePointerId;
            mInitialDownY = getMotionEventY(event, mActivePointerId);
            mInitialMotionY = mInitialDownY + mTouchSlop;
            mLastMotionY = mInitialMotionY;
          }

          //pointer delta
//                    delta = getInstrument().getTranslationY(mTargetView)
//                            + ((getMotionEventY(event, mActivePointerId) - mLastMotionY))
//                            / mSlidingOffset;

          delta = getMotionEventY(event, mActivePointerId) - mLastMotionY;

          //滑动阻力计算
//                    float tempOffset = getInstrument().getTranslationY(mTargetView)
//                            + delta;

          float tempOffset = 1 - (Math.abs(getInstrument().getTranslationY(mTargetView)
              + delta) / mTargetView.getMeasuredHeight());

          delta = getInstrument().getTranslationY(mTargetView)
              + delta * mSlidingOffset * tempOffset;

          mLastMotionY = getMotionEventY(event, mActivePointerId);

          //used for judge which side move to
          movemment = getMotionEventY(event, mActivePointerId) - mInitialMotionY;
        } else {
          float tempOffset = 1 - Math.abs(getInstrument().getTranslationY(mTargetView) / mTargetView.getMeasuredHeight());

          delta = (event.getY() - mInitialMotionY) * mSlidingOffset * tempOffset;
          //used for judge which side move to
          movemment = event.getY() - mInitialMotionY;
        }

        float distance = getSlidingDistance();

        switch (mSlidingMode) {
          case SLIDING_MODE_BOTTOM:
            if (movemment <= 0 || distance < 0) {
              //向上滑动
              if (delta > 0) {
                //如果还往下滑，就让它归零
                delta = 0;
              }

              if (delta < mSlidingBottomMaxDistance) {
                delta = mSlidingBottomMaxDistance;
                if (!isNotify) {
                  isNotify = true;
                  //Contexts.shockMethod(getContext(), 30);
                }
              }
              getInstrument().slidingByDelta(mTargetView, delta);
            }
            break;
        }
        if (mSlidingListener != null) {
          mSlidingListener.onStateChanged(STATE_SLIDING);
        }
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        if (mSlidingListener != null) {
          mSlidingListener.onStateChanged(STATE_IDLE);
        }
        if (isNotify) {
          isNotify = false;
          if (delta <= mSlidingBottomMaxDistance) {
            if (mSlidingListener != null) {
              mSlidingListener.onLoadNextVideo();
            }
          }
        }
        getInstrument().reset(mTargetView, RESET_DURATION);
        break;
    }
    //消费触摸
    return true;
  }

  private boolean isNotify = false;

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
  }


  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mTargetView != null) {
      mTargetView.clearAnimation();
    }
    mSlidingMode = 0;
    mTargetView = null;
    mBackgroundView = null;
    mSlidingListener = null;
  }
}