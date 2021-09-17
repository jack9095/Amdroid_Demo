package com.kuanquan.doyincover.publisher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kuanquan.doyincover.utils.Contexts;
import com.qiniu.pili.droid.shortvideo.PLMediaFile;
import com.qiniu.pili.droid.shortvideo.PLVideoFrame;
import com.shuashuakan.android.R;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/18
 * Description:
 */
public class ChoiceCover extends LinearLayout {
  private static final int SLICE_COUNT = 6;
  private long mDurationMs;
  private int mWidth;
  private int mHeight;
  private Paint mPaint;
  private RectF rectF;
  private RectF rectF2;
  private int rectWidth;
  private PLMediaFile mMediaFile;
  private String mVideoPath;
  private OnScrollBorderListener onScrollBorderListener;
  private int minPx;

  public ChoiceCover(Context context) {
    super(context);
    init();
  }

  public ChoiceCover(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ChoiceCover(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    setOrientation(HORIZONTAL);

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setStrokeWidth(Contexts.dip(getContext(), 2f));

    //bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.video_thumbnail);

    rectWidth = Contexts.dip(getContext(), 25f);
    minPx = Contexts.dip(getContext(), 0f);
  }

  public void setVideoPath(String path) {
    mVideoPath = path;
    mMediaFile = new PLMediaFile(mVideoPath);
    mDurationMs = mMediaFile.getDurationMs();
    initVideo();
  }

  @SuppressLint("StaticFieldLeak")
  public void initVideo() {
    final int sliceEdge = (Contexts.getScreenSize(getContext()).x - Contexts.dip(getContext(), 30f)) / SLICE_COUNT;

    new AsyncTask<Void, PLVideoFrame, Void>() {
      @Override
      protected Void doInBackground(Void... v) {
        for (int i = 0; i < SLICE_COUNT; ++i) {
          PLVideoFrame frame = mMediaFile.getVideoFrameByTime((long) ((1.0f * i / SLICE_COUNT) * mDurationMs), true, sliceEdge, Contexts.dip(getContext(), 60f));
          publishProgress(frame);
        }
        return null;
      }

      @Override
      protected void onProgressUpdate(PLVideoFrame... values) {
        super.onProgressUpdate(values);
        PLVideoFrame frame = values[0];
        if (frame != null) {
          View root = LayoutInflater.from(getContext()).inflate(R.layout.frame_item, null);

          ImageView thumbnail = root.findViewById(R.id.thumbnail);
          thumbnail.setImageBitmap(frame.toBitmap());

          LayoutParams rootLP = new LayoutParams(sliceEdge, sliceEdge);
          addView(root, rootLP);
        }
      }
    }.execute();
  }


  public void setMinInterval(int minPx) {
    if (mWidth > 0 && minPx > mWidth) {
      minPx = mWidth;
    }
    this.minPx = minPx;
  }

  public interface OnScrollBorderListener {
    void OnScrollBorder(float start, float end);

    void onScrollStateChange();
  }

  public void setOnScrollBorderListener(OnScrollBorderListener listener) {
    this.onScrollBorderListener = listener;
  }

  public float getLeftInterval() {
    return rectF.left;
  }

  public float getRightInterval() {
    return rectF2.right;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    if (mWidth == 0) {
      mWidth = getWidth();
      mHeight = getHeight();

      rectF = new RectF();
      rectF.left = 0;
      rectF.top = 0;
      rectF.right = rectWidth;
      rectF.bottom = mHeight;

      rectF2 = new RectF();
      rectF2.left = minPx + rectWidth;
      rectF2.top = 0;
      rectF2.right = (rectWidth * 2) + minPx;
      rectF2.bottom = mHeight;

    }
  }

  private float downX;
  private boolean isScroll;


  @Override
  public boolean onTouchEvent(MotionEvent event) {
    move(event);
    return isScroll;
  }

  boolean scrollChange;

  private boolean move(MotionEvent event) {

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        downX = event.getX();


        if (downX > rectF.left && downX < rectF2.right) {
          isScroll = true;
        } else {
          rectF.left = (downX - minPx / 2) - rectWidth;
          rectF.right = downX - minPx / 2;
          rectF2.left = downX + minPx / 2;
          rectF2.right = (downX + minPx / 2) + rectWidth;

          //滑动到左边最大值
          if (rectF.left < 0) {
            rectF.left = 0;
            rectF.right = rectWidth;
            rectF2.left = minPx + rectWidth;
            rectF2.right = (rectWidth * 2) + minPx;

          }

          //滑动至右边最大值
          if (rectF2.right > mWidth) {
            rectF2.right = mWidth;
            rectF2.left = mWidth - rectWidth;
            rectF.right = mWidth - minPx - rectWidth;
            rectF.left = mWidth - minPx - (rectWidth * 2);
          }
          isScroll = true;
          invalidate();

          if (onScrollBorderListener != null) {
            onScrollBorderListener.OnScrollBorder(rectF.left, rectF2.right);
          }

        }

        break;
      case MotionEvent.ACTION_MOVE:

        float moveX = event.getX();

        float scrollX = moveX - downX;
        if (isScroll) {
          rectF.left = rectF.left + scrollX;
          rectF.right = rectF.right + scrollX;

          rectF2.left = rectF2.left + scrollX;
          rectF2.right = rectF2.right + scrollX;
          //滑动到左边最大值
          if (rectF.left < 0) {
            rectF.left = 0;
            rectF.right = rectWidth;
            rectF2.left = minPx + rectWidth;
            rectF2.right = (rectWidth * 2) + minPx;

          }

          //滑动至右边最大值
          if (rectF2.right > mWidth) {
            rectF2.right = mWidth;
            rectF2.left = mWidth - rectWidth;
            rectF.right = mWidth - minPx - rectWidth;
            rectF.left = mWidth - minPx - (rectWidth * 2);
          }

          scrollChange = true;
          invalidate();

          if (onScrollBorderListener != null) {
            onScrollBorderListener.OnScrollBorder(rectF.left, rectF2.right);// 回调选择的区域
          }
        }


        downX = moveX;
        break;
      case MotionEvent.ACTION_CANCEL:

      case MotionEvent.ACTION_UP:
        downX = 0;
        isScroll = false;
        //invalidate();
        if (onScrollBorderListener != null) {
          onScrollBorderListener.onScrollStateChange();
        }
        scrollChange = false;
        break;
    }
    return true;
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    //TODO 动图托框 绘制
    mPaint.setColor(Color.WHITE);

    canvas.drawLine(rectF.left, rectF.top, rectF.left, rectF.bottom, mPaint);

    canvas.drawLine(rectF2.right, rectF2.top, rectF2.right, rectF2.bottom, mPaint);

    canvas.drawLine(rectF.left, 0, rectF2.right, 0, mPaint);
    canvas.drawLine(rectF.left, mHeight, rectF2.right, mHeight, mPaint);

    /**
     * 添加灰色图层
     */
    mPaint.setColor(ContextCompat.getColor(getContext(),R.color.color_normal_99313133));

    RectF rectF3 = new RectF();
    rectF3.left = 0;
    rectF3.top = 0;
    rectF3.right = rectF.left;
    rectF3.bottom = mHeight;
    canvas.drawRect(rectF3, mPaint);

    RectF rectF4 = new RectF();
    rectF4.left = rectF2.right;
    rectF4.top = 0;
    rectF4.right = mWidth;
    rectF4.bottom = mHeight;
    canvas.drawRect(rectF4, mPaint);
  }

}