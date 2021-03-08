package com.kuanquan.customview;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;

/**
 * 模仿 Path 写一个自己封装的 path
 * 可以封装指令 ： moveTo、cubicTo、lineTo
 * 控制 view 运动，属性动画
 */
public class AnimatorPath extends Path {

    ArrayList<PathPoint> mPathPoint = new ArrayList<>();
    private View view;

    public void moveTo(float x, float y) {
        mPathPoint.add(PathPoint.moveTo(x, y));
    }

    public void cubicTo(float x1, float y1, float x2, float y2, float x, float y) {
        mPathPoint.add(PathPoint.cubicTo(x1, y1, x2, y2, x, y));
    }

    public void lineTo(float x, float y) {
        mPathPoint.add(PathPoint.lineTo(x, y));
    }

    public void startAnimation(View v, int duration) {
        this.view = v;
        // 属性动画的本质：反射掉用的 view.setTranslationX(xxx), view.setAlpha()
        // 升华：本质是控制一个对象中的任何属性值
//        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationX", 0, 50, 70, 100);

        // 自定义属性动画
//        ObjectAnimator animator = ObjectAnimator.ofObject(this, "haha", null,PathPoint1, PathPoint2,PathPoint3);

        // mPathPoint.toArray() 表示 view 移动的一段路径
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "haha", new PathEvaluator(),mPathPoint.toArray());
        animator.setDuration(duration);
        animator.start();

    }

    public void setHaha(PathPoint p){
        // 不断的修改x,y的平移效果
        view.setTranslationX(p.mX);
        view.setTranslationY(p.mY);
    }
}
