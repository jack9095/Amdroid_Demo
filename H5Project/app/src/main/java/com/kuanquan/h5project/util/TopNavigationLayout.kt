package com.kuanquan.h5project.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.enjoy.whole.family.util.CollectionsUtil.setTextView
import com.kuanquan.h5project.R
import kotlinx.android.synthetic.main.layout_toobar.view.*

class TopNavigationLayout: FrameLayout {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context:Context,attrs:AttributeSet,defStyleAttr:Int):super(context,attrs,defStyleAttr) {
        init()
    }

    private fun init(){
        LayoutInflater.from(context).inflate(R.layout.layout_toobar, this, true)
    }

    fun setClick(listener: OnClickListener?) {
        root_rl.setOnClickListener(listener)
        iv_back.setOnClickListener(listener)
        tv_right.setOnClickListener(listener)
    }

    fun getRightTextView(): TextView? {
        return tv_right
    }

    fun setRightTextView(str: String?) {
        setTextView(tv_right, str)
    }

    fun setHintRightTextView(bean: Boolean) {
        if (bean) {
            tv_right.visibility = View.GONE
        } else {
            tv_right.visibility = View.VISIBLE
        }
    }

    fun setHintLeftTextView(bean: Boolean) {
        if (bean) {
            iv_back.visibility = View.GONE
        } else {
            iv_back.visibility = View.VISIBLE
        }
    }

    // 标题
    fun setTvTitle(str: String?) {
        setTextView(tv_title, str)
    }

}