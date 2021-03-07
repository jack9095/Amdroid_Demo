package com.mumu.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mumu.demo.CustomDialogFragment.*
//import com.mumu.demo.dialog.CustomDialogFragment.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button).setOnClickListener {
            newInstance().show(supportFragmentManager)
        }
    }
}