package com.kuanquan.music_lyric.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.music_lyric.R;
import com.kuanquan.music_lyric.widget.ListItemRelativeLayout;

/**
 * 加载更多
 */
public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    private View view;
    /**
     * item底部布局
     */
    private ListItemRelativeLayout listItemRelativeLayout;

    public LoadMoreViewHolder(View view) {
        super(view);
        this.view = view;
    }

    public ListItemRelativeLayout getListItemRelativeLayout() {
        if (listItemRelativeLayout == null) {
            listItemRelativeLayout = view.findViewById(R.id.itemBG);
        }
        return listItemRelativeLayout;
    }

}
