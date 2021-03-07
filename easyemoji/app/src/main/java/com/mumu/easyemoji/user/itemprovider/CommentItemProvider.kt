package com.mumu.easyemoji.user.itemprovider

import android.widget.TextView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mumu.easyemoji.R
import com.mumu.easyemoji.user.EmotionBean

class CommentItemProvider : BaseItemProvider<EmotionBean>() {
    override val itemViewType: Int
        get() = EmotionBean.TYPE_COMMENT
    override val layoutId: Int
        get() = R.layout.vh_emotion_item_layout

    override fun convert(helper: BaseViewHolder, item: EmotionBean) {
        val textView = helper.getView<TextView>(R.id.textView)
//            val imageView = holder.getView<ImageView>(R.id.image)
//            imageView.setImageResource(emotion.drawableRes)

        textView.text = getEmojiStringByUnicode(item.unicode?.toInt() ?: 0)
    }

    fun getEmojiStringByUnicode16(unicode: String): String? {
        return String(Character.toChars(Integer.parseInt(unicode, 16)))
    }

    fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }
}