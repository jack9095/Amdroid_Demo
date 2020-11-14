package com.example.expandtextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.expandtextview.R;
import com.example.expandtextview.bean.LikeListBean;
import com.example.expandtextview.spannable.CircleMovementMethod;
import com.example.expandtextview.spannable.SpannableClickable;

import java.util.List;

/**
 * @作者: njb
 * @时间: 2019/7/22 19:04
 * @描述: 点赞列表
 */
public class PraiseListView extends androidx.appcompat.widget.AppCompatTextView {

    private int itemColor;
    private int itemSelectorColor;
    private List<LikeListBean> datas;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PraiseListView(Context context) {
        super(context);
    }

    public PraiseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public PraiseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.c697A9F));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        } finally {
            typedArray.recycle();
        }
    }

    public List<LikeListBean> getDatas() {
        return datas;
    }

    public void setDatas(List<LikeListBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public void notifyDataSetChanged() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加点赞图标
            builder.append(setImageSpan());
            LikeListBean item = null;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (item != null) {
                    builder.append(setClickableSpan(item.getUser_name(), i));
                    if (i != datas.size() - 1) {
                        builder.append(",");
                    }
                }
            }
        }

        setText(builder);

        setMovementMethod(new CircleMovementMethod(itemSelectorColor));
    }


    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.heart_drawable_blue);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(drawable);
        imgSpanText.setSpan(imageSpan,0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        if (onItemClickListener != null) {
                                            onItemClickListener.onClick(position);
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
