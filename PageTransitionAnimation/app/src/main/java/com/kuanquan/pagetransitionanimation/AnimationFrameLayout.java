package com.kuanquan.pagetransitionanimation;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 父控件的拉动滑动动画，更易于集成
 */
public class AnimationFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    public static final String TAG = AnimationFrameLayout.class.getSimpleName();
    //退出进度
    private float DEFAULT_EXIT_SCALE = 0.5f;
    //当前自定义FrameLayout
    private FrameLayout frameLayout;
    //FrameLayout的第一个子view
    private View parent;
    private GestureDetector mGestureDetector;
    //触摸退出进度
    private float mExitScalingRef;
    //子view的高度
    private int viewHeight;
    //结束的监听器
    private FinishListener finishListener;

    public final int DRAG_GAP_PX = dp2px(getContext(),8f);
    public static final int STATUS_NORMAL = 0;//正常浏览状态
    public static final int STATUS_MOVING = 1;//滑动状态
    public static final int STATUS_RESETTING = 2;//返回中状态
    private int currentStatus = STATUS_NORMAL;

    public void setDefaultExitScale(float defaultExitScale) {
        DEFAULT_EXIT_SCALE = defaultExitScale;
    }

    TypeEvaluator<Integer> mColorEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startColor = startValue;
            int endColor = endValue;

            int alpha = (int) (Color.alpha(startColor) + fraction * (Color.alpha(endColor) - Color.alpha(startColor)));
            int red = (int) (Color.red(startColor) + fraction * (Color.red(endColor) - Color.red(startColor)));
            int green = (int) (Color.green(startColor) + fraction * (Color.green(endColor) - Color.green(startColor)));
            int blue = (int) (Color.blue(startColor) + fraction * (Color.blue(endColor) - Color.blue(startColor)));
            return Color.argb(alpha, red, green, blue);
        }
    };

    public AnimationFrameLayout(Context context) {
        this(context, null);
    }

    public AnimationFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        frameLayout = this;
        mGestureDetector = new GestureDetector(context, this);
    }

    public void setFinishListener(FinishListener finishListener) {
        this.finishListener = finishListener;
    }

    int deltaY; // 垂直移动的距离
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e(TAG, "按下");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.e(TAG, "放开");
            if (currentStatus != STATUS_MOVING) {
                return super.onTouchEvent(event);
            }
            if (mExitScalingRef < DEFAULT_EXIT_SCALE) {
                //缩小到一定的程度，将其关闭
                if (finishListener != null) {
                    currentStatus = STATUS_NORMAL;
                    finishListener.finish();
                }
            } else {
                currentStatus = STATUS_RESETTING;
                //如果拉动距离不到某个角度，则将其动画返回原位置
                final float moveX = parent.getTranslationX();
                final float moveY = parent.getTranslationY();
                final float scaleX = parent.getScaleX();
                final float scaleY = parent.getScaleY();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500);
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        currentStatus = STATUS_MOVING;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        currentStatus = STATUS_NORMAL;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float p = (float) animation.getAnimatedValue();
                        float animationMoveX = moveX + (0 - moveX) * p;
                        parent.setTranslationX(animationMoveX);
                        parent.setTranslationY(moveY + (0 - moveY) * p);
                        parent.setScaleX(scaleX + (1 - scaleX) * p);
                        parent.setScaleY(scaleY + (1 - scaleY) * p);
                    }
                });
                valueAnimator.start();
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.e(TAG, "移动");
            deltaY = (int) (event.getRawY() - mDownY);
            //手指往上滑动
//            if (deltaY <= DRAG_GAP_PX && currentStatus != STATUS_MOVING) {
//                Log.e(TAG, "currentStatus = " + currentStatus);
//                return super.onTouchEvent(event);
//            }
        }

        return mGestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
    }

    private float mDownX;
    private float mDownY;
    @Override
    public boolean onDown(MotionEvent e) {
        //必须要返回true，否则onScroll将不会被回调
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (parent == null) {
            parent = getChildAt(0);
        }
        if (viewHeight == 0) {
            viewHeight = parent.getHeight();
        }
        float moveX = e2.getX() - e1.getX();
        float moveY = e2.getY() - e1.getY();

        Log.e("moveX = ", moveX + "");
        Log.e("moveY = ", moveY + "");

        mExitScalingRef = 1;

        //viewpager2 或者 RecyclerView(水平) 不在切换中，并且手指往下滑动，开始缩放
        if (deltaY > DRAG_GAP_PX || currentStatus == STATUS_MOVING) {
            mExitScalingRef = mExitScalingRef - moveY / viewHeight;
            parent.setTranslationX(moveX);
            parent.setTranslationY(moveY);
            parent.setScaleX(mExitScalingRef);
            parent.setScaleY(mExitScalingRef);
            currentStatus = STATUS_MOVING;
            if (mExitScalingRef > 1) {
                //当用户往上滑动的时候
                frameLayout.setBackgroundColor(mColorEvaluator.evaluate(2 - mExitScalingRef, 0x00000000, 0xFF000000));
            } else {
                //当用户往下滑动的时候
                frameLayout.setBackgroundColor(mColorEvaluator.evaluate(mExitScalingRef, 0x00000000, 0xFF000000));
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface FinishListener {
        void finish();
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        try {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mDownX = ev.getRawX();
//                    mDownY = ev.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int deltaX = Math.abs((int) (ev.getRawX() - mDownX));
//                    int deltaY = (int) (ev.getRawY() - mDownY);
//                    if (deltaY > DRAG_GAP_PX && deltaX <= DRAG_GAP_PX) {
//                        return true;
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    break;
//                default:
//                    break;
//            }
//            return super.onInterceptTouchEvent(ev);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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
//                if (deltaY > DRAG_GAP_PX && deltaX <= DRAG_GAP_PX) {
//                    return true;
//                }
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
        if (mDownX < 0) {//在滚动状态
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

//        return interceptX || interceptY;
        return interceptY;
    }

    private int dp2px(Context context, float dp) {
        if (context == null) {
            return (int) (dp * 3);
        }
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return Math.round(px);
    }
}
