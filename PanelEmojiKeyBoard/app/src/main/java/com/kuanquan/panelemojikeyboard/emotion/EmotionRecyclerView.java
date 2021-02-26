//package com.kuanquan.panelemojikeyboard.emotion;
//
//import android.content.Context;
//import android.text.Editable;
//import android.text.Spannable;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.listener.OnItemClickListener;
//import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//import com.kuanquan.panelemojikeyboard.R;
//import com.kuanquan.panelemojikeyboard.util.DisplayUtils;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//
///**
// * Created by yummyLau on 18-7-11
// * Email: yummyl.lau@gmail.com
// * blog: yummylau.com
// */
//public class EmotionRecyclerView extends RecyclerView {
//
//    private static int sNumColumns = 0;
//    private static int sNumRows = 0;
//    private static int sPadding = 0;
//    private static int sEmotionSize = 0;
//    private int currentWidth = -1;
//    private int currentHeight = -1;
//    private Adapter mAdapter;
//
//    public EmotionRecyclerView(@NonNull Context context) {
//        this(context, null);
//    }
//
//    public EmotionRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void buildEmotionViews(final EditText editText, List<Emotion> data, int width, int height) {
//        if (data == null || data.isEmpty() || editText == null) {
//            return;
//        }
//        if (currentWidth == width && currentHeight == height) {
//            return;
//        }
//        currentWidth = width;
//        currentHeight = height;
//
//        int emotionViewContainSize = calSizeForContainEmotion(getContext(), currentWidth, currentHeight);
//        if (emotionViewContainSize == 0) {
//            return;
//        }
//
//        setLayoutManager(new GridLayoutManager(getContext(), sNumColumns));
//
//        mAdapter = new Adapter();
//
//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                Emotion emotion = data.get(position);
//                int start = editText.getSelectionStart();
//                Editable editable = editText.getEditableText();
//                Spannable emotionSpannable = EmojiSpanBuilder.buildEmotionSpannable(getContext(), emotion.text);
//                editable.insert(start, emotionSpannable);
//            }
//        });
//
//        mAdapter.setList(data);
//        setAdapter(mAdapter);
//    }
//
//    public static int calSizeForContainEmotion(Context context, int width, int height) {
//        sPadding = DisplayUtils.dip2px(context, 5f);
//        sEmotionSize = DisplayUtils.dip2px(context, 50f);
//        sNumColumns = width / sEmotionSize;
//        sNumRows = height / sEmotionSize;
//        return sNumColumns * sNumRows;
//    }
//
//    public static class Adapter extends BaseQuickAdapter<Emotion, BaseViewHolder> {
//
//        public Adapter() {
//            super(R.layout.vh_emotion_item_layout);
//        }
//
//        @Override
//        protected void convert(@NotNull BaseViewHolder holder, Emotion emotion) {
////            holder.setText(R.id.tv, item)
//            ImageView imageView = holder.getView(R.id.image);
//            imageView.setImageResource(emotion.drawableRes);
//        }
//    }
//
//}
