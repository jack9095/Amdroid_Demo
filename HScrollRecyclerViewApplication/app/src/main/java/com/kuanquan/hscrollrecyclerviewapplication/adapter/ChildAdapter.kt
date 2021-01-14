package com.kuanquan.hscrollrecyclerviewapplication.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.hscrollrecyclerviewapplication.R

class ChildAdapter(list: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_child_layout, list) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv, item)
    }
}