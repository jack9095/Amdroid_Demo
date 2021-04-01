package com.kuanquan.music_lyric.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.kuanquan.music_lyric.utils.FontUtil;


/**
 * @Description: 字体图标文本
 */
public class IconfontTextView extends AppCompatTextView {
    public IconfontTextView(Context context) {
        super(context);
        init(context);
    }

    public IconfontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconfontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 设置字体图片
        Typeface iconfont = FontUtil.getInstance(context).getTypeFace();
        setTypeface(iconfont);
    }


}
