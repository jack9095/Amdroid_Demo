package com.kuanquan.annotationandeventbusdemo.customView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    // new FlowLayout() 的时候使用的
    public FlowLayout(Context context) {
        super(context);
    }

    // 反射属性 FlowLayout在xml中使用的时候，Java 会把xml中布局的属性反射到AttributeSet
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // style 主题，如果APP有切换主题需求，在自定义View中必须实现这个方法
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineCount = allLines.size(); // 获取到总行数
        int curL = 0;
        Log.d("TAG", "onLayout: getLeft(): " + getLeft());
        int curT = 0;
        Log.d("TAG", "onLayout: getTop(): " + getTop());
        for (int i = 0; i < lineCount; i++) {
            // 获取到每一行的 View 数组
            List<View> lineViews = allLines.get(i);

            // 获取到行的高度
            int lineHeight = lineHeights.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                int left = curL;
                int top = curT;
//                int bottom = top + view.getHeight();
//                int right = left + view.getWidth();

                int bottom = top + view.getMeasuredHeight();
                int right = left + view.getMeasuredWidth();
                view.layout(left,top, right,bottom);
                curL = right + mHorizontalSpacing;
            }
            curL = 0;
            curT = curT + lineHeight + mVerticalSpacing;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    // 确定 View 大小的
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int mHorizontalSpacing = dp2px(16); //每个item横向间距
    private int mVerticalSpacing = dp2px(8); //每个item纵向间距

    // 记录所有的行，一行一行的存储
    List<List<View>> allLines = new ArrayList<>();
    List<Integer> lineHeights = new ArrayList<>(); // 记录每一行的行高


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        // 存储每一行的 View
        List<View> lines = new ArrayList<>();
        int lineWidthUSe = 0; // 记录这行使用了多款的size

        // 流失布局

        // 先测量孩子（子View）
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            LayoutParams childLP = childView.getLayoutParams();

            // 1.父控件的  2.父控件的左右内边距 3，子控件的宽度
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLP.width);

            // 1.父控件的  2.父控件的上下内边距 3，子控件的高度
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLP.height);

            // 这里要传入 子View 的真正宽高，在 xml 中的 width height
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);


            // view 是分行layout的，所以要记录每一行有哪些view，这样可以方便layout布局
            lines.add(childView);
        }

        // 再测量自己，思路：把行的宽度测量出来，比较取最大的宽度加上间距，把每行的高度及间距叠加起来获取到总高度
//        setMeasuredDimension();

    }


    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    // 速度追踪
    VelocityTracker velocityTracker = VelocityTracker.obtain();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.computeCurrentVelocity(1000);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float xVelocity = velocityTracker.getXVelocity();
                Log.e("水平滑动速度 = ",xVelocity + "");
                float yVelocity = velocityTracker.getYVelocity();
                Log.e("垂直滑动速度 = ",yVelocity + "");
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }
}
