package com.kuanquan.pagetransitionanimation;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.view.View;

import androidx.core.app.SharedElementCallback;

public class TransitionCallBack extends SharedElementCallback {

    @Override
    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
        sharedElement.getBackground().setAlpha(0);
        return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
    }
}