package com.kuanquan.customview;

public class PathPoint {
    public static final int MOVE = 0;
    public static final int LINE = 1;
    public static final int CUBIC = 2;

    // 存储变量指令
    int mOperation;

    // 当前点移动的坐标， 不断改变的
    float mX, mY;

    // 贝塞尔曲线拐点
    float mControl0X, mControl0Y;
    float mControl1X, mControl1Y;

    private PathPoint(int operation, float x, float y) {
        mOperation = operation;

        mX = x;
        mY = y;
    }

    private PathPoint(int operation, float x1, float y1, float x2, float y2, float x, float y) {
        mOperation = operation;

        mControl0X = x1;
        mControl0Y = y1;
        mControl1X = x2;
        mControl1Y = y2;

        mX = x;
        mY = y;
    }

    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(MOVE, x, y);
    }

    public static PathPoint cubicTo(float x1, float y1, float x2, float y2, float x, float y) {
        return new PathPoint(CUBIC, x1, y1, x2, y2, x, y);
    }

    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(LINE, x, y);
    }

}
