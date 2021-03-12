package com.kuanquan.customlayoutmanager.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float downX;
    float downY;

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = ev.getX();
//                downY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = ev.getX();
//                float moveY = ev.getY();
//
//
//                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
//                    downX = moveX;
//                    downY = moveY;
//                    return super.dispatchTouchEvent(ev);
//                } else {
//                    downX = moveX;
//                    downY = moveY;
//                    return true;
//                }
//
////                break;
//            case MotionEvent.ACTION_UP:
//                downX = 0f;
//                downY = 0f;
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
