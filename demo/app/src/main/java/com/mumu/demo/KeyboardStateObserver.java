package com.mumu.demo;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class KeyboardStateObserver {

    private static final String TAG = KeyboardStateObserver.class.getSimpleName();

    private View mChildOfContent;
    private int usableHeightPrevious;
    private OnKeyboardVisibilityListener listener;

    public void setKeyboardVisibilityListener(OnKeyboardVisibilityListener listener) {
        this.listener = listener;
    }

    public KeyboardStateObserver(Activity activity) {
        FrameLayout content = activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            possiblyResizeChildOfContent();
        }
    };

    public void removeOnGlobalLayoutListener(){
        if (mChildOfContent != null) {
            mChildOfContent.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            mChildOfContent = null;
        }

        if (listener != null) {
            listener = null;
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                if (listener != null) {
                    listener.onKeyboardShow();
                }
            } else {
                if (listener != null) {
                    listener.onKeyboardHide();
                }
            }
            usableHeightPrevious = usableHeightNow;
            Log.d(TAG,"usableHeightNow: " + usableHeightNow + " | usableHeightSansKeyboard:" + usableHeightSansKeyboard + " | heightDifference:" + heightDifference);
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);

        Log.d(TAG,"rec bottom>" + r.bottom + " | rec top>" + r.top);
        return (r.bottom - r.top);// 全屏模式下： return r.bottom
    }

    public interface OnKeyboardVisibilityListener {
        void onKeyboardShow();

        void onKeyboardHide();
    }
}