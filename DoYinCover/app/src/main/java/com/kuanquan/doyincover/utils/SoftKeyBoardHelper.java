package com.kuanquan.doyincover.utils;

import android.app.Activity;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author: zhaoningqiang
 * @time: 2019/5/8
 * @Description:
 */
public class SoftKeyBoardHelper {

    private View mDecorView;//activity的根视图
    private int mScreenBottom;//根视图的显示高度
    private boolean isKeyBoardShow = false;//软键盘是否显示
    private ViewTreeObserver.OnGlobalLayoutListener listener;

    public SoftKeyBoardHelper(Activity activity, OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        //获取activity的根视图
        mDecorView = activity.getWindow().getDecorView();

        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            Rect decorViewDisplayFrame = new Rect();
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                mDecorView.getWindowVisibleDisplayFrame(decorViewDisplayFrame);
                int decorBottom = decorViewDisplayFrame.bottom;

                if (mScreenBottom == 0) {
                    mScreenBottom = decorBottom;
                }

                if (mScreenBottom > decorBottom) {

                    if (!isKeyBoardShow && onSoftKeyBoardChangeListener != null) {
                        isKeyBoardShow = true;
                        onSoftKeyBoardChangeListener.onKeyBoardShow(mScreenBottom - decorViewDisplayFrame.bottom);
                    }
                } else {

                    if (isKeyBoardShow && onSoftKeyBoardChangeListener != null) {
                        isKeyBoardShow = false;
                        onSoftKeyBoardChangeListener.onKeyBoardHide();
                    }
                }
            }
        };

        mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        addLifeObServer(activity);
    }


    private void addLifeObServer(Activity activity) {
        if (activity instanceof LifecycleOwner) {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) activity;
            Lifecycle lifecycle = lifecycleOwner.getLifecycle();
            lifecycle.addObserver(new GenericLifecycleObserver() {
                @Override
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        lifecycle.removeObserver(this);
                        if (mDecorView != null)
                            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                    }
                }
            });
        }
    }

    public interface OnSoftKeyBoardChangeListener {
        void onKeyBoardShow(int height);

        void onKeyBoardHide();
    }


    public static int getDisplayHeight(Activity activity) {
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize.y;
    }


    public static void closeSoftKeyboardChecked(ViewGroup root, float x, float y, View currentFocus, Context mContext) {

        boolean result = isClickEditText(root, x, y);

        if (!result) {
            hideSoftKeyboard(currentFocus, mContext);
        }
    }

    public static void hideSoftKeyboard(View currentFocus, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    private static boolean isClickEditText(ViewGroup root, float x, float y) {
        int childCount = root.getChildCount();
        int[] childLocation = new int[2];
        for (int i = 0; i < childCount; i++) {
            View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) child;
                boolean result = isClickEditText(viewGroup, x, y);
                if (result) {
                    return true;
                }
            } else {
                if (child instanceof EditText) {
                    child.getLocationOnScreen(childLocation);
                    if (x > childLocation[0] && x < childLocation[0] + child.getWidth() && y > childLocation[1] && y < childLocation[1] + child.getHeight()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    public static boolean isInsideView(View v, MotionEvent event) {
        if (v != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
