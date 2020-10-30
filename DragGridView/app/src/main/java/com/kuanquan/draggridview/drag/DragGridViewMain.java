package com.kuanquan.draggridview.drag;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.kuanquan.draggridview.R;
import com.kuanquan.draggridview.widget.GridViewForScrollView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class DragGridViewMain extends GridViewForScrollView {
    private static final int MOVE_DURATION = 300;
    private static final int SCROLL_SPEED = 60;
    private static final int EXTEND_LENGTH = 20; // 开始拖拽 图片放大的距离

    private int lastX = -1;
    private int lastY = -1;

    private boolean mIsInside = false;
    private float mInsideScale = 0.86f;
    private float mScale = 1.2f;
    private float mMoveScale = mScale;
    private View delArea;
    private int delPosition;

    /**
     * 拖动时的图像 和 它的位置
     */
    private BitmapDrawable hoverCell;
    private Rect currentRect;

    /**
     * 要拖动的view
     */
    private View selectView;

    private int originPosition = INVALID_POSITION;
    private int currentPosition = INVALID_POSITION;

    private boolean isEdit;
    public static boolean isDrag;  // true 表示在拖拽
    private boolean isSwap;  // true 表示 itemView 在交换位置在滑动
    private boolean isUp;  // true 表示 手指放开

    private DragCallback dragCallback;

    public DragGridViewMain(Context context) {
        this(context, null);
    }

    public DragGridViewMain(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridViewMain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragAdapterInterface getInterface() {
        return (DragAdapterInterface) getAdapter();
    }

    public void setDragCallback(DragCallback dragCallback) {
        this.dragCallback = dragCallback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = true;
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDrag) {
                    int offsetX = x - lastX;
                    int offsetY = y - lastY;

                    lastX = x;
                    lastY = y;

                    currentRect.offset(offsetX, offsetY);
                    if (hoverCell != null) {
                        hoverCell.setBounds(currentRect);
                    }
                    invalidate();
                    if (!isSwap) {
                        swapItems(x, y);
                    }
                    handleScroll();

                    isDeleteArea(selectView, delArea, delPosition, (int) ev.getRawY());
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isUp = false;
                if (isDrag) {
                    endDrag();
                }
                mIsInside = false;
                break;
            default:
        }

        return super.onTouchEvent(ev);
    }

    public void clicked(int position) {
        if (isEdit) {
            isEdit = false;
            return;
        }
        // resumeView();
        Log.i("drag", "点击 Item " + position);
    }

    /**
     * 显示 ItemView
     */
    private void resumeView() {

        if (selectView != null) {
            // selectView.findViewById(R.id.delete_img).setVisibility(INVISIBLE);
            selectView.findViewById(R.id.item_container).setVisibility(VISIBLE);
            selectView.findViewById(R.id.item_container).setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * 开始拖拽的角标
     *
     * @param position
     */
    public void startDrag(int position, View delArea) {
        if (position == INVALID_POSITION) {
            return;
        }

        this.delArea = delArea;
        this.delPosition = position;

        // 恢复之前的图像,改变背景,去除删除按钮
//         resumeView();
        selectView = getChildAt(position - getFirstVisiblePosition());
        if (selectView != null) {
            isDrag = true;
            isEdit = true;

            /**
             * 移动的图像背景要有区别,并显示删除按钮
             */
//             selectView.findViewById(R.id.item_container).setBackgroundColor(Color.parseColor("#f0f0f0"));
//            selectView.findViewById(R.id.delete_img).setVisibility(INVISIBLE);

            originPosition = position;
            currentPosition = position;

            // 获取图像
            hoverCell = getHoverCell(selectView);

            // 设置监听回掉，开始拖拽
            if (dragCallback != null) {
                dragCallback.startDrag(position);
            }

            if (hoverCell != null) {
                // itemView 布局隐藏
                selectView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectView.findViewById(R.id.item_container).setVisibility(INVISIBLE);
                    }
                }, 80);

            }
        }
    }

    /**
     * 是否进入删除区域
     */
    private void isDeleteArea(View itemView, View deleteView,int position, int rawY) {
        if (deleteView != null && itemView != null) {
// 删除区域的宽度
            int delAreaWidth = deleteView.getWidth();
            // 删除区域的高度
            int delAreaHeight = deleteView.getHeight();

            // 获取相对在它父窗口里的坐标 View.getLeft() , View.getTop(), View.getBottom(), View.getRight()
            // View.getLocationInWindow()和 View.getLocationOnScreen()在window占据全部screen时，返回值相同，不同的典型情况是在Dialog中时。
            // 当Dialog出现在屏幕中间时，View.getLocationOnScreen()取得的值要比View.getLocationInWindow()取得的值要大
            int[] delLocation = new int[2];
            // 获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
//        delArea.getLocationOnScreen(delLocation);
            // 获取在当前窗口内的绝对坐标
            deleteView.getLocationInWindow(delLocation);
            int delAreaX = delLocation[0];
            int delAreaY = delLocation[1];

            int itemWidth = itemView.getWidth();
            int itemHeight = itemView.getHeight();

            int[] itemLocation = new int[2];
            itemView.getLocationInWindow(itemLocation);
            int itemX = itemLocation[0];
            int itemY = itemLocation[1];

            // Log.d("jiabin","itemWidth:" + itemWidth + " | itemHeight:" + itemHeight + " | itemX:" + itemX + " | itemY:" + itemY);

//            int scaleItemWidth = (int) (itemWidth * mMoveScale);
//            int scaleItemHeight = (int) (itemHeight * mMoveScale);

            int centerX = itemWidth / 2;
            int centerY = itemHeight / 2;

            boolean isInside = false;
//            if (centerY > delAreaY && centerY < delAreaY + delAreaHeight && centerX > delAreaX && centerX < delAreaX + delAreaWidth) {
//            Log.e("ItemTouchHelp", "itemY = " + itemY);
//            Log.e("ItemTouchHelp", "rawY = " + rawY);
//            Log.e("ItemTouchHelp", "delAreaY = " + delAreaY);
            if (rawY > delAreaY) {
                isInside = true;
//                Logger.e("进入删除区域 = "+ position);
//                Log.e("ItemTouchHelp", "进入删除区域");
            } else {
//                Logger.e("非删除区域 = "+ position);
//                Log.e("ItemTouchHelp", "非删除区域");
                isInside = false;
                mIsInside = false;
            }
            Log.e("ItemTouchHelp", "mIsInside = " + mIsInside);
            Log.e("ItemTouchHelp", "isInside = " + isInside);
            if (isInside != mIsInside) {
                if (isInside) {
                    Log.e("ItemTouchHelp", "mInsideScale" + mInsideScale);
                    mMoveScale = mInsideScale;

                } else {
                    Log.e("ItemTouchHelp", "mScale" + mScale);
                    mMoveScale = mScale;
                }

                if (dragCallback != null) {
                    if (isUp) {
//                        itemView.setVisibility(View.GONE);
                    }
                    dragCallback.isDelete(position, isUp);
                }
                mIsInside = isInside;
            }
        }
    }

    // 交换数据
    private void swapItems(int x, int y) {
        int endPosition = pointToPosition(x, y);

        if (endPosition != INVALID_POSITION && endPosition != currentPosition) {
            isSwap = true;
            isEdit = false;
            resumeView();

            // 交换数据内容
            getInterface().reOrder(currentPosition, endPosition);

            selectView = getChildAt(endPosition - getFirstVisiblePosition());
            selectView.findViewById(R.id.item_container).setVisibility(INVISIBLE);
            selectView.findViewById(R.id.item_container).setBackgroundColor(Color.parseColor("#f0f0f0"));
//            selectView.findViewById(R.id.delete_img).setVisibility(VISIBLE);

            // 动画显示交换过程
            animateSwap(endPosition);

        }
    }

    // 动画显示交换过程
    private void animateSwap(int endPosition) {
        List<Animator> animators = new ArrayList<>();
        if (endPosition < currentPosition) {
            for (int i = endPosition + 1; i <= currentPosition; i++) {
                View view = getChildAt(i - getFirstVisiblePosition());
                if (i % getNumColumns() == 0) {
                    animators.add(createTranslationAnimations(view, view.getWidth() * (getNumColumns() - 1), 0,
                            -view.getHeight(), 0));
                } else {
                    animators.add(createTranslationAnimations(view, -view.getWidth(), 0, 0, 0));
                }
            }
        } else {
            for (int i = currentPosition; i < endPosition; i++) {
                View view = getChildAt(i - getFirstVisiblePosition());
                if ((i + 1) % getNumColumns() == 0) {
                    animators.add(createTranslationAnimations(view, -view.getWidth() * (getNumColumns() - 1), 0,
                            view.getHeight(), 0));
                } else {
                    animators.add(createTranslationAnimations(view, view.getWidth(), 0, 0, 0));
                }
            }
        }

        currentPosition = endPosition;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.setDuration(MOVE_DURATION);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSwap = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    // 创建平移动画
    private Animator createTranslationAnimations(View view, float startX, float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    // 拖拽结束
    private void endDrag() {
        currentRect.set(selectView.getLeft(), selectView.getTop(), selectView.getRight(), selectView.getBottom());
        animateBound();
    }

    private void animateBound() {
        TypeEvaluator<Rect> evaluator = new TypeEvaluator<Rect>() {
            @Override
            public Rect evaluate(float fraction, Rect startValue, Rect endValue) {
                return new Rect(interpolate(startValue.left, endValue.left, fraction),
                        interpolate(startValue.top, endValue.top, fraction),
                        interpolate(startValue.right, endValue.right, fraction),
                        interpolate(startValue.bottom, endValue.bottom, fraction));
            }

            public int interpolate(int start, int end, float fraction) {
                return (int) (start + fraction * (end - start));
            }

        };

        ObjectAnimator animator = ObjectAnimator.ofObject(hoverCell, "bounds", evaluator, currentRect);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDrag = false;

                if (currentPosition != originPosition) {
                    resumeView();
                    originPosition = currentPosition;
                }

                hoverCell = null;
                selectView.findViewById(R.id.item_container).setVisibility(VISIBLE);

                if (dragCallback != null) {
                    dragCallback.endDrag(currentPosition);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    /**
     * 获取 View 上的 BitmapDrawable
     *
     * @param view
     * @return
     */
    private BitmapDrawable getHoverCell(View view) {
        int left = view.getLeft();
        int top = view.getTop();
        int w = view.getWidth();
        int h = view.getHeight();

        Bitmap bitmap = getBitmapFromView(view);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        currentRect = new Rect(left - EXTEND_LENGTH, top - EXTEND_LENGTH, left + w + EXTEND_LENGTH,
                top + h + EXTEND_LENGTH);

        drawable.setBounds(currentRect);
        return drawable;
    }

    /**
     * 获取 View 上的 Bitmap
     *
     * @param v
     * @return
     */
    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void handleScroll() {
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeHorizontalScrollRange();
        if (currentRect.top <= 0 && offset > 0) {
            smoothScrollBy(-SCROLL_SPEED, 0);
        } else if (currentRect.bottom >= height && (offset + extent) < range) {
            smoothScrollBy(SCROLL_SPEED, 0);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (hoverCell != null) {
            hoverCell.draw(canvas);
        }
    }

}
