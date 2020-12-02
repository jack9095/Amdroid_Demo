package com.kuanquan.commentbanner.viewpager2.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kuanquan.commentbanner.R;

/**
 * auth aboom
 * date 2020-01-13
 */
public class ImageAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public ImageAdapter() {
        super(R.layout.item_image);
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = super.onCreateDefViewHolder(parent, viewType);
        return baseViewHolder;
    }

    //看这里 回调出正确的position
    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {

        Glide.with(helper.itemView.getContext())
                .load(item)
                .into((ImageView) helper.getView(R.id.img));
    }


}
