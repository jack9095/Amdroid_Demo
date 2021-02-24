package com.kuanquan.emotionkeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MyLinearLayout extends LinearLayout {
    private int oldHeight;
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("zyp","onSizeChanged w="+w+"  h="+h+"  oldw="+oldw+"   oldh="+oldh);
        if(oldh>0 && oldh-h>0 && onKeyboardVisibilityChangedListener!=null){
            onKeyboardVisibilityChangedListener.onKeyboardVisibilityChanged(oldh-h>0,oldh-h);
        }
    }
    private onKeyboardVisibilityChangedListener onKeyboardVisibilityChangedListener;

    public MyLinearLayout.onKeyboardVisibilityChangedListener getOnKeyboardVisibilityChangedListener() {
        return onKeyboardVisibilityChangedListener;
    }

    public void setOnKeyboardVisibilityChangedListener(MyLinearLayout.onKeyboardVisibilityChangedListener onKeyboardVisibilityChangedListener) {
        this.onKeyboardVisibilityChangedListener = onKeyboardVisibilityChangedListener;
    }

    public interface   onKeyboardVisibilityChangedListener{
        void onKeyboardVisibilityChanged(boolean keyboardShow,int keyboardHeight);
    }
}
