package com.kuanquan.doyincover.publisher.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.shuashuakan.android.R;

public class LongClickProgressBar extends View {

  private static final float DEFAULT_BREAK_POINT_WIDTH = 1f;
  private static final long DEFAULT_FIRST_POINT_TIME = 1000;
  private static final long DEFAULT_TOTAL_TIME = 10 * 1000;

  private Paint mProgressBarPaint;
  private Paint mFirstPointPaint;

  private float mPixelUnit;

  private float mFirstPointTime = DEFAULT_FIRST_POINT_TIME;
  private float mTotalTime = DEFAULT_TOTAL_TIME;

  private volatile State mCurrentState = State.PAUSE;

  private float mPixelsPerMilliSecond;

  private double mProceedingSpeed = 1;

  private float mProgressWidth;

  private long mLastUpdateTime;

  /**
   * The enum State.
   */
  public enum State {
    /**
     * Start state.
     */
    START,
    /**
     * Pause state.
     */
    PAUSE
  }

  /**
   * Set the progress bar's color.
   */
  public void setBarColor(int color) {
    mProgressBarPaint.setColor(color);
  }

  /**
   * Set the progress bar's proceeding speed.
   */
  public void setProceedingSpeed(double speed) {
    mProceedingSpeed = speed;
  }

  /**
   * Instantiates a new Progress view.
   *
   * @param context the context
   */
  public LongClickProgressBar(Context context) {
    super(context);
    init(context);
  }

  /**
   * Instantiates a new Progress view.
   *
   * @param paramContext      the param context
   * @param paramAttributeSet the param attribute set
   */
  public LongClickProgressBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramContext);

  }

  /**
   * Instantiates a new Progress view.ee
   *
   * @param paramContext      the param context
   * @param paramAttributeSet the param attribute set
   * @param paramInt          the param int
   */
  public LongClickProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }

  private void init(Context paramContext) {
    mProgressBarPaint = new Paint();
    mFirstPointPaint = new Paint();

    setBackgroundColor(ContextCompat.getColor(paramContext,R.color.transparent));

    mProgressBarPaint.setStyle(Paint.Style.FILL);
    mProgressBarPaint.setColor(ContextCompat.getColor(paramContext,R.color.color_ffef30));

    mFirstPointPaint.setStyle(Paint.Style.FILL);
    mFirstPointPaint.setColor(ContextCompat.getColor(paramContext,R.color.white));

    setTotalTime(paramContext, DEFAULT_TOTAL_TIME);
  }

  /**
   * Reset.
   */
  public synchronized void reset() {
    setCurrentState(State.PAUSE);
  }

  /**
   * Sets total time in millisecond
   *
   * @param context     the context
   * @param millisecond the millisecond
   */
  public void setTotalTime(Context context, long millisecond) {
    mTotalTime = millisecond;

    DisplayMetrics dm = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    mPixelUnit = dm.widthPixels / mTotalTime;

    mPixelsPerMilliSecond = mPixelUnit;
  }

  /**
   * Sets current state
   *
   * @param state the state
   */
  public void setCurrentState(State state) {
    mCurrentState = state;
    if (state == State.PAUSE) {
      mProgressWidth = mPixelsPerMilliSecond;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    long curTime = System.currentTimeMillis();

    // redraw all the break point
    int startPoint = 0;

    // increase the progress bar in start state
    if (mCurrentState == State.START) {
      mProgressWidth += mPixelsPerMilliSecond * (curTime - mLastUpdateTime) / mProceedingSpeed;
      if (startPoint + mProgressWidth <= getMeasuredWidth()) {
        canvas.drawRect(startPoint, 0, startPoint + mProgressWidth, getMeasuredHeight(), mProgressBarPaint);
      } else {
        reset();
        onProgressListener.onEnd();
        canvas.drawRect(startPoint, 0, getMeasuredWidth(), getMeasuredHeight(), mProgressBarPaint);
      }
      synchronized (this) {
        // draw the first point
        canvas.drawRect(mPixelUnit * mFirstPointTime, 0, mPixelUnit * mFirstPointTime + DEFAULT_BREAK_POINT_WIDTH, getMeasuredHeight(), mFirstPointPaint);
      }
    }

    mLastUpdateTime = System.currentTimeMillis();

    invalidate();
  }

  private OnProgressListener onProgressListener;

  public void setOnProgressListener(OnProgressListener onProgressListener) {
    this.onProgressListener = onProgressListener;
  }

  public interface OnProgressListener {
    void onEnd();
  }
}