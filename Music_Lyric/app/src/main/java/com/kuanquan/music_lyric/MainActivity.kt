package com.kuanquan.music_lyric

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuanquan.music_lyric.demo.LrcView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lrcView = findViewById<LrcView>(R.id.lrcView)
    }
}