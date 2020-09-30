package com.kuanquan.weixinedit.adapter

import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.kuanquan.weixinedit.adapter.provider.ImgItemProvider
import com.kuanquan.weixinedit.model.ProviderMultiEntity
import com.kuanquan.weixinedit.adapter.provider.TextItemProvider

class CircleAdapter(): BaseProviderMultiAdapter<ProviderMultiEntity>() {

    init {
        addItemProvider(TextItemProvider())
        addItemProvider(ImgItemProvider())
    }

    /**
     * 自行根据数据、位置等内容，返回 item 类型
     * @param data
     * @param position
     * @return
     */
    override fun getItemType(data: List<ProviderMultiEntity>, position: Int): Int {
        val providerMultiEntity = data[position]
        when (providerMultiEntity.type) {
            0 -> return ProviderMultiEntity.TYPE_TEXT
            1 -> return ProviderMultiEntity.TYPE_IMAGE
            else -> {
            }
        }
        return 0
    }


}