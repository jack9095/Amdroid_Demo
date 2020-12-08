package com.maxxipoint.animator.copy

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.maxxipoint.animator.R
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


class UpVoteLayout : FrameLayout {

    var onLikeListener: () -> Unit = {}     //屏幕点赞后，点赞按钮需同步点赞
    var onPauseListener: () -> Unit = {}     //暂停 或者 继续播放

    lateinit var imageView: ImageView
    lateinit var top: ImageView
    lateinit var left: ImageView
    lateinit var right: ImageView
    lateinit var leftBottom: ImageView
    lateinit var rightBottom: ImageView

    private val imageViews = mutableListOf<ImageView>() // 圆形散开图片的集合
    private val radius = 200 // 圆形散开的半径

    constructor(context: Context) : super(context){
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }

    private var iconDrawable: Drawable? = ContextCompat.getDrawable(context,R.mipmap.ic_heart)
    private var mClickCount = 0     //点击一次是暂停，多次是点赞
    private val mHandler = LikeLayoutHandler(this)

    init {
        clipChildren = false        //避免旋转时红心被遮挡
    }

    private fun initView() {
        val viewRoot = LayoutInflater.from(context).inflate(R.layout.animator_layout,this,true)
        imageView = viewRoot.findViewById(R.id.icon)
        top = findViewById(R.id.shop) // 顶部的图片控件
        imageViews.add(top)
        left = findViewById(R.id.news) // 左边的图片控件
        imageViews.add(left)
        right = findViewById(R.id.fans) // 右边的图片控件
        imageViews.add(right)
        leftBottom = findViewById(R.id.trails) // 左下角图片控件
        imageViews.add(leftBottom)
        rightBottom = findViewById(R.id.expression) // 右下角图片控件
        imageViews.add(rightBottom)

        gestureDetector = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener() {

            override fun onDoubleTap(event: MotionEvent?): Boolean {
                Log.e("UpVoteLayout","双击事件")
                event?.let {
                    val x = it.x
                    val y = it.y
                    addHeartView(x, y)
                    onLikeListener()
                }
                return super.onDoubleTap(event)
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                Log.e("UpVoteLayout","单击事件")
                onPauseListener()
                return super.onSingleTapConfirmed(e)
            }
        })
    }

    private var gestureDetector: GestureDetector? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector?.onTouchEvent(event)
//        if (event?.action == MotionEvent.ACTION_DOWN) {     // 按下时在Layout中生成红心
//            val x = event.x
//            val y = event.y
//            mClickCount++
//            mHandler.removeCallbacksAndMessages(null)
//            if (mClickCount >= 2) { // 双击事件
//                addHeartView(x, y)  // 在Layout中添加红心并，播放消失动画
//                onLikeListener()
//                mHandler.sendEmptyMessageDelayed(1, 300)
//            } else { // 单击事件
//                mHandler.sendEmptyMessageDelayed(0, 300)
//            }
//
//        }
        return true
    }

    // 暂停播放事件
    private fun pauseClick() {
        if (mClickCount == 1) {
            onPauseListener()
        }
        mClickCount = 0
    }

    // 在 activity 或者 fragment 的 onPause() 方法中执行
    fun onPause() {
        mClickCount = 0
        mHandler.removeCallbacksAndMessages(null)
    }

    fun onDestroy(){
        onPause()
        animSet?.removeAllListeners()
        hideSet?.removeAllListeners()
    }

    private var animSet: AnimatorSet? = null
    var hideSet: AnimatorSet? = null
    /**
     * 在Layout中添加红心并，播放消失动画
     */
    private fun addHeartView(x: Float, y: Float) {

        iconDrawable?.run {
            val lp = LayoutParams(intrinsicWidth, intrinsicHeight)    //计算点击的点位红心的下部中间

            lp.leftMargin = (x - intrinsicWidth / 2).toInt()
            lp.topMargin = (y - intrinsicHeight).toInt()

//        Log.e("点击左上角X坐标 = ","${lp.leftMargin}")
//        Log.e("点击左上角Y坐标 = ","${lp.topMargin}")
//        Log.e("点击图片的宽 = ","${icon.intrinsicWidth}")
//        Log.e("点击图片的高 = ","${icon.intrinsicHeight}")

            val objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", getRandomRotate())
            val alpha = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0.0f)

            val animatorSet = AnimatorSet()
            animatorSet.duration = 2300
            animatorSet.playTogether(objectAnimator, alpha)
            animatorSet.start()

//        val imageView = ImageView(context)
//            imageView.scaleType = ImageView.ScaleType.MATRIX  // 使用图像矩阵缩放
//            val matrix = Matrix()
//            matrix.postRotate(getRandomRotate())       //设置红心的微量偏移
//            imageView.imageMatrix = matrix   // 给 ImageView 设置矩阵
            imageView.setImageDrawable(iconDrawable)  // 给 ImageView 设置展示的图片
            imageView.layoutParams = lp

            // 这部分代码可使 下层的 5个图片控件跟着心形的坐标走
            val lpView = LayoutParams(60, 60)    // 计算点击的点位红心的下部中间
//            lpView.leftMargin = (x - intrinsicWidth / 3).toInt()
//            lpView.topMargin = (y - intrinsicHeight*2 / 3).toInt()
            lpView.leftMargin = (x - intrinsicWidth / 3 + 60).toInt()
            lpView.topMargin = (y - intrinsicHeight * 2 / 3 - 40).toInt()
            imageViews.forEach {
                it.layoutParams = lpView
            }
//            top.layoutParams = lpView
//            left.layoutParams = lpView
//            right.layoutParams = lpView
//            leftBottom.layoutParams = lpView
//            rightBottom.layoutParams = lpView
//        addView(imageView)

            animSet = getShowAnimSet(imageView)
            hideSet = getHideAnimSet(imageView)
            animSet?.run {
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        hideSet?.start()
                    }
                })
            }
            hideSet?.addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    imageView.postDelayed({
                        imageViews.forEach {
                            it.alpha = 1f
                        }
                        // 底下动画开始
//                        top.alpha = 1f
//                        left.alpha = 1f
//                        right.alpha = 1f
//                        leftBottom.alpha = 1f
//                        rightBottom.alpha = 1f
                        showCircleAnimator()
                        // 底下动画 end
                    },100)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    imageViews.forEach {
                        it.alpha = 0f
                    }
