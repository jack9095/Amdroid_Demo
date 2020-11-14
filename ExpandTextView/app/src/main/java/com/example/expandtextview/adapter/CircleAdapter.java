package com.example.expandtextview.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.expandtextview.R;
import com.example.expandtextview.bean.CircleBean;
import com.example.expandtextview.bean.CommentListBean;
import com.example.expandtextview.util.Constants;
import com.example.expandtextview.util.GlideUtils;
import com.example.expandtextview.util.KeyboardUtil;
import com.example.expandtextview.util.PopupWindowUtil;
import com.example.expandtextview.view.CommentsView;
import com.example.expandtextview.view.ExpandTextView;
import com.example.expandtextview.view.NineGridView;
import com.example.expandtextview.view.PraiseListView;
import com.github.ielse.imagewatcher.ImageWatcher;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @描述: 朋友圈适配器
 */
public class CircleAdapter extends BaseMultiItemQuickAdapter<CircleBean.DataBean,BaseViewHolder>{
    private TextView tvAddress, tvDelete;
    private ImageView imageView;
    private ExpandTextView tvContent;
    private ImageWatcher imageWatcher;
    private RequestOptions mRequestOptions;
    private DrawableTransitionOptions mDrawableTransitionOptions;
    private LinearLayout llComment;
    private EditText etComment;
    private int x;
    private int y;
    private PopupWindow mPopupWindow;
    private Click click;

    public CircleAdapter(@Nullable List<CircleBean.DataBean> data, ImageWatcher imageWatcher, LinearLayout llComment, EditText etComment, Click click) {
        super(data);
        //文本
        addItemType(Constants.TYPE_TEXT, R.layout.item_text);
        //图片
        addItemType(Constants.TYPE_IMAGE, R.layout.item_image);
        //视频
        addItemType(Constants.TYPE_VIDEO, R.layout.item_video);
        //网页
        addItemType(Constants.TYPE_WEB, R.layout.item_web);
        this.imageWatcher = imageWatcher;
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
        this.llComment = llComment;
        this.etComment = etComment;
        this.click = click;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CircleBean.DataBean item) {
        if(item == null){
            return;
        }
        imageView = helper.getView(R.id.iv_photo);
        tvContent = helper.getView(R.id.tv_content);
        tvAddress = helper.getView(R.id.tv_address);
        tvDelete = helper.getView(R.id.tv_delete);

        switch (item.getItemType()) {
            //文本
            case Constants.TYPE_TEXT:
                //用户名
                if (item.getUser_name() != null && !item.getUser_name().equals("")) {
                    helper.setText(R.id.tv_name, item.getUser_name());
                }
                //评论时间
                if (item.getCreateon() != null && !item.getCreateon().equals("")) {
                    helper.setText(R.id.tv_time, item.getCreateon());
                }
                break;
            //图片
            case Constants.TYPE_IMAGE:
                if (item.getUser_name() != null && !item.getUser_name().equals("")) {
                    helper.setText(R.id.tv_name, item.getUser_name());
                }
                if (item.getCreateon() != null && !item.getCreateon().equals("")) {
                    helper.setText(R.id.tv_time, item.getCreateon());
                }
                //多张图片显示
                NineGridView layout = helper.getView(R.id.layout_nine);
                layout.setSingleImageSize(80, 120);
                if (item.getFiles() != null && item.getFiles().size() > 0) {
                    layout.setAdapter(new NineImageAdapter(mContext, mRequestOptions, mDrawableTransitionOptions, item.getFiles()));
                    layout.setOnImageClickListener((position, view) -> {
                        imageWatcher.show((ImageView) view, layout.getImageViews(), item.getImageUriList());
                    });
                }
                break;
            //视频
            case Constants.TYPE_VIDEO:
                if (item.getUser_name() != null && !item.getUser_name().equals("")) {
                    helper.setText(R.id.tv_name, item.getUser_name());
                }

                if (item.getCreateon() != null && !item.getCreateon().equals("")) {
                    helper.setText(R.id.tv_time, item.getCreateon());
                }
                ImageView ivVideo = helper.getView(R.id.video_view);
                //视频封面图
                if (item.getFiles() != null && item.getFiles().size() > 0) {
                    GlideUtils.loadImg(mContext, item.getFiles().get(0), ivVideo);
                }
                break;
            //网页
            case Constants.TYPE_WEB:
                ImageView ivUser = helper.getView(R.id.iv_user);
                if (item.getUser_name() != null && !item.getUser_name().equals("")) {
                    helper.setText(R.id.tv_name, item.getUser_name());
                }

                if (item.getCreateon() != null && !item.getCreateon().equals("")) {
                    helper.setText(R.id.tv_time, item.getCreateon());
                }
                if (item.getShare_image() != null && !item.getShare_image().equals("")) {
                    GlideUtils.loadImg(mContext, item.getShare_image(), ivUser, R.drawable.ic_launcher_background);
                }
                if (item.getShare_title() != null && !item.getShare_title().equals("")) {
                    helper.setText(R.id.tv_text, item.getShare_title());
                } else {
                    helper.setText(R.id.tv_text, "");
                }
                break;
    }

        //用户头像
        if (!TextUtils.isEmpty(item.getHead_img())) {
            GlideUtils.loadImg(mContext, item.getHead_img(), imageView, R.drawable.ic_launcher_background);
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
        //评论内容
        if (TextUtils.isEmpty(item.getContent())) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(item.getContent());
        }
        if (!TextUtils.isEmpty(item.getPosition()) && !item.getPosition().equals("该位置信息暂无")) {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(item.getPosition());
        } else {
            tvAddress.setVisibility(View.GONE);
        }
        tvDelete.setVisibility(View.VISIBLE);
        LinearLayout llLike = helper.getView(R.id.ll_like);
        PraiseListView rvLike = helper.getView(R.id.rv_like);
        CommentsView rvComment = helper.getView(R.id.rv_comment);
        View viewLike = helper.getView(R.id.view_like);

        helper.addOnClickListener(R.id.iv_edit);
        helper.addOnClickListener(R.id.tv_like);
        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.iv_user);
        helper.addOnClickListener(R.id.video_view);
        helper.addOnClickListener(R.id.iv_photo);
        helper.addOnClickListener(R.id.tv_name);
        helper.addOnClickListener(R.id.ll_share);
        helper.addOnLongClickListener(R.id.tv_content);

