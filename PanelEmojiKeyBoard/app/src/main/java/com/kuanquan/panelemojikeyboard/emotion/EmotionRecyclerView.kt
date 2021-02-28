package com.kuanquan.panelemojikeyboard.emotion

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.kuanquan.panelemojikeyboard.emotion.itemprovider.CommentItemProvider
import com.kuanquan.panelemojikeyboard.emotion.itemprovider.HeadItemProvider
import com.kuanquan.panelemojikeyboard.util.DisplayUtils
import com.kuanquan.panelemojikeyboard.util.SpUtil
import java.util.*

class EmotionRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs), LatelyEmotionRecyclerView.LatelyEmotionItemClickListener {
    private var sNumColumns = 0
    private var sNumRows = 0
    private var sPadding = 0
    private var sEmotionSize = 0
    private var currentWidth = -1
    private var currentHeight = -1
    private var mAdapter: Adapter? = null
    private val linkList = LinkedList<String>()
    private var mEditText: EditText? = null
    private val TAG = "EmotionRecyclerView"

    private val gridLayoutManager by lazy {
        GridLayoutManager(context, sNumColumns).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val data = mAdapter?.getItem(position)
                    return if (data is EmotionBean && EmotionBean.TYPE_COMMENT == data.type) 1 else sNumColumns
                }
            }
        }
    }

    /**
     * 初始化设置数据
     */
    fun buildEmotionViews(editText: EditText?, data: MutableList<EmotionBean>?, width: Int, height: Int) {
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

        Log.e("EmotionRecyclerView", "掉用时机")

        // 获取存储最近使用的数据
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

        val mSpacing = DisplayUtils.dip2px(context, 18f)
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
//                val position = parent.getChildAdapterPosition(view)
//                val column = position % sNumColumns
//                outRect.left = column * mSpacing / sNumColumns
//                outRect.right = mSpacing - (column + 1) * mSpacing / sNumColumns
                outRect.bottom = mSpacing
            }
        })

//        layoutManager = GridLayoutManager(context, sNumColumns)
        layoutManager = gridLayoutManager
        setBackgroundColor(Color.WHITE)
        mAdapter = Adapter(editText, width, height).apply {
            setOnItemClickListener { adapter, view, position ->
                val emotion = data[position]
                if (emotion.type == EmotionBean.TYPE_COMMENT) {
                    adapterItemClickEvent(emotion.unicode, mEditText)
                }
            }
        }
        mAdapter?.setLatelyEmotionItemClickListener(this)
        mAdapter?.setList(data)
        adapter = mAdapter
    }

    /**
     * 点击 item 表情
     */
    private fun adapterItemClickEvent(unicode: String?, editText: EditText?){
        Log.e("linkList click -> ", "${linkList.size}")
        Log.e("linkList click1 -> ", "$sNumColumns")
        if (linkList.contains(unicode)) {
            linkList.remove(unicode)
            linkList.addFirst(unicode ?: "0")
        } else {
            if (linkList.size < sNumColumns) {
                linkList.addFirst(unicode ?: "0")
            } else {
                linkList.removeLast()
                linkList.addFirst(unicode ?: "0")
            }
        }

        Log.e(TAG, "linkList -> $linkList")

        val start = editText?.selectionStart ?: 0
        val editable = editText?.editableText
        val emotionSpannable = getEmojiStringByUnicode(unicode?.toInt() ?: 0)
        editable?.insert(start, emotionSpannable)

        saveEmotion()
    }

    fun upData(data: MutableList<EmotionBean>?) {
        mAdapter?.setList(data)
    }

    fun saveEmotion() {
        Log.e("linkList -> ", "${linkList.size}")
//        linkList.forEach {
//            Log.e("linkList -> ", "${linkList.size}")
//        }
//        if (linkList.size > sNumColumns) {
//            val subList = linkList.subList(0, sNumColumns)
//            SpUtil.getInstace(context).save("latelyKey", parseListToStr(linkList.subList(0, sNumColumns)))
//        } else {
            SpUtil.getInstace(context).save("latelyKey", parseListToStr(linkList))
//        }
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

    inner class Adapter(val editText: EditText?, val width: Int, val height: Int) : BaseProviderMultiAdapter<EmotionBean>() {
        var headItemProvider = HeadItemProvider(editText, width, height)
        init {
            addItemProvider(headItemProvider)
            addItemProvider(CommentItemProvider())
        }

        fun setLatelyEmotionItemClickListener(listener: LatelyEmotionRecyclerView.LatelyEmotionItemClickListener) {
            headItemProvider.setLatelyEmotionItemClickListener(listener)
        }

        override fun getItemType(data: List<EmotionBean>, position: Int): Int {
            return data[position].type
        }

    }

    /**
     * 数字转换成 Emoji 表情
     */
    private fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    override fun onItemClick(data: String?) {
        adapterItemClickEvent(data, mEditText)
    }

}

