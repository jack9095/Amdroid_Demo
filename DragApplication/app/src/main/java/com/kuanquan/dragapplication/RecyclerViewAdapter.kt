package com.kuanquan.dragapplication

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.adapter_item_layout.view.*

class RecyclerViewAdapter(data: MutableList<DataBean>):
    BaseQuickAdapter<DataBean, BaseViewHolder>(R.layout.adapter_item_layout, data) {

    override fun convert(holder: BaseViewHolder, item: DataBean) {
        Glide.with(context).load(item.url)
//            .override(photoView.width, photoView.height)
//            .placeholder(photoView.drawable)
            .into(holder.itemView.imageView)
    }

}