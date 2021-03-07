package com.mumu.demo.emotion

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mumu.demo.R

class EmotionRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs){
    private var sNumColumns = 0
    private var sNumRows = 0
    private var sPadding = 0
    private var sEmotionSize = 0
    private var currentWidth = -1
    private var currentHeight = -1
    private var mAdapter: Adapter? = null
    private var mEditText: EditText? = null
    private val TAG = "EmotionRecyclerView"

    /**
     * 初始化设置数据
     */
    fun buildEmotionViews(editText: EditText?, data: MutableList<String>?, width: Int, height: Int) {
        if (data == null || data.isEmpty() || editText == null) {
            return
        }
        if (currentWidth == width && currentHeight == height) {
            return
        }
        mEditText = editText
        currentWidth = width
        currentHeight = height
        val emotionViewContainSize = calSizeForContainEmotion(context, currentWidth, currentHeight)
        if (emotionViewContainSize == 0) return

        val mSpacing = DisplayUtils.dip2px(context, 18f)
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = mSpacing
            }
        })

        layoutManager = GridLayoutManager(context, sNumColumns)
        setBackgroundColor(Color.WHITE)
        mAdapter = Adapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val emotion = data[position]
                adapterItemClickEvent(emotion, mEditText)
            }
        }
        mAdapter?.setList(data)
        adapter = mAdapter
    }

    /**
     * 点击 item 表情
     */
    private fun adapterItemClickEvent(unicode: String?, editText: EditText?){
        val start = editText?.selectionStart ?: 0
        val editable = editText?.editableText
        val emotionSpannable = getEmojiStringByUnicode(unicode?.toInt() ?: 0)
        editable?.insert(start, emotionSpannable)
    }

    /**
     * 集合转数组
     */
    private fun parseListToStr(list: List<String>?): String? {
        val sb = StringBuffer()
        if (list != null && list.isNotEmpty()) {
            for (i in list.indices) {
                if (i < list.size - 1) {
                    sb.append(list[i] + ",")
                } else {
                    sb.append(list[i])
                }
            }
        }
        Log.e(TAG, "存入的数据 -》$sb")
        return sb.toString()
    }

    private fun calSizeForContainEmotion(context: Context?, width: Int, height: Int): Int {
//        sPadding = DisplayUtils.dip2px(context, 5f)
        sEmotionSize = DisplayUtils.dip2px(context, 50f)
        sNumColumns = width / sEmotionSize
        sNumRows = height / sEmotionSize
        return sNumColumns * sNumRows
    }


    inner class Adapter : BaseQuickAdapter<String?, BaseViewHolder>(R.layout.vh_emotion_item_layout) {
        override fun convert(holder: BaseViewHolder, emotion: String?) {
            val textView = holder.getView<TextView>(R.id.textView)
            textView.text = getEmojiStringByUnicode(emotion?.toInt() ?: 0)
        }
    }

    /**
     * 数字转换成 Emoji 表情
     */
    private fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

}

