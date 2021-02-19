package com.kuanquan.sinaweibopraise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.kuanquan.sinaweibopraise.glide.GlideApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView)
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)


        linearLayout.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                // 显示表情数据
                val lists = getData()
                SinaPraiseSelectView.attachView(imageView, lists, object : Callback<PraiseData> {
                    override fun callback(data: PraiseData?) {
                        // 选中表态回掉回来的数据
                        GlideApp.with(this@MainActivity)
                            .load(data?.iconUrl)
                            .into(imageView)
                    }
                }, object : Builder<PraiseData> {
                    override fun buildModel(item: EmojiItem<PraiseData>): EmojiItem<PraiseData> {
                        if (!TextUtils.isEmpty(item.data.iconUrl)) {
                            item.bitmapUrl = item.data.iconUrl ?: ""
                            item.titleText = item.data.desc ?: ""
                        }
                        return item
                    }
                })

                return true
            }
        })
    }
}