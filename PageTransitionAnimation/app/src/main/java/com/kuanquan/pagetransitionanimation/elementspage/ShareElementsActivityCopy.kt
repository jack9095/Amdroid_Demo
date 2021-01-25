//package com.kuanquan.pagetransitionanimation.elementspage
//
//import android.R.transition
//import android.graphics.Color
//import android.os.Bundle
//import android.transition.Fade
//import android.transition.Transition
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.Window
//import android.widget.ImageView
//import android.widget.LinearLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.Group
//import com.bumptech.glide.Glide
//import com.kuanquan.pagetransitionanimation.AnimationFrameLayout
//import com.kuanquan.pagetransitionanimation.R
//import com.kuanquan.pagetransitionanimation.util.SineInterpolator
//import com.kuanquan.pagetransitionanimation.util.TransitionCallBack
//import com.kuanquan.pagetransitionanimation.databinding.ActivityShareElementsBinding
//
//
//class ShareElementsActivityCopy : AppCompatActivity() {
//    lateinit var viewBinding: ActivityShareElementsBinding
//    lateinit var rootView: View
//    override fun onCreate(savedInstanceState: Bundle?) {
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
//        super.onCreate(savedInstanceState)
//
//        viewBinding = ActivityShareElementsBinding.inflate(LayoutInflater.from(this))
//        setContentView(viewBinding.root)
////        if (Build.VERSION.SDK_INT >= 23) {
////            val decorView = window.decorView
////            window.statusBarColor = Color.TRANSPARENT
////            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
////            decorView.systemUiVisibility = option
////        }
//
////        val contentView = window?.decorView?.findViewById<View>(android.R.id.content)
////        contentView?.setBackgroundColor(Color.TRANSPARENT)
////
////        val decorView = window.decorView
////        decorView.setBackgroundColor(Color.TRANSPARENT)
//
//        //将内部的布局 item_linear_layout，放入 AnimationFrameLayout 中
//        val view = LayoutInflater.from(this).inflate(R.layout.item_linear_layout, viewBinding.frameLayout)
//        view.setBackgroundColor(Color.TRANSPARENT)
//        rootView = findViewById<LinearLayout>(R.id.parent)
////        rootView.setBackgroundColor(Color.TRANSPARENT)
//
//
//
//        val imageView = findViewById<ImageView>(R.id.image)
//        val url = intent.getStringExtra("url")
//        Glide.with(this).load(url).into(imageView)
//        viewBinding.frameLayout.setFinishListener(object : AnimationFrameLayout.FinishListener {
//            override fun gofinish() {
////                finish()
////                finishAffinity()
////                finishAfterTransition()
//                onBackPressed()
////                onBackPressedDispatcher
//            }
//
//            override fun setBackgroundColor(color: Int) {
////                Log.e("颜色 -> ", "$color")
//                rootView.setBackgroundColor(color)
//            }
//
//            override fun setRestitution(isRestitution: Boolean) {
//                if (isRestitution) {
//                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.VISIBLE
//                } else {
//                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.INVISIBLE
//                }
//            }
//        })
//
////        分解效果
////        window.enterTransition = Explode().setDuration(0)
////        window.exitTransition = Explode().setDuration(0)
//
////        滑动效果
////        window.enterTransition = Slide().setDuration(0)
////        window.exitTransition = Slide().setDuration(0)
//
////        val transition = Fade()
////        transition.startDelay = 0L
////        transition.duration = 80
////        transition.interpolator = SineInterpolator()
//
////        淡入淡出效果
////        window.enterTransition = transition
////        window.exitTransition = transition
//
//
//        // 回掉
////        setExitSharedElementCallback(TransitionCallBack())
//
////        addTransitionListener()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
////        overridePendingTransition(0,0)
////        viewBinding.frameLayout.setBackgroundColor(Color.parseColor("#00000000"))
//    }
//
//    private fun addTransitionListener() {
//        val transition = window.sharedElementEnterTransition
//        transition?.addListener(object : Transition.TransitionListener{
//            override fun onTransitionEnd(transition: Transition?) {
//                // 动画完成之后 处理你自己的逻辑
//                transition?.removeListener(this)
////                rootView.setBackgroundColor(Color.WHITE)
//            }
//
//            override fun onTransitionResume(transition: Transition?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTransitionPause(transition: Transition?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTransitionCancel(transition: Transition?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onTransitionStart(transition: Transition?) {
////                rootView.setBackgroundColor(Color.TRANSPARENT)
//            }
//
//        })
//    }
//}