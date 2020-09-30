package com.kuanquan.weixinedit.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kuanquan.weixinedit.R;

public class ToastUtils {

    private static ToastUtils toastUtils;
    private static Context mContext;

    public static ToastUtils getInstance() {
        if (toastUtils == null) {
            toastUtils = new ToastUtils();
        }
        return toastUtils;
    }

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 同时显示文本和图片的Toast
     *
     * @param context
     * @param message
     */
    public static void ToastShortImage(Context context, String message) {
        mContext = context;
        if (mContext != null) {
            View toastView = LayoutInflater.from(mContext).inflate(R.layout.toast_image_layout, null);
            TextView text = toastView.findViewById(R.id.tv_message);
            text.setText(message);    //要提示的文本
            Toast toast = new Toast(mContext);   //上下文
            toast.setGravity(Gravity.CENTER, 0, 0);   //位置居中
            toast.setDuration(Toast.LENGTH_SHORT);  //设置短暂提示
            toast.setView(toastView);   //把定义好的View布局设置到Toast里面
            toast.show();
        }
    }

    /**
     * 显示文本的Toast
     *
     * @param context
     * @param message 文本消息
     */
    public static void ToastShort(Context context, String message) {
        mContext = context;
        if (mContext != null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.toast_text_layout, null);
            TextView text = view.findViewById(R.id.tv_message);
            text.setText(message);
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }

    /**
     * 显示消息
     *
     * @param message 消息
     */
    public static void ToastShort(String message) {
        if (mContext != null) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.toast_text_layout, null);
            TextView text = view.findViewById(R.id.tv_message);
            text.setText(message);
            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(view);
            toast.show();
        }
    }
}
