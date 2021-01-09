package com.kuanquan.pagetransitionanimation.animationlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * 父控件的拉动滑动动画，更易于集成
 */
public class FAnimationFrameLayout extends FrameLayout {
    public static final int STATUS_NORMAL = 0;//正常浏览状态
    public static final int STATUS_MOVING = 1;//滑动状态
    public static final int STATUS_RESETTING = 2;//返回中状态
    public static final String TAG = "FAnimationFrameLayout";


    public static final float MIN_SCALE_SIZE = 0.3f;//最小缩放比例
    public static final int BACK_DURATION = 300;//ms
    public static final int DRAG_GAP_PX = 50;

    private int currentStatus = STATUS_NORMAL;
    private int currentPageStatus;

    private float mDownX;
    private float mDownY;
    private float screenHeight;

    /**
     * 要缩放的View
     */
    private View currentShowView;
    /**
     * 滑动速度检测类
     */
    private VelocityTracker mVelocityTracker;
    private IAnimClose iAnimClose;
    private IPageChangeListener mOnChangeListener;

    public void setIAnimClose(IAnimClose iAnimClose) {
        this.iAnimClose = iAnimClose;
    }

    public FAnimationFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public FAnimationFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        screenHeight = getScreenHeight(context);
//        setBackgroundColor(Color.BLACK);
//        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (mOnChangeListener != null) {
//                    mOnChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (mOnChangeListener != null) {
//                    mOnChangeListener.onPageSelected(position);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                currentPageStatus = state;
//                if (mOnChangeListener != null) {
//                    mOnChangeListener.onPageScrollStateChanged(state);
//                }
//            }
//        });
    }

    public void setChangeListener(IPageChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public interface IPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    public static class IPageChangeListenerImp implements IPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void setCurrentShowView(View currentShowView) {
        this.currentShowView = currentShowView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (currentShowView != null) {
                Log.e(TAG,"控件不为空");
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = ev.getRawX();
                        mDownY = ev.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaX = Math.abs((int) (ev.getRawX() - mDownX));
                        int deltaY = (int) (ev.getRawY() - mDownY);
                        if (deltaY > DRAG_GAP_PX && deltaX <= DRAG_GAP_PX) {
                            Log.e(TAG,"拦截事件");
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e(TAG,"拦截事件——ACTION_UP");
                        break;
                    default:
                        break;
                }
            } else {
                Log.e(TAG,"控件为空");
            }
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e(TAG,"onTouchEvent");
        if (currentStatus == STATUS_RESETTING) {
            Log.e(TAG,"onTouchEvent_false");
            return false;
        }
        try {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getRawX();
                    mDownY = ev.getRawY();
                    addIntoVelocity(ev);
                    break;
                case MotionEvent.ACTION_MOVE:
                    addIntoVelocity(ev);
                    int deltaY = (int) (ev.getRawY() - mDownY);
                    //手指往上滑动
                    if (deltaY <= DRAG_GAP_PX && currentStatus != STATUS_MOVING) {
                        return super.onTouchEvent(ev);
                    }
                    //viewpager2 或者 RecyclerView(水平) 不在切换中，并且手指往下滑动，开始缩放
//                    if (currentPageStatus != SCROLL_STATE_DRAGGING && (deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING)) {
                    if (deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING) {
                        Log.e(TAG, "moveView");
                        moveView(ev.getRawX(), ev.getRawY());
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (currentStatus != STATUS_MOVING) {
                        return super.onTouchEvent(ev);
                    }
                    final float mUpX = ev.getRawX();
                    final float mUpY = ev.getRawY();

                    float vY = computeYVelocity();//松开时必须释放VelocityTracker资源
                    if (vY >= 1200 || (mUpY - mDownY) > screenHeight / 5) {
                        //下滑速度快，或者下滑距离超过屏幕高度的一半，就关闭 (上滑返回数据)
                        if (iAnimClose != null) {
                            iAnimClose.onPictureRelease(currentShowView);
                        }
                    } else {
                        resetReviewState(mUpX, mUpY);
                    }
                    break;
                default:
                    break;
            }
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //返回浏览状态
    private void resetReviewState(final float mUpX, final float mUpY) {
        currentStatus = STATUS_RESETTING;
        if (mUpY != mDownY) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpY, mDownY);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mY = (float) animation.getAnimatedValue();
                float percent = (mY - mDownY) / (mUpY - mDownY);
                float mX = percent * (mUpX - mDownX) + mDownX;
                moveView(mX, mY);
                if (mY == mDownY) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        } else if (mUpX != mDownX) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(mUpX, mDownX);
            valueAnimator.setDuration(BACK_DURATION);
            valueAnimator.addUpdateListener(animation -> {
                float mX = (float) animation.getAnimatedValue();
                float percent = (mX - mDownX) / (mUpX - mDownX);
                float mY = percent * (mUpY - mDownY) + mDownY;
                moveView(mX, mY);
                if (mX == mDownX) {
                    mDownY = 0;
                    mDownX = 0;
                    currentStatus = STATUS_NORMAL;
                }
            });
            valueAnimator.start();
        } else if (iAnimClose != null) {
            iAnimClose.onPictureClick();
        }
    }


    //移动View
    private void moveView(float movingX, float movingY) {
        currentStatus = STATUS_MOVING;
        float deltaX = movingX - mDownX;
        float deltaY = movingY - mDownY;
        float scale = 1f;
        float alphaPercent = 1f;
        if (deltaY > 0) {
            scale = 1 - Math.abs(deltaY) / screenHeight;
            alphaPercent = 1 - Math.abs(deltaY) / (screenHeight / 2);
        }

        currentShowView.setTranslationX(deltaX);
        currentShowView.setTranslationY(deltaY);
        scaleView(scale);
        setBackgroundColor(getBlackAlpha(alphaPercent));
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
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public interface IAnimClose {
        void onPictureClick();

        void onPictureRelease(View view);
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

/*
    使用
    FAnimationFrameLayout?.run {
            val contentView = activity?.window?.decorView?.findViewById<View>(android.R.id.content)
            setCurrentShowView(contentView)
            setIAnimClose(object : FAnimationFrameLayout.IAnimClose {
                override fun onPictureClick() {
                }

                override fun onPictureRelease(view: View) {
                    onBackPressed()
                }
            })
      }
 */