package com.example.expandtextview.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.expandtextview.R;
import com.example.expandtextview.bean.LikeListBean;

import java.util.List;


/**
 * @作者: njb
 * @时间: 2019/7/16 19:20
 * @描述: 点赞列表适配器
 */
public class LikeListAdapter extends BaseQuickAdapter<LikeListBean, BaseViewHolder> {

    public LikeListAdapter(@Nullable List<LikeListBean> data) {
        super(R.layout.item_like_list,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikeListBean item) {
        if(item == null){
            return;
        }
        helper.setText(R.id.tv_like_name,item.getUser_name()+",");
        helper.addOnClickListener(R.id.tv_like_name);
    }
}
