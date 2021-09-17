package com.kuanquan.doyincover;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ChoiceCoverView extends View {
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private RectF rectF;
    private RectF rectF2;
    private int rectWidth;
    private Bitmap bitmap;
    private OnScrollBorderListener onScrollBorderListener;
    private int minPx;

    public ChoiceCoverView(Context context) {
        super(context);
        init();
    }

    public ChoiceCoverView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChoiceCoverView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        int dp5 = (int) getResources().getDimension(R.dimen.dimen_2);
        mPaint.setStrokeWidth(dp5);

        //bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.video_thumbnail);

        rectWidth = (int) getResources().getDimension(R.dimen.dimen_2);
        minPx = (int) getResources().getDimension(R.dimen.dimen_28);
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
                    Log.e("downX", "downX = " + downX + "  left*" + rectF.left + "  right*" + rectF2.right);
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
    protected void onDraw(Canvas canvas) {
        //TODO 动图托框 绘制
        mPaint.setColor(Color.RED);


        canvas.drawLine(rectF.left, rectF.top, rectF.left, rectF.bottom, mPaint);

        canvas.drawLine(rectF2.right, rectF2.top, rectF2.right, rectF2.bottom, mPaint);

        canvas.drawLine(rectF.left, 0, rectF2.right, 0, mPaint);
        canvas.drawLine(rectF.left, mHeight, rectF2.right, mHeight, mPaint);

        /**
         * 添加灰色图层
         */
        mPaint.setColor(Color.parseColor("#99313133"));

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
