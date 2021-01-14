package com.kuanquan.hscrollrecyclerviewapplication.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.kuanquan.hscrollrecyclerviewapplication.bean.TestBean

class TestAdapter: BaseProviderMultiAdapter<TestBean>() {

    init {
        addItemProvider(ScrollItemProvider())
        addItemProvider(TextItemProvider())
    }

    override fun getItemType(data: List<TestBean>, position: Int): Int {
        return data[position].type
    }
}