package com.kuanquan.botttomsheetdialog

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class TestAdapter(data: MutableList<TestData>): BaseQuickAdapter<TestData, BaseViewHolder>(R.layout.dialog_fragment_layout_item, data) {

    override fun convert(holder: BaseViewHolder, item: TestData) {

        holder.getView<TextView>(R.id.nickNameTv).text = item.nickname
        holder.getView<TextView>(R.id.contentTv).text = item.content

    }

}