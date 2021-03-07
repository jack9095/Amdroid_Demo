package com.kuanquan.panelemojikeyboard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class SoftEditText extends androidx.appcompat.widget.AppCompatEditText {
    private OnSoftHideListener onSoftHideListenner;
    public SoftEditText(Context context) {
        super(context);
    }

    public SoftEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1) {
            super.onKeyPreIme(keyCode, event);
            if(onSoftHideListenner!=null){
                onSoftHideListenner.onHide();
            }
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setOnSoftHideListenner(OnSoftHideListener onSoftHideListenner) {
        this.onSoftHideListenner = onSoftHideListenner;
    }

    public  interface OnSoftHideListener{
        void onHide();
    }
}
