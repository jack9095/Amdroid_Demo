package com.kuanquan.music_lyric.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuanquan.music_lyric.R

class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val lrcView = findViewById<LrcView>(R.id.lrcView)
//        lrcView.setLrc()
//        lrcView.setPlayer()
//        lrcView.init()
    }
}