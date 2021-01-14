package com.kuanquan.hscrollrecyclerviewapplication.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.hscrollrecyclerviewapplication.R
import com.kuanquan.hscrollrecyclerviewapplication.bean.TestBean

class ScrollItemProvider : BaseItemProvider<TestBean>() {

    override val itemViewType: Int =
        TestBean.TYPE_SCROLL

    override val layoutId: Int =
        R.layout.item_scroll_layout

    override fun convert(helper: BaseViewHolder, item: TestBean) {
        helper.getView<RecyclerView>(R.id.leftRecyclerView).run {
            layoutManager = LinearLayoutManager(context)
            adapter = ChildAdapter(getData())
        }

        helper.getView<RecyclerView>(R.id.rightRecyclerView).run {
            layoutManager = LinearLayoutManager(context)
            adapter = ChildAdapter(getData())
        }
    }


    private fun getData(): MutableList<String>{
        val list = mutableListOf<String>()
        for (i in 0..3){
            list.add("火山小视频$i")
        }
        return list
    }
}