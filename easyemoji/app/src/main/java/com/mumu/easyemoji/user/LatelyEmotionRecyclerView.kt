package com.mumu.easyemoji.user

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mumu.easyemoji.R
import com.mumu.easyemoji.user.itemprovider.DisplayUtils

class LatelyEmotionRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {
    private var sNumColumns = 0
    private var sNumRows = 0
    private var sEmotionSize = 0
    private var currentWidth = -1
    private var currentHeight = -1
    private var mAdapter: Adapter? = null

    fun buildEmotionViews(editText: EditText?, data: MutableList<EmotionBean>?, width: Int, height: Int) {
        if (data == null || data.isEmpty() || editText == null) {
            return
        }

        currentWidth = width
        currentHeight = height
        val emotionViewContainSize = calSizeForContainEmotion(context, currentWidth, currentHeight)
        if (emotionViewContainSize == 0) return

        layoutManager = GridLayoutManager(context, sNumColumns)
        setBackgroundColor(Color.WHITE)
        mAdapter = Adapter()
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val emotion = data[position]
            if (emotion.type == EmotionBean.TYPE_COMMENT) {

                mLatelyEmotionItemClickListener?.onItemClick(emotion.unicode)

//                val start = editText.selectionStart
//                val editable = editText.editableText
//                val emotionSpannable = getEmojiStringByUnicode(emotion.unicode?.toInt() ?: 0)
//                editable.insert(start, emotionSpannable)
            }
        }
        mAdapter?.setList(data)
        adapter = mAdapter
    }

    private fun calSizeForContainEmotion(context: Context?, width: Int, height: Int): Int {
        sEmotionSize = DisplayUtils.dip2px(context, 50f)
        sNumColumns = width / sEmotionSize
        sNumRows = height / sEmotionSize
        return sNumColumns * sNumRows
    }

    inner class Adapter : BaseQuickAdapter<EmotionBean, BaseViewHolder>(R.layout.vh_emotion_item_layout) {

        override fun convert(holder: BaseViewHolder, emotion: EmotionBean) {
            val textView = holder.getView<TextView>(R.id.textView)
            textView.text = getEmojiStringByUnicode(emotion.unicode?.toInt() ?: 0)
        }
    }

    private fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    private var mLatelyEmotionItemClickListener: LatelyEmotionItemClickListener? = null

    fun setLatelyEmotionItemClickListener(listener: LatelyEmotionItemClickListener?) {
        mLatelyEmotionItemClickListener = listener
    }

    interface LatelyEmotionItemClickListener {
        fun onItemClick(data: String?)
    }
}