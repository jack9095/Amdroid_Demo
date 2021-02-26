package com.kuanquan.emotionkeyboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.emotionkeyboard.activity.EditTextActivity
import com.kuanquan.emotionkeyboard.activity.ListViewBarEditActivity

class MainActivity : AppCompatActivity() {

    private var btn_main_editText: Button? = null
    private var btn_main_listView: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initListener()
    }

    /**
     * 初始化控件
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        btn_main_editText = findViewById(R.id.btn_main_editText)
        btn_main_listView = findViewById(R.id.btn_main_listView)
        val tv = findViewById<TextView>(R.id.tv)

//        tv.text = "\u0001F602"
//        tv.text = "\u1F603"
//        tv.text = "这是一个表情\uD83D\uDE01哈哈哈哈"

        Log.e("表情 -> ", tv.text.toString())
        val emojiStringByUnicode = getEmojiStringByUnicode(0x1F601)

        if (emojiStringByUnicode != null) {
//            Log.e("表情", emojiStringByUnicode)
//            Log.e("表情", "${emojiStringByUnicode.toInt()}")
        }
//        tv.text = emojiStringByUnicode
//        tv.text = getEmojiStringByUnicode(128512)
        tv.text = getEmojiStringByUnicode(128515) + getEmojiStringByUnicode(128516)
    }

    fun getEmojiStringByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    /**
     * 初始化监听器
     */
    private fun initListener() {
        btn_main_editText!!.setOnClickListener {
            val intentE = Intent(this@MainActivity, EditTextActivity::class.java)
            startActivity(intentE)
        }
        btn_main_listView?.setOnClickListener {
            val intentL = Intent(this@MainActivity, ListViewBarEditActivity::class.java)
            startActivity(intentL)
        }
    }
}