        if ((item.getLike_list() != null && item.getLike_list().size() > 0) && (item.getComments_list() != null && item.getComments_list().size() > 0)) {
            viewLike.setVisibility(View.VISIBLE);
        } else {
            viewLike.setVisibility(View.GONE);
        }
        if ((item.getLike_list() != null && item.getLike_list().size() > 0) || (item.getComments_list() != null && item.getComments_list().size() > 0)) {
            llLike.setVisibility(View.VISIBLE);
            if (item.getLike_list() != null && item.getLike_list().size() > 0) {
                rvLike.setVisibility(View.VISIBLE);
                rvLike.setDatas(item.getLike_list());
            } else {
                rvLike.setVisibility(View.GONE);
            }

            if (item.getComments_list() != null && item.getComments_list().size() > 0) {
                rvComment.setVisibility(View.VISIBLE);
                rvComment.setList(item.getComments_list());
                rvComment.setOnCommentListener((position, bean, user_id) -> {
                    etComment.setText("");
                    //如果当前用户id和评论用户id相同则删除
                    if (bean.getCommentsUser().getUserId().equals("1")) {

                        showDeletePopWindow(rvComment, Integer.parseInt(bean.getId()), helper.getLayoutPosition() - 1, position);
                        llComment.setVisibility(View.GONE);
                    } else {
                        //不相同回复
                        llComment.setVisibility(View.VISIBLE);
                        etComment.setHint("回复:" + bean.getCommentsUser().getUserName());
                        if (View.VISIBLE == llComment.getVisibility()) {
                            llComment.requestFocus();
                            //弹出键盘
                            KeyboardUtil.showSoftInput(etComment.getContext(), etComment);
                        } else if (View.GONE == llComment.getVisibility()) {
                            //隐藏键盘
                            KeyboardUtil.hideSoftInput(etComment.getContext(), etComment);
                        }
                        if (click != null) {
                            //由于真实项目中有头部所以position-1
                            click.Commend(helper.getLayoutPosition(), bean);
                        }
                    }
                });
                rvComment.notifyDataSetChanged();
            } else {
                rvComment.setVisibility(View.GONE);
            }
        } else {
            llLike.setVisibility(View.GONE);
        }
        rvComment.setOnItemLongClickListener((position, bean) -> {
            showCopyPopWindow(rvComment, item.getComments_list().get(position).getContent());
        });
    }

    private void showCopyPopWindow(CommentsView rvComment, String content) {
        View contentView = getCopyPopupWindowContentView(content);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int[] windowPos = PopupWindowUtil.calculatePopWindowPos(rvComment, contentView, x, y);
        mPopupWindow.showAsDropDown(rvComment, 0, -60, windowPos[1]);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    private View getCopyPopupWindowContentView(String content) {
        // 布局ID
        int layoutId = R.layout.popup_copy;
        View contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = v -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
            ClipboardManager mCM = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
            mCM.setPrimaryClip(ClipData.newPlainText(null, content));
            Toast.makeText(mContext, mContext.getString(R.string.copied), Toast.LENGTH_SHORT).show();
        };
        contentView.findViewById(R.id.menu_copy).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void showDeletePopWindow(View view, int id, int layoutPosition, int position) {
        View contentView = getPopupWindowContentView(id, layoutPosition, position);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int[] windowPos = PopupWindowUtil.calculatePopWindowPos(view, contentView, x, y);
        mPopupWindow.showAsDropDown(view, 0, -40, windowPos[1]);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
    }

    private View getPopupWindowContentView(int id, int layoutPosition, int position) {
        // 布局ID
        int layoutId = R.layout.popup_delete;
        View contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = v -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }

        };
        contentView.findViewById(R.id.menu_delete).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    public interface Click {
        void Commend(int position, CommentListBean bean);//回复评论
    }
}
