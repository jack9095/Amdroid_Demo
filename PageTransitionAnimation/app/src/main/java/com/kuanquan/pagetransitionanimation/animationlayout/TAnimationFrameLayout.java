package com.kuanquan.pagetransitionanimation.animationlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;

/**
 * 父控件的拉动滑动动画，更易于集成
 */
public class TAnimationFrameLayout extends FrameLayout {
    public final int STATUS_NORMAL = 0;//正常浏览状态
    public final int STATUS_MOVING = 1;//滑动状态
    public final int STATUS_RESETTING = 2;//返回中状态

    public final float MIN_SCALE_SIZE = 0.3f;//最小缩放比例
    public final int BACK_DURATION = 300;//ms
    //小于该值认定为点击事件，不滑动，不做拦截
    private int DRAG_GAP_PX = dp2px(getContext(),8);

    private int currentStatus = STATUS_NORMAL;

    private float mDownX;
    private float mDownY;
    private float screenHeight;
    private float screenWidth;

    /**
     * 要缩放的View
     */
    private View currentShowView;
    /**
     * 滑动速度检测类
     */
    private VelocityTracker mVelocityTracker;
    private IAnimListener listener;
    //是否拦截事件
    private boolean imageState = false;
    private boolean scrollState = false;

    public void setIAnimClose(IAnimListener listener) {
        this.listener = listener;
    }

