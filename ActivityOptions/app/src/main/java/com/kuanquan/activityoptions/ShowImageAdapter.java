package com.kuanquan.activityoptions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


public class ShowImageAdapter extends BaseRVAdapter<ImageBean, ShowImageAdapter.ShowImageAdapterHolder> {

    @NonNull
    @Override
    public ShowImageAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ShowImageAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_show_image, viewGroup, false));
    }

    @Override
    protected void bindItemData(ShowImageAdapterHolder viewHolder, ImageBean imageBean, int position) {
        viewHolder.bindData(imageBean, position);
    }

    public static class ShowImageAdapterHolder extends RecyclerView.ViewHolder implements IViewHolder<ImageBean> {

        ImageView mImage;
        RelativeLayout mItemRelativeLayout;
        private int[] shapes = {R.drawable.shape_blue_bg, R.drawable.shape_yellow_18dp, R.drawable.shape_white20, R.drawable.shape_black60_2dp_bg};

        public ShowImageAdapterHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_item_show_image);
            mItemRelativeLayout = itemView.findViewById(R.id.rlv_item_tree_story_adapter);
        }

        @Override
        public void bindData(ImageBean imageBean, int position) {
            Context context = itemView.getContext();
            Glide.with(context)
                    .load(imageBean.getCoverUrl())
//                    .placeholder(shapes[position % 4])
//                    .fitCenter()
//                    .dontAnimate()
                    .into(mImage);
        }
    }
}
