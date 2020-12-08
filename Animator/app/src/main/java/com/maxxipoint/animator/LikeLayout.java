//package com.maxxipoint.animator;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.animation.TypeEvaluator;
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.graphics.Matrix;
//import android.graphics.PointF;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.Random;
//
//public class LikeLayout extends FrameLayout {
//
//    ImageView imageView,top,left,right,leftBottom,rightBottom;
//
//    int window_width = 300; // 屏幕宽度
//    int window_height = 300 ; // 屏幕高度
//    int image_width = 60; // 小红心心的宽度
//
//    Drawable icon = getResources().getDrawable(R.mipmap.ic_heart);
//    int mClickCount = 0;     //点击一次是暂停，多次是点赞
//
//    public LikeLayout(@NonNull Context context) {
//        super(context);
//        initView();
//    }
//
//    public LikeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public LikeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView();
//    }
//
//    private void initView() {
//        setClipChildren(false);  // 避免旋转时红心被遮挡
//
//        View viewRoot = LayoutInflater.from(getContext()).inflate(R.layout.animator_layout,this,true);
//        imageView = viewRoot.findViewById(R.id.icon);
//        top = viewRoot.findViewById(R.id.shop); // 顶部的图片控件
//        left = viewRoot.findViewById(R.id.news); // 左边的图片控件
//        right = viewRoot.findViewById(R.id.fans); // 右边的图片控件
//        leftBottom = viewRoot.findViewById(R.id.trails); // 左下角图片控件
//        rightBottom = viewRoot.findViewById(R.id.expression); // 右下角图片控件
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {     // 按下时在Layout中生成红心
//            float x = event.getX();
//            float y = event.getY();
//            mClickCount++;
////            mHandler.removeCallbacksAndMessages(null);
//            if (mClickCount >= 2) { // 双击事件
//                addHeartView(x, y);  // 在Layout中添加红心并，播放消失动画
////                onLikeListener();
////                mHandler.sendEmptyMessageDelayed(1, 500); // 清除所有的 handler 消息
//            } else { // 单击事件
////                mHandler.sendEmptyMessageDelayed(0, 500);
//            }
//
//        }
//        return true;
//    }
//
//    /**
//     * 在Layout中添加红心并，播放消失动画
//     */
//    private void addHeartView(float x, float y) {
//        //计算点击的点位红心的下部中间
//        LayoutParams lp = new LayoutParams(icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
//
//        lp.leftMargin = (int) (x - icon.getIntrinsicWidth() / 2);
//        lp.topMargin = (int) (y - icon.getIntrinsicHeight());
//
//        Log.e("点击左上角X坐标 = ","${lp.leftMargin}");
//        Log.e("点击左上角Y坐标 = ","${lp.topMargin}");
//        Log.e("点击图片的宽 = ","${icon.intrinsicWidth}");
//        Log.e("点击图片的高 = ","${icon.intrinsicHeight}");
//        window_height = Math.abs(lp.topMargin) + icon.getIntrinsicHeight() * 4;
//        window_width = Math.abs(lp.leftMargin) + icon.getIntrinsicWidth() * 4;
//
//        Log.e("window_width = ","$window_width");
//        Log.e("window_height = ","$window_height");
//
////        val imageView = ImageView(context)
//        imageView.setScaleType(ImageView.ScaleType.MATRIX);  // 使用图像矩阵缩放
//        Matrix matrix = new Matrix();
//        matrix.postRotate(getRandomRotate());      //设置红心的微量偏移
//        imageView.setImageMatrix(matrix);   // 给 ImageView 设置矩阵
//        imageView.setImageDrawable(icon);  // 给 ImageView 设置展示的图片
//        imageView.setLayoutParams(lp);
//
//        // 这部分代码可使 下层的 5个图片控件跟着心形的坐标走
//        LayoutParams lpView = new LayoutParams(0, 0);   //计算点击的点位红心的下部中间
//        lpView.leftMargin = (int) (x - icon.getIntrinsicWidth() / 2);
//        lpView.topMargin = (int) (y - icon.getIntrinsicHeight());
//        top.setLayoutParams(lp);
//        left.setLayoutParams(lp);
//        right.setLayoutParams(lp);
//        leftBottom.setLayoutParams(lp);
//        rightBottom.setLayoutParams(lp);
////        addView(imageView)
//
//        AnimatorSet animSet = getShowAnimSet(imageView);
//        final AnimatorSet hideSet = getHideAnimSet(imageView);
//        animSet.start();
//        animSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                hideSet.start();
//            }
//        });
//        hideSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                top.setAlpha(0f);
//                left.setAlpha(0f);
//                right.setAlpha(0f);
//                leftBottom.setAlpha(0f);
//                rightBottom.setAlpha(0f);
//            }
//        });
//    }
//
//    /**
//     * 刚点击的时候的一个缩放效果
//     */
//    private AnimatorSet getShowAnimSet(ImageView view) {
//        // 缩放动画
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.playTogether(scaleX, scaleY);
//        animSet.setDuration(300);
//        return animSet;
//    }
//
//    /**
//     * 缩放结束后到红心消失的效果
//     */
//    private AnimatorSet getHideAnimSet(ImageView view) {
//        // 1.alpha动画
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.0f);
//        // 2.缩放动画
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 2f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2f);
//        // 3.translation动画
//        ObjectAnimator translation = ObjectAnimator.ofFloat(view, "translationY", 0f, -150f);
//
//        // 底下动画开始
//        top.setAlpha(1f);
//        left.setAlpha(1f);
//        right.setAlpha(1f);
//        leftBottom.setAlpha(1f);
//        rightBottom.setAlpha(1f);
//
//        int startangle = 54; // 角度 方向 +1 正方向，-1 负方向。
//
//        // Math.PI 圆周率 3.141592653589793
//        // 圆周率乘以72°再除以180°是:  Math.sin(72 * Math.PI / 180
//        // 是72角对应的弧度值
//
//        int length = (int) ((window_width
//                / (Math.sin(72 * Math.PI / 180)) - image_width) / 2 - image_width / 5);
//
//        // 右下角图片设置动画
//        ValueAnimator expressionanim = zhixian(rightBottom, startangle,
//                length);
//        // 左下角图片设置动画
//        ValueAnimator trailsanim = zhixian(leftBottom, startangle + 72,
//                length);
//        // 左边图片设置动画
//        ValueAnimator newsanim = zhixian(left, startangle + (72 * 2),
//                length);
//        // 顶部图片设置动画
//        ValueAnimator shopanim = zhixian(top, startangle + (72 * 3),
//                length);
//        // 右边图片设置动画
//        ValueAnimator fansanim = zhixian(right, startangle + (72 * 4),
//                length);
//
//        // 底下动画 end
//
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.playTogether(alpha, scaleX, scaleY, translation,shopanim, fansanim,
//                newsanim, expressionanim, trailsanim);
//        animSet.setDuration(500);
//        return animSet;
//    }
//
//    /**
//     * 沿直线运动。
//     *
//     * @param view  要移动的对象。
//     * @param angle  方向 +1 正方向，-1 负方向。
//     * @param length 移动的距离
//     */
//    public ValueAnimator zhixian(final View view, final int angle, final int length) {
//        ValueAnimator valueAnimator = new ValueAnimator();
//        valueAnimator.setObjectValues(new PointF(0, 0));
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
//            // fraction = t / duration
//            @Override
//            public PointF evaluate(float fraction, PointF startValue,
//                                   PointF endValue) {
//                Log.v("znz", "znz ---> " + fraction);
//                PointF point = new PointF();
//                point.x = (float) (fraction * length * Math.cos(angle * Math.PI
//                        / 180));
//                point.y = (float) (fraction * length * Math.sin(angle * Math.PI
//                        / 180));
//                return point;
//            }
//        });
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                PointF point = (PointF) animation.getAnimatedValue();
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                        image_width, image_width);
//                params.leftMargin = (int) point.x
//                        + (window_width / 2 - image_width / 2); // Your
//                // coordinate
//                params.topMargin = (int) point.y
//                        + (window_height / 2 - image_width / 2); // Your Y
//                // coordinate
//                Log.v("znz", "point.x ---> " + point.x);
//                Log.v("znz", "point.y ---> " + point.y);
//                view.setLayoutParams(params);
//            }
//        });
//        return valueAnimator;
//    }
//
//    /**
//     * 生成一个随机的左右偏移量
//     */
//    private float getRandomRotate() {
//        return (float) (new Random().nextInt(40) - 10);
//    }
//}
