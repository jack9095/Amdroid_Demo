package com.kuanquan.pagetransitionanimation.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean scrollable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
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
