package com.kuanquan.panelemojikeyboard.emotion

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.kuanquan.panelemojikeyboard.emotion.itemprovider.CommentItemProvider
import com.kuanquan.panelemojikeyboard.emotion.itemprovider.HeadItemProvider
import com.kuanquan.panelemojikeyboard.util.DisplayUtils
import com.kuanquan.panelemojikeyboard.util.SpUtil
import java.util.*

class EmotionRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {
    private var sNumColumns = 0
    private var sNumRows = 0
    private var sPadding = 0
    private var sEmotionSize = 0
    private var currentWidth = -1
    private var currentHeight = -1
    private var mAdapter: Adapter? = null
    private val linkList = LinkedList<String>()

    private val gridLayoutManager by lazy {
        GridLayoutManager(context, sNumColumns).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val data = mAdapter?.getItem(position)
                    return if (data is EmotionBean && EmotionBean.TYPE_HEAD == data.type) 1 else sNumColumns
                }
            }
        }
    }

    fun buildEmotionViews(editText: EditText?, data: MutableList<EmotionBean>?, width: Int, height: Int) {
        if (data == null || data.isEmpty() || editText == null) {
            return
        }
        if (currentWidth == width && currentHeight == height) {
            return
        }
        currentWidth = width
        currentHeight = height
        val emotionViewContainSize = calSizeForContainEmotion(context, currentWidth, currentHeight)
        if (emotionViewContainSize == 0) return

        Log.e("EmotionRecyclerView", "掉用时机")

        // 存储最近使用的数据
        val latelyData = SpUtil.getInstace(context).getString("latelyKey", "")
        if (!TextUtils.isEmpty(latelyData)) {
            val split = latelyData.split(",")
//            val mutableList = mutableListOf<EmotionBean>()
            split.forEach {
                linkList.addLast(it)
//                mutableList.add(EmotionBean(unicode = it))
            }

//            mutableList.reverse()
//            Log.e("EmotionRv size -> ", "${mutableList.size}")
//            data.add(EmotionBean(childList = mutableList, type = EmotionBean.TYPE_HEAD))

        }


//        layoutManager = GridLayoutManager(context, sNumColumns)
        layoutManager = gridLayoutManager
        setBackgroundColor(Color.WHITE)
        mAdapter = Adapter(editText, width, height)
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            val emotion = data[position]
            if (emotion.type == EmotionBean.TYPE_COMMENT) {

                if (linkList.size < 8) {
                    linkList.addFirst(emotion.unicode ?: "0")
                } else {
                    linkList.removeAt(7)
                    linkList.addFirst(emotion.unicode ?: "0")
                }

                val start = editText.selectionStart
                val editable = editText.editableText
                val emotionSpannable = getEmojiStringByUnicode(emotion.unicode?.toInt() ?: 0)
                editable.insert(start, emotionSpannable)
            }
        }
        mAdapter?.setList(data)
        adapter = mAdapter
    }

    fun upData(data: MutableList<EmotionBean>?) {
        mAdapter?.setList(data)
    }

    fun saveEmotion(){
        SpUtil.getInstace(context).save("latelyKey", parseListToStr(linkList))
    }

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
        return sb.toString()
    }

    private fun calSizeForContainEmotion(context: Context?, width: Int, height: Int): Int {
//        sPadding = DisplayUtils.dip2px(context, 5f)
        sEmotionSize = DisplayUtils.dip2px(context, 50f)
        sNumColumns = width / sEmotionSize
        sNumRows = height / sEmotionSize
        return sNumColumns * sNumRows
    }

    class Adapter(val editText: EditText?, val width: Int, val height: Int) : BaseProviderMultiAdapter<EmotionBean>() {

        init {
            addItemProvider(HeadItemProvider(editText, width, height))
            addItemProvider(CommentItemProvider())
        }

        override fun getItemType(data: List<EmotionBean>, position: Int): Int {
            return data[position].type
        }

    }

//    class Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.vh_emotion_item_layout) {
//
//        override fun convert(holder: BaseViewHolder, emotion: String) {
//            val textView = holder.getView<TextView>(R.id.textView)
////            val imageView = holder.getView<ImageView>(R.id.image)
////            imageView.setImageResource(emotion.drawableRes)
//
//            textView.text = getEmojiStringByUnicode(emotion.toInt())
//        }
//
//        fun getEmojiStringByUnicode16(unicode: String): String? {
//            return String(Character.toChars(Integer.parseInt(unicode, 16)))
//        }
//
//        fun getEmojiStringByUnicode(unicode: Int): String? {
//            return String(Character.toChars(unicode))
//        }
//    }

   private fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

}

