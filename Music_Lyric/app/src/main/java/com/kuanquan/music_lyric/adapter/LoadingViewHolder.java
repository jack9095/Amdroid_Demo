package com.kuanquan.music_lyric.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.music_lyric.R;
import com.kuanquan.music_lyric.widget.ListItemRelativeLayout;

/**
 * 加载中
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {
    private View view;
    /**
     * item底部布局
     */
    private ListItemRelativeLayout listItemRelativeLayout;

    public LoadingViewHolder(View view) {
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
