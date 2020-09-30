package com.kuanquan.weixinedit.widget;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kuanquan.weixinedit.R;
import com.kuanquan.weixinedit.model.CommentListBean;
import com.kuanquan.weixinedit.model.UserBean;
import com.kuanquan.weixinedit.spannable.CircleMovementMethod;

import java.util.List;

/**
 * @作者: njb
 * @时间: 2019/7/22 19:04
 * @描述: 评论列表
 */
public class CommentsView extends LinearLayout {
    private Context mContext;
    private List<CommentListBean> mDatas;
    private onItemLongClickListener onItemLongClickListener;
    private CommentListener onCommentListener;

    public CommentsView(Context context) {
        this(context, null);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        this.mContext = context;
    }

    /**
     * 设置评论列表信息
     *
     * @param list
     */
    public void setList(List<CommentListBean> list) {
        mDatas = list;
    }

    public void setOnItemLongClickListener(onItemLongClickListener longClickListener){
        this.onItemLongClickListener = longClickListener;
    }

    public void setOnCommentListener(CommentListener onCommentListener){
        this.onCommentListener = onCommentListener;
    }


    public void notifyDataSetChanged() {
        removeAllViews();
        if (mDatas == null || mDatas.size() <= 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10);
        for (int i = 0; i < mDatas.size(); i++) {
            View view = getView(i);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            addView(view, i, layoutParams);
        }
    }

    private View getView(final int position) {
        final CommentListBean item = mDatas.get(position);
        UserBean userBean = new UserBean();
        UserBean replyUser;
        if(!TextUtils.isEmpty(item.getTo_user_id())&& !TextUtils.isEmpty(item.getTo_user_name())){
            userBean.setUserId(item.getTo_user_id());
            userBean.setUserName(item.getTo_user_name());
            item.setReplyUser(userBean);
        }
        replyUser = item.getReplyUser();
        boolean hasReply = false;   // 是否有回复
        if (replyUser != null) {
            hasReply = true;
        }
        TextView textView = new TextView(mContext);
        textView.setTextSize(15);
        textView.setTextColor(0xff686868);
        SpannableStringBuilder builder = new SpannableStringBuilder();

        UserBean userComBean = new UserBean();
        if(item.getUser_id() != null && item.getUser_name() != null){
            userComBean.setUserId(item.getUser_id());
            userComBean.setUserName(item.getUser_name());
            item.setCommentsUser(userComBean);
        }

        UserBean comUser = item.getCommentsUser();

        String name = comUser.getUserName();
        if (hasReply) {
            builder.append(setClickableSpan(name, item.getCommentsUser()));
            builder.append("回复");
            builder.append(setClickableSpan(replyUser.getUserName(), item.getReplyUser()));
        } else {
           builder.append(setClickableSpan(name, item.getCommentsUser()));
        }
        builder.append(" : ");
        builder.append(setClickableSpanContent(item.getContent(), position,userBean.getUserId(),item));
        textView.setText(builder);
        // 设置点击背景色
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        textView.setMovementMethod(new CircleMovementMethod(0xffcccccc, 0xffcccccc));


        return textView;
    }

    /**
     * 设置评论内容点击事件
     *
     * @param item
     * @param position
     * @return
     */
    public SpannableString setClickableSpanContent(final String item, final int position, final String user_id, final CommentListBean bean) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if(onCommentListener != null){
                    onCommentListener.Comment(position,bean,user_id);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置显示的内容文本颜色
                ds.setColor(0xff686868);
                ds.setUnderlineText(false);
            }
        };
        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    /**
     * 设置评论用户名字点击事件
     *
     * @param item
     * @param bean
     * @return
     */
    public SpannableString setClickableSpan(final String item, final UserBean bean) {
        final SpannableString string = new SpannableString(item);
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // 设置显示的用户名文本颜色
                ds.setColor(getResources().getColor(R.color.c697A9F));
                ds.setUnderlineText(false);
            }
        };

        string.setSpan(span, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }


    public interface onItemLongClickListener{
        void onItemLongClick(int position, CommentListBean bean);
    }

    public interface CommentListener{
        void Comment(int position, CommentListBean bean, String user_id);
    }
}
