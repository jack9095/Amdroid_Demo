package com.kuanquan.pagetransitionanimation.elementspage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kuanquan.pagetransitionanimation.R
import com.kuanquan.pagetransitionanimation.animationlayout.TAnimationFrameLayout
import com.kuanquan.pagetransitionanimation.databinding.ActivityDetail3Binding

/**
 * 记录一个问题，当自定义组合控件，内部写事件分发的时候，子控件一定要写点击事件，不然自定义的组合控件的 onTouchEvent
 * 或者 onInterceptTouchEvent 不会走 MotionEvent.ACTION_MOVE 事件
 * 博客: https://blog.csdn.net/u012102149/article/details/100017162
 */
class DetailActivity3 : AppCompatActivity() {
    private lateinit var viewBinding: ActivityDetail3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetail3Binding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        val imageView: ImageView = findViewById(R.id.image)
        imageView.setOnClickListener {
            Toast.makeText(this@DetailActivity3, "toast", Toast.LENGTH_SHORT).show()
        }
        val url = intent.getStringExtra("url")
        Glide.with(this).load(url).into(imageView)

        val contentView: View = window.decorView.findViewById(android.R.id.content)
        viewBinding.flayout.setCurrentShowView(contentView)

//        viewBinding.flayout.setCurrentShowView(null)
        viewBinding.flayout.setIAnimClose(object : TAnimationFrameLayout.IAnimListener {
           override fun onRelease(view: View?) {
                onBackPressed()
            }

           override fun onMoveStart() {

            }

            override fun onMoveEnd() {

            }
        })
//        viewBinding.flayout.setScrollState(false)
//        viewBinding.flayout.setImageState(false)
    }
}