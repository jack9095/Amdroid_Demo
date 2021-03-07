package com.siberiadante.emotionapp.utils;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.siberiadante.emotionapp.adapter.EmotionGridViewAdapter;

/**
 * Created by SiberiaDante
 * Describe: 点击表情的全局监听管理类
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */
public class GlobalOnItemClickManagerUtils {

    private static GlobalOnItemClickManagerUtils instance;
    private EditText mEditText;//输入框
//    private static Context mContext;

    public static GlobalOnItemClickManagerUtils getInstance() {
//        mContext=context;
        if (instance == null) {
            synchronized (GlobalOnItemClickManagerUtils.class) {
                if (instance == null) {
                    instance = new GlobalOnItemClickManagerUtils();
                }
            }
        }
        return instance;
    }

    /*
    绑定EditText
     */
    public void attachToEditText(EditText editText) {
        mEditText = editText;
    }

    /*

     */
    public void unAttachToEditText() {
        mEditText = null;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener(final Context context, final int emotion_map_type) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemAdapter = parent.getAdapter();

                if (itemAdapter instanceof EmotionGridViewAdapter) {
                    // 点击的是表情
                    EmotionGridViewAdapter emotionGvAdapter = (EmotionGridViewAdapter) itemAdapter;

                    if (position == emotionGvAdapter.getCount() - 1) {
                        // 如果点击了最后一个回退按钮,则调用删除键事件
                        mEditText.dispatchKeyEvent(new KeyEvent(
                                KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else {
                        // 如果点击了表情,则添加到输入框中
                        String emotionName = emotionGvAdapter.getItem(position);

                        // 获取当前光标位置,在指定位置上添加表情图片文本
                        int curPosition = mEditText.getSelectionStart();
                        StringBuilder sb = new StringBuilder(mEditText.getText().toString());
                        sb.insert(curPosition, emotionName);

                        // 特殊文字处理,将表情等转换一下
                        mEditText.setText(SpanStringUtils.getEmotionContent(emotion_map_type,
                                context, mEditText, sb.toString()));

                        // 将光标设置到新增完表情的右侧
                        mEditText.setSelection(curPosition + emotionName.length());
                    }

                }
            }
        };
    }

}
