package com.kuanquan.commentbanner.banner

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.commentbanner.R
import kotlinx.android.synthetic.main.adapter_layout.view.*

class BannerQuickAdapter(data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.adapter_layout, data) {

    override fun convert(holder: BaseViewHolder, item: String) {
//        val imageView: ImageView = holder.getView(R.id.imageView)
        Glide.with(holder.itemView)
            .load(item)
            .into(holder.itemView.imageView)
    }

}