//                removeView    // 动画结束移除红心
//                    top.alpha = 0f
//                    left.alpha = 0f
//                    right.alpha = 0f
//                    leftBottom.alpha = 0f
//                    rightBottom.alpha = 0f
                }
            })
        }
    }

    /**
     * 刚点击的时候的一个缩放效果
     */
    private fun getShowAnimSet(view: ImageView): AnimatorSet {
        // 缩放动画
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1f)
        val animSet = AnimatorSet()
        animSet.playTogether(scaleX, scaleY)
        animSet.duration = 300
        return animSet
    }

    /**
     * 缩放结束后到红心消失的效果
     */
    private fun getHideAnimSet(view: ImageView): AnimatorSet {
        // 1.alpha动画
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.0f)
        // 2.缩放动画
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 2f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2f)
        // 3.translation动画
        val translation = ObjectAnimator.ofFloat(view, "translationY", 0f, -150f)
        val animSet = AnimatorSet()
        animSet.playTogether(alpha, scaleX, scaleY, translation)
        animSet.duration = 500
        return animSet
    }

    /**
     * 生成一个随机的左右偏移量
     */
    private fun getRandomRotate(): Float = (Random().nextInt(30) - 10).toFloat()

    private fun getRandomRotate1(): Float {
        val items = arrayOf(-30, -15, 0, 15, 30)
        val a = floor(Math.random() * items.size).toInt()
        return items[a].toFloat()
    }

    companion object {
        private class LikeLayoutHandler(view: UpVoteLayout) : Handler() {
            private val mView = WeakReference(view)
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when(msg?.what){
                    0-> mView.get()?.pauseClick()
                    1-> mView.get()?.onPause()
                }
            }
        }
    }

    // 显示底层圆形扩散动画
    private fun showCircleAnimator() {
        for (i in imageViews.indices) {
            val point = PointF()
            val avgAngle: Int = 360 / imageViews.size
            val angle: Int = avgAngle * i
            point.x =
                cos(angle * (Math.PI / 180)).toFloat() * radius
            point.y =
                sin(angle * (Math.PI / 180)).toFloat() * radius
            Log.e("LikeLayout", "angle=$angle")
            val objectAnimatorX =
                ObjectAnimator.ofFloat(imageViews[i], "translationX", 0f, point.x)
            val objectAnimatorY =
                ObjectAnimator.ofFloat(imageViews[i], "translationY", 0f, point.y)
            val alpha = ObjectAnimator.ofFloat(imageViews[i], "alpha", 1f, 0.0f)
            val objectAnimator = ObjectAnimator.ofFloat(imageViews[i], "rotation", (angle + 72).toFloat())

            val animatorSet = AnimatorSet()
            animatorSet.duration = 500
            animatorSet.playTogether(objectAnimator,alpha, objectAnimatorX, objectAnimatorY)
            animatorSet.start()
        }
    }
}