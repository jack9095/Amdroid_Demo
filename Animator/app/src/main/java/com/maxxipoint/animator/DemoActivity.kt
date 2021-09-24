package com.maxxipoint.animator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible


class DemoActivity: AppCompatActivity() {

    private var textView: TextView? = null
    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
//        textView?.alpha = 0.0f
    }

    /**
     * View 淡出，3秒后向上平移淡出消失
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun animator(){
        // 1.alpha动画 出现
        val appearAlpha = ObjectAnimator.ofFloat(textView, "alpha", 0.0f, 1.0f)
        //设置匀速旋转
//        appearAlpha.interpolator = LinearInterpolator()
        //设置无限循环
//        appearAlpha.repeatCount = Animation.INFINITE
        appearAlpha.start()

        // 2.alpha动画 消失
        val disappearAlpha = ObjectAnimator.ofFloat(textView, "alpha", 1.0f, 0.0f)
        disappearAlpha.startDelay = 3000
        // 3.translation动画
        val translation = ObjectAnimator.ofFloat(textView, "translationY", 0f, -150f)
        translation.startDelay = 3000

        val animSet = AnimatorSet()
//        animSet.playTogether(appearAlpha, translation, disappearAlpha)
//        animSet.play(appearAlpha).before(translation).with(disappearAlpha)
        animSet.play(translation).with(disappearAlpha)
        //animSet.playTogether(iv1,iv2,iv3);//三个组件一起执行
//        animSet.playSequentially(iv1,iv2,iv3);//一个接着一个执行
        animSet.duration = 500
        animSet.start()
    }

    /**
     * 图片缩小放大
     */
    @SuppressLint("ObjectAnimatorBinding")
    private fun animatorScale(){
        // 缩放动画
        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.8f)
        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.8f)

        //设置无限循环
//        scaleX.repeatCount = Animation.INFINITE
//        scaleY.repeatCount = Animation.INFINITE


        val scaleXD = ObjectAnimator.ofFloat(imageView, "scaleX", 0.8f, 1f)
        scaleXD.startDelay = 700
//        scaleXD.repeatCount = Animation.INFINITE
        val scaleYD = ObjectAnimator.ofFloat(imageView, "scaleY", 0.8f, 1f)
        scaleYD.startDelay = 700
//        scaleYD.repeatCount = Animation.INFINITE

        val animSet = AnimatorSet()
//        animSet.playTogether(scaleX,scaleY)
        animSet.play(scaleX).with(scaleY).with(scaleXD).with(scaleYD)
        animSet.duration = 700
        animSet.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            animSet.reverse()
        }

        animSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                animSet.start() // 间接达到循环播放的效果，这样会造成页面卡顿，建议不要这么使用
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })

//        imageView?.postDelayed({
//            val scaleXD = ObjectAnimator.ofFloat(imageView, "scaleX", 0.8f, 1f)
//            val scaleYD = ObjectAnimator.ofFloat(imageView, "scaleY", 0.8f, 1f)
//            val animSetD = AnimatorSet()
//            animSetD.playTogether(scaleXD, scaleYD)
//            animSetD.duration = 700
//            animSetD.start()
//        }, 700)
    }

    fun click(view: View) {
//        animator()
//        animatorScale()
//        imageView?.startAnimation(scaleAnimation())

        val appearAlpha = ObjectAnimator.ofFloat(textView, "alpha", 0.0f, 1.0f)
        //设置匀速旋转
//        appearAlpha.interpolator = LinearInterpolator()
        //设置无限循环
//        appearAlpha.repeatCount = Animation.INFINITE
        appearAlpha.startDelay = 150
        appearAlpha.start()

//        textView?.isVisible = true
    }

    /**
     * 图片自动循环缩放
     * 参数：第1~4：表示x/y缩放数据
     *      第5/7：表示缩放缩放的位置相对控件自身还是父容器
     *      第6/8：表示缩放的位置
     *      PS: 第5/7设置Animation.RELATIVE_TO_SELF，第6/8设置0.5f，表示相对控件自身中心点进行缩放
     * @return
     *
     * 使用：
     *      开始动画：imageView.startAnimation(scaleAnimation())
     *      停止动画：imageView.clearAnimation()
     */
    private fun scaleAnimation(): ScaleAnimation? {
        val scaleAnimation = ScaleAnimation(
            1.0f, 0.8f, 1.0f, 0.8f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 900 //动画执行时间
        scaleAnimation.repeatCount = -1 //-1表示重复执行动画
        scaleAnimation.repeatMode = Animation.REVERSE //重复 缩小和放大效果
        return scaleAnimation
    }
}