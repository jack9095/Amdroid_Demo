package com.kuanquan.annotationandeventbusdemo.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class CustomView extends View {

    Paint paint;
    private int color;
    private float radius;


    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
//        color = typedArray.getColor(R.styleable.CustomView_circle_color, Color.RED);
//        radius = typedArray.getDimension(R.styleable.CustomView_circle_radius, 0);
//        typedArray.recycle();
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
//        color = typedArray.getColor(R.styleable.CustomView_circle_color, Color.RED);
//        radius = typedArray.getDimension(R.styleable.CustomView_circle_radius, 0);
//        typedArray.recycle();
        init();
    }

    private void init(){
//        setOnTouchListener(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    float lastX = 0f;
    float lastY = 0f;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastX = event.getRawX();
//                lastY = event.getRawY();
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                // 每次移动的距离
//                float deltaX = event.getRawX() - lastX;
//                float deltaY = event.getRawY() - lastY;
//
//                // 控件要移动的位置
//                float nextX = event.getX() + deltaX;
//                float nextY = event.getY() + deltaY;
//
////                    PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", x, getWidth() + deltaX);
////                    PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", y, getHeight() + deltaY);
////                    ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, translationX, translationY);
////                    objectAnimator.setDuration(800);
////                    objectAnimator.start();
//
//                ObjectAnimator animatorX =
//                        ObjectAnimator.ofFloat(this, "x", event.getX(), nextX);
//                ObjectAnimator animatorY =
//                        ObjectAnimator.ofFloat(this, "y", event.getY(), nextY);
//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.playTogether(animatorX,animatorY);//一起开始动画
//                animatorSet.setDuration(0);
//                animatorSet.start();
//
//                lastX = event.getRawX();
//                lastY = event.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
        return super.onTouchEvent(event);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    public void setWillNotDraw(boolean willNotDraw) {
        super.setWillNotDraw(true); // 设置为 true 表示这个 View 不需要绘制任何内容，系统默认为 false
    }


    // 当包含此 View 的 Activity 退出或者，View 被移除时调用此方法
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    // 当包含此 View 的 Activity 启动时，调用此方法
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    // 自定义继承 View 的控件，必须要处理 wrap_content,不处理 wrap_content 就是 match_parent
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(200),dp2px(200));
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(200),dp2px(200));
        }

    }


    // 接下来解决 padding 的问题
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制的时候是已经测量和布局好了，可以直接获取到 View 四周的空白也就是 padding 了
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

//        Log.e("CustomView","半径 = " + radius);
        if (radius <= 0) {
            radius = Math.min(width,height) / 2;
        }

        // 绘制一个圆
        canvas.drawCircle(paddingLeft + width/2,paddingTop + height/2,radius,paint);
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
