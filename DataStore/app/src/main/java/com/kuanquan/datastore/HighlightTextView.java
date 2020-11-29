package com.kuanquan.datastore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by albertlii on 2017/8/14.
 */

public class HighlightTextView extends AppCompatTextView {
    private final int DEF_SPAN_FLAG = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    /**
     * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE :
     * Before and after are not included, that is, within a specified range of front and back insert new characters will not apply new style
     * <p>
     * Spannable.SPAN_EXCLUSIVE_INCLUSIVE :
     * Do not include front, including later. That is only in the scope of the characters behind the new characters, application of new style
     * <p>
     * Spannable.SPAN_INCLUSIVE_EXCLUSIVE :
     * Including front, does not include the behind.
     * <p>
     * Spannable.SPAN_INCLUSIVE_INCLUSIVE :
     * Including both before and after
     */
    private int mSpanFlag;
    private SpannableStringBuilder mBuilder;

    private String mText;

    public HighlightTextView(Context context) {
        super(context);
        init(context, null);
    }

    public HighlightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HighlightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mSpanFlag = DEF_SPAN_FLAG;
        mBuilder = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(mText)) {
            mBuilder.append(mText);
        }
    }

    public HighlightTextView setSpanFlag(int flag) {
        this.mSpanFlag = flag;
        return this;
    }

    public HighlightTextView addContent(CharSequence text) {
        mBuilder.append(text);
        return this;
    }

    /**
     * Set the text color
     *
     * @param color
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addFontColorStyle(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new ForegroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addFontColorStyleByKey(@ColorInt int color, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(new ForegroundColorSpan(color), index[0], index[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Set the text background color
     *
     * @param color
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addBgColorStyle(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new BackgroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addBgColorStyleByKey(@ColorInt int color, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(new BackgroundColorSpan(color), index[0], index[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Add url links to text
     *
     * @param url
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addURLStyle(String url, int start, int end) {
        mBuilder.setSpan(new URLSpan(url), start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addURLStyleByKey(String url, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(new URLSpan(url), index[0], index[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Set the font style, such as italic
     *
     * @param style Typeface.NORMAL、 Typeface.BOLD、Typeface.ITALIC、Typeface.BOLD_ITALIC
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addTypefaceStyle(int style, int start, int end) {
        mBuilder.setSpan(new StyleSpan(style), start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addTypefaceStyleByKey(int style, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(new StyleSpan(style), index[0], index[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Add strikethrough
     *
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addStrikethroughStyle(int start, int end) {
        mBuilder.setSpan(new StrikethroughSpan(), start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addStrikethroughStyleByKey(String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(new StrikethroughSpan(), index[0], index[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Replace text with drawable
     *
     * @param span
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addImageStyle(ImageSpan span, int start, int end) {
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addImageStyleByKey(ImageSpan span, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            mBuilder.setSpan(span, index[0], index[1], mSpanFlag);
        }
        return this;
    }

    public HighlightTextView addImageStyle(Bitmap b, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(getContext(), b, getImageSpanType(verticalAlignment));
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addImageStyleByKey(Bitmap b, int verticalAlignment, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            ImageSpan span = new ImageSpan(getContext(), b, getImageSpanType(verticalAlignment));
            mBuilder.setSpan(span, index[0], index[1], mSpanFlag);
        }
        return this;
    }

    public HighlightTextView addImageStyle(Drawable d, int verticalAlignment, int start, int end) {
//        d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(d, getImageSpanType(verticalAlignment));
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addImageStyleByKey(Drawable d, int verticalAlignment, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            ImageSpan span = new ImageSpan(d, getImageSpanType(verticalAlignment));
            mBuilder.setSpan(span, index[0], index[1], mSpanFlag);
        }
        return this;
    }

    public HighlightTextView addImageStyle(@DrawableRes int resourceId, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(getContext(), resourceId, getImageSpanType(verticalAlignment));
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addImageStyleByKey(@DrawableRes int resourceId, int verticalAlignment, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            ImageSpan span = new ImageSpan(getContext(), resourceId, getImageSpanType(verticalAlignment));
            mBuilder.setSpan(span, index[0], index[1], mSpanFlag);
        }
        return this;
    }

    public HighlightTextView addImageStyle(Uri uri, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(getContext(), uri, getImageSpanType(verticalAlignment));
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addImageStyleByKey(Uri uri, int verticalAlignment, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            ImageSpan span = new ImageSpan(getContext(), uri, getImageSpanType(verticalAlignment));
            mBuilder.setSpan(span, index[0], index[1], mSpanFlag);
        }
        return this;
    }

    private int getImageSpanType(int verticalAlignment) {
        int type;
        if (verticalAlignment == 0) {
            type = ImageSpan.ALIGN_BOTTOM;
        } else if (verticalAlignment == 1) {
            type = ImageSpan.ALIGN_BASELINE;
        } else {
            type = ImageSpan.ALIGN_BOTTOM;
        }
        return type;
    }

    /**
     * For text to add the click event
     * *@param listener
     *
     * @param isNeedUnderLine Whether to need to add the underline
     * @param start
     * @param end
     * @return
     */
    public HighlightTextView addClickStyle(final OnHighlightClickListener listener, final boolean isNeedUnderLine, int start, int end) {
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                if (!isNeedUnderLine) {
                    ds.setUnderlineText(false);
                }
            }

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onTextClick(-1, widget);
                }
            }
        }, start, end, mSpanFlag);
        return this;
    }

    public HighlightTextView addClickStyleByKey(final OnHighlightClickListener listener, final boolean isNeedUnderLine, String key) {
        List<int[]> list = searchAllIndex(key);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    if (!isNeedUnderLine) {
                        ds.setUnderlineText(false);
                    }
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(finalI, widget);
                    }
                }
            }, list.get(finalI)[0], list.get(finalI)[1], mSpanFlag);
        }
        return this;
    }

    public void build() {
        setText(mBuilder);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public SpannableStringBuilder getSpanBuilder() {
        return mBuilder;
    }

    /**
     * Search for all keywords in all strings
     *
     * @param key
     * @return
     */
    public List<int[]> searchAllIndex(String key) {
        List<int[]> indexList = new ArrayList<int[]>();
        String text = mBuilder.toString();
        if (TextUtils.isEmpty(text)) {
            return indexList;
        }
        int a = text.indexOf(key);
        while (a != -1) {
            int[] index = new int[]{a, a + key.length()};
            indexList.add(index);
            a = text.indexOf(key, a + 1);
        }
        return indexList;
    }

    public interface OnHighlightClickListener {
        /**
         * @param position Positon is only useful in the addClickStyleByKey method
         * @param v
         */
        void onTextClick(int position, View v);
    }
}
