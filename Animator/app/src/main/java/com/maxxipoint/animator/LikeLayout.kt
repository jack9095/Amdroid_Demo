package com.maxxipoint.animator

import android.animation.*
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.maxxipoint.animator.LikeLayout
import java.lang.ref.WeakReference
import java.util.*


class LikeLayout : FrameLayout {

    var onLikeListener: () -> Unit = {}     //屏幕点赞后，点赞按钮需同步点赞
    var onPauseListener: () -> Unit = {}     //暂停 或者 继续播放

    lateinit var imageView: ImageView
    lateinit var top: ImageView
    lateinit var left: ImageView
    lateinit var right: ImageView
    lateinit var leftBottom: ImageView
    lateinit var rightBottom: ImageView

    var window_width = 0 // 屏幕宽度
    var window_height = 0  // 屏幕高度
    val image_width = 60 // 小红心心的宽度

    constructor(context: Context) : super(context){
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView()
    }

    private var icon: Drawable = resources.getDrawable(R.mipmap.ic_heart)
    private var mClickCount = 0     //点击一次是暂停，多次是点赞
    private val mHandler = LikeLayoutHandler(this)

    init {
        clipChildren = false        //避免旋转时红心被遮挡
    }

    private fun initView() {
        val viewRoot = LayoutInflater.from(context).inflate(R.layout.animator_layout,this,true)
        imageView = viewRoot.findViewById(R.id.icon)
        top = findViewById(R.id.shop) // 顶部的图片控件
        left = findViewById(R.id.news) // 左边的图片控件
        right = findViewById(R.id.fans) // 右边的图片控件
        leftBottom = findViewById(R.id.trails) // 左下角图片控件
        rightBottom = findViewById(R.id.expression) // 右下角图片控件
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {     // 按下时在Layout中生成红心
            val x = event.x
            val y = event.y
            mClickCount++
            mHandler.removeCallbacksAndMessages(null)
            if (mClickCount >= 2) { // 双击事件
                addHeartView(x, y)  // 在Layout中添加红心并，播放消失动画
                onLikeListener()
                mHandler.sendEmptyMessageDelayed(1, 500)
            } else { // 单击事件
                mHandler.sendEmptyMessageDelayed(0, 500)
            }

        }
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

    lateinit var lp: LayoutParams
    /**
     * 在Layout中添加红心并，播放消失动画
     */
    private fun addHeartView(x: Float, y: Float) {

        lp = LayoutParams(icon.intrinsicWidth, icon.intrinsicHeight)    //计算点击的点位红心的下部中间

        lp.leftMargin = (x - icon.intrinsicWidth / 2).toInt()
        lp.topMargin = (y - icon.intrinsicHeight/2).toInt()

        Log.e("点击左上角X坐标 = ","${lp.leftMargin}")
        Log.e("点击左上角Y坐标 = ","${lp.topMargin}")
        Log.e("点击图片的宽 = ","${icon.intrinsicWidth}")
        Log.e("点击图片的高 = ","${icon.intrinsicHeight}")
        window_height = y.toInt();//Math.abs(lp.topMargin) + icon.intrinsicHeight
        window_width =x.toInt();// Math.abs(lp.leftMargin) + icon.intrinsicWidth

//        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.MATRIX  // 使用图像矩阵缩放
        val matrix = Matrix()
        matrix.postRotate(getRandomRotate())       //设置红心的微量偏移
        imageView.imageMatrix = matrix   // 给 ImageView 设置矩阵
        imageView.setImageDrawable(icon)  // 给 ImageView 设置展示的图片
        imageView.layoutParams = lp

        // 这部分代码可使 下层的 5个图片控件跟着心形的坐标走
        val lpView = LayoutParams(0, 0)    //计算点击的点位红心的下部中间
        lpView.leftMargin = (x - image_width / 2).toInt()
        lpView.topMargin = (y - image_width/2).toInt()
        top.layoutParams = lpView
        left.layoutParams = lpView
        right.layoutParams = lpView
        leftBottom.layoutParams = lpView
        rightBottom.layoutParams = lpView

        top.alpha = 1f
        left.alpha = 1f
        right.alpha = 1f
        leftBottom.alpha = 1f
        rightBottom.alpha = 1f
//        addView(imageView)

        val animSet = getShowAnimSet(imageView)
        val hideSet = getHideAnimSet(imageView)
        animSet.start()
        animSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                hideSet.start()
            }
        })
        hideSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
