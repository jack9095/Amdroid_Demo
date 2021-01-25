package com.kuanquan.pagetransitionanimation.util;

import android.view.animation.Interpolator;

public class SineInterpolator implements Interpolator {

    private int type;

    public SineInterpolator(){}

    public SineInterpolator(int type) {
        this.type = type;
    }

    public float getInterpolation(float t) {
//        if (type == EasingType.IN) {
//            return in(t);
//        } else
//        if (type == EasingType.OUT) {
//            return out(t);
//        } else
//        if (type == EasingType.INOUT) {
            return inout(t);
//        }
//        return 0;
    }

    private float in(float t) {
        return (float) (-Math.cos(t * (Math.PI/2)) + 1);
    }
    private float out(float t) {
        return (float) Math.sin(t * (Math.PI/2));
    }
    private float inout(float t) {
        return (float) (-0.5f * (Math.cos(Math.PI*t) - 1));
    }
}