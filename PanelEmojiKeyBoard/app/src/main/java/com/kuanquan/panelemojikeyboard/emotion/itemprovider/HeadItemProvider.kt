package com.kuanquan.panelemojikeyboard.emotion.itemprovider

import android.widget.EditText
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.panelemojikeyboard.R
import com.kuanquan.panelemojikeyboard.emotion.EmotionBean
import com.kuanquan.panelemojikeyboard.emotion.LatelyEmotionRecyclerView

class HeadItemProvider(val editText: EditText?, val width: Int, val height: Int) : BaseItemProvider<EmotionBean>() {
    var mLatelyEmotionItemClickListener: LatelyEmotionRecyclerView.LatelyEmotionItemClickListener? = null
    override val itemViewType: Int
        get() = EmotionBean.TYPE_HEAD
    override val layoutId: Int
        get() = R.layout.head_emotion_item_layout

    override fun convert(helper: BaseViewHolder, item: EmotionBean) {
       val recyclerView = helper.getView<LatelyEmotionRecyclerView>(R.id.recyclerView)
        recyclerView.buildEmotionViews(
            editText,
            item.childList,
            width, height
        )
        recyclerView.setLatelyEmotionItemClickListener(mLatelyEmotionItemClickListener)
    }

    fun setLatelyEmotionItemClickListener(listener: LatelyEmotionRecyclerView.LatelyEmotionItemClickListener) {
        mLatelyEmotionItemClickListener = listener
    }

}