package com.kuanquan.panelemojikeyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)

//        tv.text = "\u0001F602"
//        tv.text = "\u1F603"


//        tv.text = "\uD83D\uDE01"

        Log.e("表情 -> ", tv.text.toString())
        val emojiStringByUnicode = getEmojiStringByUnicode(0x1F601)
//        tv.text = getEmojiStringByUnicode(65039)
//        tv.text = getEmojiStringByUnicode(128513)
        tv.text = getEmojiStringByUnicode16("1f601")

        if (emojiStringByUnicode != null) {
//            Log.e("表情", emojiStringByUnicode)
//            Log.e("表情", "${emojiStringByUnicode.toInt()}")
        }
//        tv.text = emojiStringByUnicode
    }

    fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    fun getEmojiStringByUnicode16(unicode: String): String? {
        return String(Character.toChars(Integer.parseInt(unicode, 16)))
    }

}