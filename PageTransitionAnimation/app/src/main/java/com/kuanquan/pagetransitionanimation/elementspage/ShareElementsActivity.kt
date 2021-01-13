package com.kuanquan.pagetransitionanimation.elementspage

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kuanquan.pagetransitionanimation.AnimationFrameLayout
import com.kuanquan.pagetransitionanimation.R
import com.kuanquan.pagetransitionanimation.databinding.ActivityShareElementsBinding


class ShareElementsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityShareElementsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityShareElementsBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
//        if (Build.VERSION.SDK_INT >= 23) {
//            val decorView = window.decorView
//            window.statusBarColor = Color.TRANSPARENT
//            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            decorView.systemUiVisibility = option
//        }
        //将内部的布局 item_linear_layout，放入 AnimationFrameLayout 中
        val view = LayoutInflater.from(this).inflate(R.layout.item_linear_layout, viewBinding.frameLayout)
        val imageView = view.findViewById<ImageView>(R.id.image)
        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).into(imageView)
        viewBinding.frameLayout.setFinishListener(object : AnimationFrameLayout.FinishListener {
            override fun gofinish() {
//                finish()
//                finishAffinity()
//                finishAfterTransition()
                onBackPressed()
//                onBackPressedDispatcher
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        viewBinding.frameLayout.setBackgroundColor(Color.parseColor("#00000000"))
    }
}