    public TAnimationFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public TAnimationFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        screenHeight = getScreenHeight(context);
        screenWidth = getScreenWidth(context);
    }

    public void setCurrentShowView(View currentShowView) {
        this.currentShowView = currentShowView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (currentShowView == null) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (currentStatus == STATUS_RESETTING) {//修复拖动松手后再快速拖拽导致位置偏移错误的问题
                    mDownX = -1;
                    mDownY = -1;
                    return false;
                }
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = ev.getRawY() - mDownY;
                float deltaX = ev.getRawX() - mDownX;
                if (interceptMoveEvent(deltaX, deltaY)) {
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean interceptMoveEvent(float deltaX, float deltaY) {
        if (scrollState || mDownX < 0) {//在滚动状态
            return false;
        }
        if (currentStatus == STATUS_RESETTING) {
            return false;
        }
        if (deltaX < DRAG_GAP_PX && deltaY < DRAG_GAP_PX) {
            return false;
        }
        boolean interceptX = deltaX > 0 && Math.abs(deltaX) >= Math.abs(deltaY);
        boolean interceptY = deltaY > 0 && Math.abs(deltaY) >= Math.abs(deltaX);
        if (imageState) {
            return interceptY;
        }
        return interceptX || interceptY;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (currentShowView == null) {
            return super.onTouchEvent(ev);
        }
        //System.out.println("AnimationFrameLayout onTouchEvent currentStatus:" + currentStatus);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                addIntoVelocity(ev);
                float deltaX = ev.getRawX() - mDownX;
                float deltaY = ev.getRawY() - mDownY;
                if (deltaX > DRAG_GAP_PX || deltaY > DRAG_GAP_PX) {
                    if (listener != null) {
                        listener.onMoveStart();
                    }
                }
                currentStatus = STATUS_MOVING;
                moveView(ev.getRawX() - mDownX, ev.getRawY() - mDownY);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (currentStatus != STATUS_MOVING) {
                    //System.out.println("AnimationFrameLayout super.onTouchEvent:" + ev.getActionMasked());
                    return super.onTouchEvent(ev);
                }
                final float mUpX = ev.getRawX();
                final float mUpY = ev.getRawY();

                float vY = computeYVelocity();//松开时必须释放VelocityTracker资源
                if (vY >= 1200 || (mUpY - mDownY) >= screenHeight / 6 || (mUpX - mDownX) >= screenWidth / 6) {
                    //System.out.println("AnimationFrameLayout onTouchEvent:" + ev.getActionMasked());

                    //下滑速度快，或者滑动距离超过屏幕宽/高度的1/6
                    setAlphaBackground(Color.TRANSPARENT);
                    if (listener != null) {
                        listener.onRelease(currentShowView);
                    }
                } else {
                    resetReviewState(mUpX, mUpY);
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //返回浏览状态
    private void resetReviewState(final float mUpX, final float mUpY) {
        currentStatus = STATUS_RESETTING;
        //System.out.println(String.format("AnimationFrameLayout resetReviewState mUpY:%s, mDownY:%s, mUpX:%s, mDownX:%s, currentStatus:%s", mUpY, mDownY, mUpX, mDownX, currentStatus));

        final float downX = this.mDownX;
        final float downY = this.mDownY;
        ValueAnimator valueAnimator = null;
        if (mUpY != downY) {
            valueAnimator = ValueAnimator.ofFloat(mUpY, downY);
            valueAnimator.addUpdateListener(animation -> {
                float mY = (float) animation.getAnimatedValue();
                float percent = (mY - downY) / (mUpY - downY);
                float mX = percent * (mUpX - downX) + downX;
                moveView(mX - downX, mY - downY);
            });
        } else if (mUpX != downX) {
            valueAnimator = ValueAnimator.ofFloat(mUpX, downX);
            valueAnimator.addUpdateListener(animation -> {
                float mX = (float) animation.getAnimatedValue();
                float percent = (mX - downX) / (mUpX - downX);
                float mY = percent * (mUpY - downY) + downY;
                moveView(mX - downX, mY - downY);
            });
        }
        if (null != valueAnimator) {
            valueAnimator.addListener(new SAnimatorListener() {
                @Override
                public void onAnimationEnd(@NotNull Animator animation) {
                    //System.out.println("AnimationFrameLayout onAnimationEnd");
                    currentStatus = STATUS_NORMAL;
                    if (listener != null) {
                        listener.onMoveEnd();
                    }
                }
            });
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.start();
        }
    }


    //移动View
    private void moveView(float deltaX, float deltaY) {
        //System.out.println("AnimationFrameLayout moveView currentStatus:" + currentStatus);
        float scale = 1f;
        float alphaPercent = 1f;
        if (deltaY > 0) {
            scale = 1 - Math.abs(deltaY) / screenHeight;
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
        }

        currentShowView.setTranslationX(deltaX);
        currentShowView.setTranslationY(deltaY);
        scaleView(scale);
        setAlphaBackground(getBlackAlpha(alphaPercent));
    }

    private void setAlphaBackground(int color) {
        if (currentShowView != null && currentShowView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentShowView.getParent();
            parent.setBackgroundColor(color);
        } else {
            setBackgroundColor(color);
        }
    }

    //缩放View
    private void scaleView(float scale) {
        scale = Math.min(Math.max(scale, MIN_SCALE_SIZE), 1);
        currentShowView.setScaleX(scale);
        currentShowView.setScaleY(scale);
    }

    private int getBlackAlpha(float percent) {
        percent = Math.min(1, Math.max(0, percent));
        int intAlpha = (int) (percent * 255);
        return Color.argb(intAlpha, 0, 0, 0);
    }

    private void addIntoVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }


    private float computeYVelocity() {
        float result = 0;
        if (mVelocityTracker != null) {
            mVelocityTracker.computeCurrentVelocity(1000);
            result = mVelocityTracker.getYVelocity();
            releaseVelocity();
        }
        return result;
    }

    private void releaseVelocity() {
        //System.out.println("AnimationFrameLayout releaseVelocity");
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setImageState(boolean imageState) {
        this.imageState = imageState;
    }

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
        if (scrollState && mDownX > 0) {
            mDownX = -1;
            mDownY = -1;
        }
    }

    public interface IAnimListener {

        void onRelease(View view);

        void onMoveStart();

        void onMoveEnd();
    }

    /**
     * 获取屏幕宽度
     */
    private int getScreenWidth(Context context) {
        return ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getWidth();
    }

    /**
     * 获取屏幕宽度
     */
    private int getScreenHeight(Context context) {
        return ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
    }

    private int dp2px(Context context, float dp) {
        if (context == null) {
            return (int) (dp * 3);
        }
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(px);
    }
}
