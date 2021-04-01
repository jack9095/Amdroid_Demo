package com.kuanquan.music_lyric.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.music_lyric.R;
import com.kuanquan.music_lyric.application.HPApplication;
import com.kuanquan.music_lyric.libs.widget.CircleImageView;
import com.kuanquan.music_lyric.ui.LrcActivity;
import com.kuanquan.music_lyric.ui.SearchSingerActivity;
import com.kuanquan.music_lyric.utils.ImageUtil;
import com.kuanquan.music_lyric.widget.PopListItemRelativeLayout;

/**
 * 歌手列表
 */
public class LrcPopSingerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private String[] mDatas;
    private HPApplication mHPApplication;
    private LrcActivity.LrcActivityListener mLrcActivityListener;

    public LrcPopSingerListAdapter(HPApplication hPApplication, Context context, String[] datas, LrcActivity.LrcActivityListener lrcActivityListener) {
        this.mLrcActivityListener = lrcActivityListener;
        this.mHPApplication = hPApplication;
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_lvitem_lrc_popsinger, null, false);
        LrcPopSingerListViewHolder holder = new LrcPopSingerListViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LrcPopSingerListViewHolder && position < mDatas.length) {
            String singerName = mDatas[position];
            reshViewHolder(position, (LrcPopSingerListViewHolder) viewHolder, singerName);
        }
    }

    /**
     * 刷新
     *
     * @param position
     * @param viewHolder
     * @param singerName
     */
    private void reshViewHolder(final int position, final LrcPopSingerListViewHolder viewHolder, final String singerName) {
        //加载歌手图片
        ImageUtil.loadSingerImage(mHPApplication, mContext, viewHolder.getSingPicImg(), singerName);

        viewHolder.getSingerNameTv().setText(singerName);
        //item点击事件
        viewHolder.getListItemRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLrcActivityListener != null) {
                    mLrcActivityListener.closeSingerPopListVeiw(singerName);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mDatas.length;
    }


    /////////////////////////////////////////////////////

    class LrcPopSingerListViewHolder extends RecyclerView.ViewHolder {
        private View view;
        /**
         * item底部布局
         */
        private PopListItemRelativeLayout listItemRelativeLayout;

        /**
         * 歌手头像按钮
         */
        private CircleImageView singPicImg;

        /**
         * 歌手名称
         */
        private TextView singerNameTv;


        public LrcPopSingerListViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public PopListItemRelativeLayout getListItemRelativeLayout() {
            if (listItemRelativeLayout == null) {
                listItemRelativeLayout = view.findViewById(R.id.itemBG);
            }
            return listItemRelativeLayout;
        }

        public TextView getSingerNameTv() {
            if (singerNameTv == null) {
                singerNameTv = view.findViewById(R.id.singerName);
            }
            return singerNameTv;
        }


        public CircleImageView getSingPicImg() {
            if (singPicImg == null) {
                singPicImg = view.findViewById(R.id.singPic);
            }
            return singPicImg;
        }

    }

}
