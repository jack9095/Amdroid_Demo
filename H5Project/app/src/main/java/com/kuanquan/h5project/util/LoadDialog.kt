package com.kuanquan.h5project.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import com.kuanquan.h5project.R
import kotlinx.android.synthetic.main.load.*

class LoadDialog(context: Context): Dialog(context) {
    private var tv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.load)
        tv = findViewById<View>(R.id.tv) as? TextView
        tv?.text = "正在加载....."
        linearLayout.background.alpha = 210
    }
}