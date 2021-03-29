package com.kuanquan.nineluckpan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (findViewById<NineLuckPan>(R.id.luckpan)).onLuckPanAnimEndListener =
                NineLuckPan.OnLuckPanAnimEndListener { position, msg ->
            Toast.makeText(this@MainActivity, "位置："+position+"提示信息："+msg, Toast.LENGTH_SHORT).show()
        }
    }
}