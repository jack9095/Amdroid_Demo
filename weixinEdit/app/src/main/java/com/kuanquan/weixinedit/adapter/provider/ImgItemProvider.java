package com.kuanquan.weixinedit.adapter.provider;

import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.kuanquan.weixinedit.R;
import com.kuanquan.weixinedit.adapter.NineImageAdapter;
import com.kuanquan.weixinedit.model.EventModel;
import com.kuanquan.weixinedit.model.ProviderMultiEntity;
import com.kuanquan.weixinedit.util.ToastUtils;
import com.kuanquan.weixinedit.widget.ExpandTextView;
import com.kuanquan.weixinedit.widget.NineGridView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImgItemProvider extends BaseItemProvider<ProviderMultiEntity> {

    private RequestOptions mRequestOptions;
    private DrawableTransitionOptions mDrawableTransitionOptions;
    private ExpandTextView tvContent;

    public ImgItemProvider() {
        this.mRequestOptions = new RequestOptions().centerCrop();
        this.mDrawableTransitionOptions = DrawableTransitionOptions.withCrossFade();
        addChildClickViewIds(R.id.iv_edit);
    }

    @Override
    public int getItemViewType() {
        return ProviderMultiEntity.TYPE_IMAGE;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_image;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @Nullable ProviderMultiEntity data) {

        tvContent = helper.getView(R.id.tv_content);
        //用户名
        if (data.name != null && !data.name.equals("")) {
            helper.setText(R.id.tv_name, data.name);
        }
        //评论时间
        if (data.createon != null && !data.createon.equals("")) {
            helper.setText(R.id.tv_time, data.createon);
        }
        //评论内容
        if (TextUtils.isEmpty(data.content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(data.content);
        }

        //多张图片显示
        NineGridView layout = helper.getView(R.id.layout_nine);
        layout.setSingleImageSize(80, 120);
        if (data.images != null && data.images.size() > 0) {
            layout.setAdapter(new NineImageAdapter(helper.itemView.getContext(), mRequestOptions, mDrawableTransitionOptions, data.images));
        }
//        if (helper.getAdapterPosition() % 2 == 0) {
//            helper.setImageResource(R.id.iv, R.mipmap.animation_img1);
//        } else {
//            helper.setImageResource(R.id.iv, R.mipmap.animation_img2);
//        }
    }

    /**
     * item 点击
     *
     * @param helper
     * @param data
     * @param position
     */
    @Override
    public void onClick(@NonNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
//        Tips.show("Click: " + position);
    }

    @Override
    public boolean onLongClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
//        Tips.show("Long Click: " + position);
        return true;
    }

    /**
     * 子控件点击
     *
     * @param helper
     * @param view
     * @param data
     * @param position
     */
    @Override
    public void onChildClick(@NotNull BaseViewHolder helper, @NotNull View view, ProviderMultiEntity data, int position) {
        if (view.getId() == R.id.iv_edit) {
//            ToastUtils.ToastShort(view.getContext(), "点击评论");
            EventBus.getDefault().post(new EventModel(view, position));
        }
    }
}