package com.kuanquan.commentbanner.viewpager2.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kuanquan.commentbanner.R;

/**
 * auth aboom
 * date 2020-01-13
 */
public class ImageRoundAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public ImageRoundAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {
        Glide.with(getContext())
                .load(item)
//                .apply(new RequestOptions().transform(new RoundedCorners(UIUtil.dip2px(mContext, 12))))
                .into((ImageView) helper.getView(R.id.img));
    }
}
