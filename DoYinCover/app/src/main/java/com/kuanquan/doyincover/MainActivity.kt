package com.kuanquan.doyincover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.doyincover.ChoiceCoverView.OnScrollBorderListener


class MainActivity : AppCompatActivity() {

    var mChoiceCover: ChoiceCoverView? = null
    var mCursor = 0
    var startDurationTime: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mChoiceCover = findViewById(R.id.choiceCover)

        mChoiceCover?.setOnScrollBorderListener(object : OnScrollBorderListener {
            override fun OnScrollBorder(start: Float, end: Float) {
                // 游标变更
                val startTIme = start.toInt()
                val result = startTIme / (mChoiceCover?.width as? Float)!!
//                mCursor = (result * mDuration) as Int
                startDurationTime = start.toString()
            }

            override fun onScrollStateChange() {}
        })
    }
}