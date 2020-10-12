package com.mouble.baselibrary.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.mouble.baselibrary.R
import kotlinx.android.synthetic.main.load.*


class LoadDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去掉默认的title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //去掉白色边角 我的小米手机在xml里设置 android:background="@android:color/transparent"居然不生效 //所以在代码里设置，不知道是不是小米手机的原因
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.load)
        tv.text = "正在加载....."
        linearLayout.background.mutate().alpha = 210
    }

}