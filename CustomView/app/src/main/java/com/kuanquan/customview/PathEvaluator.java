package com.kuanquan.customview;

import android.animation.TypeEvaluator;

public class PathEvaluator implements TypeEvaluator<PathPoint> {


    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
        float x = 0, y = 0;

        // 判断是哪一段计算方式
        if (endValue.mOperation == PathPoint.CUBIC) {
            // 贝塞尔曲线计算方式
            float oneMinusT = 1 - fraction;
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0X +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1X +
                    fraction * fraction * fraction * endValue.mX;

            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0Y +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1Y +
                    fraction * fraction * fraction * endValue.mY;

        } else if (endValue.mOperation == PathPoint.LINE) {
            // 直线计算方式

            x = startValue.mX + fraction * (endValue.mX - startValue.mX);
            y = startValue.mY + fraction * (endValue.mY - startValue.mY);
        } else {
            // 直接赋值 MOVE
            x = endValue.mX;
            y = endValue.mY;
        }
        return PathPoint.moveTo(x, y);
    }

    // 直线计算方式 FloatTypeEvaluator 中的源码
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
