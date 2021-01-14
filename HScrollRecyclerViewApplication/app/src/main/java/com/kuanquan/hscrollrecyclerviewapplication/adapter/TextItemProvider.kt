package com.kuanquan.hscrollrecyclerviewapplication.adapter

import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.hscrollrecyclerviewapplication.R
import com.kuanquan.hscrollrecyclerviewapplication.bean.TestBean

class TextItemProvider() : BaseItemProvider<TestBean>() {

    override val itemViewType: Int = TestBean.TYPE_NORMAL

    override val layoutId: Int =
        R.layout.item_banner_image

    override fun convert(helper: BaseViewHolder, item: TestBean) {

    }
}