//                removeView    //动画结束移除红心
//                imageView.visibility = View.GONE
//
                top.alpha = 0f
                left.alpha = 0f
                right.alpha = 0f
                leftBottom.alpha = 0f
                rightBottom.alpha = 0f
            }
        })
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

        // 底下动画开始
        top.alpha = 1f
        left.alpha = 1f
        right.alpha = 1f
        leftBottom.alpha = 1f
        rightBottom.alpha = 1f

        val startangle = 54 // 角度 方向 +1 正方向，-1 负方向。

        // Math.PI 圆周率 3.141592653589793
        // 圆周率乘以72°再除以180°是:  Math.sin(72 * Math.PI / 180
        // 是72角对应的弧度值
        val length = ((600
                / Math.sin(72 * Math.PI / 180) - image_width) / 2 - image_width / 5).toInt()

        // 右下角图片设置动画
        val translationRightBottom = zhixian(
            rightBottom, startangle,
            length
        )
        val alphaRightBottom = ObjectAnimator.ofFloat(rightBottom, "alpha", 1f, 0.0f)
        // 左下角图片设置动画
        val translationLeftBottom = zhixian(
            leftBottom, startangle + 72,
            length
        )
        val alphaLeftBottom = ObjectAnimator.ofFloat(leftBottom, "alpha", 1f, 0.0f)
        // 左边图片设置动画
        val translationLeft = zhixian(
            left, startangle + 72 * 2,
            length
        )
        val alphaLeft = ObjectAnimator.ofFloat(left, "alpha", 1f, 0.0f)
        // 顶部图片设置动画
        val translationTop = zhixian(
            top, startangle + 72 * 3,
            length
        )
        val alphaTop = ObjectAnimator.ofFloat(top, "alpha", 1f, 0.0f)
        // 右边图片设置动画
        val translationRight = zhixian(
            right, startangle + 72 * 4,
            length
        )
        val alphaRight = ObjectAnimator.ofFloat(right, "alpha", 1f, 0.0f)
        // 底下动画 end

        val animSet = AnimatorSet()
        animSet.playTogether(alpha, scaleX, scaleY, translation,alphaTop,translationTop, alphaRight,translationRight,
            alphaLeft,translationLeft, alphaRightBottom, translationRightBottom, alphaLeftBottom,translationLeftBottom)
        animSet.duration = 500
        return animSet
    }

    /**
     * 生成一个随机的左右偏移量
     */
    private fun getRandomRotate(): Float = (Random().nextInt(40) - 10).toFloat()

    companion object {
        private class LikeLayoutHandler(view: LikeLayout) : Handler() {
            private val mView = WeakReference(view)

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when(msg?.what){
                    0-> mView.get()?.pauseClick()
                    1-> mView.get()?.onPause()
                }
            }
        }
    }

    /**
     * 沿直线运动。
     *
     * @param view  要移动的对象。
     * @param angle  方向 +1 正方向，-1 负方向。
     * @param length 移动的距离
     */
    fun zhixian(view: View, angle: Int, length: Int): ValueAnimator? {
        val valueAnimator = ValueAnimator()
        // 设置Object型变化值，一般设置初始值和结束值，当然你也可以设置中间值，因为这是一个可变参数，长度可变
        valueAnimator.setObjectValues(PointF(0f, 0f))
//        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.interpolator = AccelerateInterpolator()

        // 设置动画的估值器
        valueAnimator.setEvaluator { fraction, startValue, endValue ->

            // fraction = t / duration
            Log.v("znz", "znz ---> $fraction")
            val point = PointF()
            point.x = (fraction * length * Math.cos(
                angle * Math.PI
                        / 180
            )).toFloat()
            point.y = (fraction * length * Math.sin(
                angle * Math.PI
                        / 180
            )).toFloat()
            point
        }
        valueAnimator.addUpdateListener { animation ->

            /**
             * 通过这样一个监听事件，我们就可以获取
             * 到ValueAnimator每一步所产生的值。
             *
             * 通过调用getAnimatedValue()获取到每个时间因子所产生的Value。
             */
            val point = animation.animatedValue as PointF // 获取到每个时间因子所产生的Value
            val params = FrameLayout.LayoutParams(image_width, image_width)
            params.leftMargin = (point.x.toInt()
                    + (window_width  - image_width / 2))  //  X 坐标
            params.topMargin = (point.y.toInt()
                    + (window_height  - image_width / 2)) //  Y 坐标
            Log.e("znz", "point.x ---> " + point.x)
            Log.e("znz", "point.y ---> " + point.y)
            view.layoutParams = params
        }
        return valueAnimator
    }
}