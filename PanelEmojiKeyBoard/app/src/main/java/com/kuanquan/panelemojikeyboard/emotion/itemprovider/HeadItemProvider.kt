package com.kuanquan.panelemojikeyboard.emotion.itemprovider

import android.widget.EditText
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.panelemojikeyboard.R
import com.kuanquan.panelemojikeyboard.emotion.EmotionBean
import com.kuanquan.panelemojikeyboard.emotion.LatelyEmotionRecyclerView

class HeadItemProvider(val editText: EditText?, val width: Int, val height: Int) : BaseItemProvider<EmotionBean>() {
    override val itemViewType: Int
        get() = EmotionBean.TYPE_HEAD
    override val layoutId: Int
        get() = R.layout.head_emotion_item_layout

    var recyclerView: LatelyEmotionRecyclerView? = null

    override fun convert(helper: BaseViewHolder, item: EmotionBean) {
        recyclerView = helper.getView(R.id.recyclerView)
        recyclerView?.buildEmotionViews(
            editText,
            item.childList,
            width, height
        )
    }

    fun setLatelyEmotionItemClickListener(listener: LatelyEmotionRecyclerView.LatelyEmotionItemClickListener) {
        recyclerView?.setLatelyEmotionItemClickListener(listener)
    }

}