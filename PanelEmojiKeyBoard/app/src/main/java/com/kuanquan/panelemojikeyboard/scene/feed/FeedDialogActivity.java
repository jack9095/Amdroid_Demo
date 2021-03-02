package com.kuanquan.panelemojikeyboard.scene.feed;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.*;

import com.effective.android.panel.utils.DisplayUtil;
import com.kuanquan.panelemojikeyboard.R;
import com.kuanquan.panelemojikeyboard.databinding.ActivityFeedDialogLayoutBinding;
import com.kuanquan.panelemojikeyboard.scene.CustomDialogFragment;
import com.kuanquan.panelemojikeyboard.systemui.StatusbarHelper;

/**
 * 类微博/微信朋友圈信息流 Dialog 实现
 * 致敬开源项目： https://github.com/YummyLau/PanelSwitchHelper
 */
public class FeedDialogActivity extends AppCompatActivity {

    private ActivityFeedDialogLayoutBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        StatusbarHelper.setStatusBarColor(this, Color.TRANSPARENT);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_feed_dialog_layout);
        mBinding.feedList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.feedList.setAdapter(new FeedAdapter(this));
    }

    public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;

        public FeedAdapter(Context context) {
            super();
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return viewType == 0 ? new CoverHolder(LayoutInflater.from(context).inflate(R.layout.holder_feed_cover_layout, parent, false))
                    : new FeedItemHolder(LayoutInflater.from(context).inflate(R.layout.holder_feed_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof FeedItemHolder) {
                ((FeedItemHolder) holder).bindData(position, position == getItemCount() - 1);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? 0 : 1;
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    public class FeedItemHolder extends RecyclerView.ViewHolder {

        private FeedActionPopup popup;
        private Rect selectedItemRect = new Rect();
        private FeedCommentDialog.onDialogStatus dialogStatusListener = new FeedCommentDialog.onDialogStatus() {
            private boolean lastVisibleStatue = false;

            @Override
            public void onStatus(boolean visible, final int currentTop) {
                if (lastVisibleStatue == visible) {
                    return;
                }
                lastVisibleStatue = visible;
                if (visible) {
                    int dist = selectedItemRect.bottom - currentTop;
                    mBinding.feedList.scrollBy(0, dist);
                }
                //如果需要还原到原来位置，则把注释打开就好
//                else {
//                    Rect currentRect = new Rect();
//                    itemView.getGlobalVisibleRect(currentRect);
//                    mBinding.feedList.scrollBy(0, currentRect.bottom - selectedItemRect.bottom);
//                }
            }
        };

        public FeedItemHolder(View itemView) {
            super(itemView);
        }

        @TargetApi(19)
        public void bindData(int position, boolean isLast) {
            Context context = itemView.getContext();
            ((ImageView) itemView.findViewById(R.id.image)).setImageDrawable(ContextCompat.getDrawable(context, context.getResources().getIdentifier("ic_uzi_" + position % 10, "drawable", context.getApplicationInfo().packageName)));
            itemView.findViewById(R.id.divider).setVisibility(isLast ? View.GONE : View.VISIBLE);
            final View action = itemView.findViewById(R.id.action);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popup == null) {
                        popup = new FeedActionPopup(context, v1 -> {
                            itemView.getGlobalVisibleRect(selectedItemRect);
                            CustomDialogFragment.newInstance().show(getSupportFragmentManager());
//                            new FeedCommentDialog((FeedDialogActivity) v1.getContext(), dialogStatusListener).show();
                        });
                    }
                    if (popup.isShowing()) {
                        popup.dismiss();
                    } else {
                        //api 19 request,demo make targetApi 19.if your targerApi below 19,should user showAtLocation
                        popup.showAsDropDown(action, -DisplayUtil.dip2px(context, 10f) - action.getMeasuredWidth(), (DisplayUtil.dip2px(context, 30f) - action.getMeasuredHeight()) / 2 - DisplayUtil.dip2px(context, 30f), Gravity.RIGHT);
                    }
                }
            });
        }
    }

    public class CoverHolder extends RecyclerView.ViewHolder {

        public CoverHolder(View itemView) {
            super(itemView);
        }
    }

    // 点赞和评论的 popupWindow
    public class FeedActionPopup extends PopupWindow {

        public FeedActionPopup(final Context context, View.OnClickListener clickListener) {
            final View view = LayoutInflater.from(context).inflate(R.layout.pop_feed_action_layout, null, false);
            setAnimationStyle(R.style.FeedActionPopup_anim_style);
            setFocusable(true);
            setWidth(DisplayUtil.dip2px(context, 150f));
            setHeight(DisplayUtil.dip2px(context, 34f));
            setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
            setBackgroundDrawable(dw);
            setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            setContentView(view);
            view.findViewById(R.id.comment).setOnClickListener(v -> {
                dismiss();
                clickListener.onClick(v);
            });

        }

    }


}
