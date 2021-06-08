package com.kuanquan.botttomsheetdialog.widget;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;


public class CustomViewpager extends ViewPager {
    private boolean scrollable = true;

    public CustomViewpager(Context context) {
        super(context);
    }

    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (this.scrollable) {
                return super.onTouchEvent(ev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (this.scrollable) {
                return super.onInterceptTouchEvent(ev);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
