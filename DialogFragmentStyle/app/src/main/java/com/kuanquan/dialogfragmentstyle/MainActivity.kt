package com.kuanquan.dialogfragmentstyle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class  MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.textview).setOnClickListener {
            ADialogFragment.newInstance("", "", "").show(supportFragmentManager, "history")
        }
    }
}