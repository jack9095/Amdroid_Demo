package com.kuanquan.circlemenu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.cos
import kotlin.math.sin

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private val imageViews = mutableListOf<ImageView>()
    private val imageViews2: MutableList<ImageView> = mutableListOf()
    private val radius1 = 500
    private val radius2 = 300

    private fun assignViews() {
        imageViews.add(iv8)
        imageViews.add(iv7)
        imageViews.add(iv6)
        imageViews.add(iv5)
        imageViews.add(iv4)
        imageViews.add(iv3)
        imageViews.add(iv2)

        imageViews2.add(iv18)
        imageViews2.add(iv17)
        imageViews2.add(iv16)
        imageViews2.add(iv15)
        imageViews2.add(iv14)
        imageViews2.add(iv13)
        imageViews2.add(iv12)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        assignViews()
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun onClick(v: View) {
        if (v == iv1) {
            val isShowing = iv1.tag as? Boolean ?: false
            if (!isShowing) {
                val objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 0f, 45f)
                objectAnimator.duration = 500
                objectAnimator.start()
                iv1!!.tag = true
                showSectorMenu()
            } else {
                iv1.tag = false
                val objectAnimator = ObjectAnimator.ofFloat(iv1, "rotation", 45f, 0f)
                objectAnimator.duration = 500
                objectAnimator.start()
                closeSectorMenu()
            }
        } else if (v.id == iv11.id) {
            val isShowing = iv11.tag as? Boolean ?: false
            if (!isShowing) {
                val objectAnimator = ObjectAnimator.ofFloat(iv11, "rotation", 0f, 45f)
                objectAnimator.duration = 500
                objectAnimator.start()
                iv11!!.tag = true
                showCircleMenu()
            } else {
                iv11.tag = false
                val objectAnimator = ObjectAnimator.ofFloat(iv11, "rotation", 45f, 0f)
                objectAnimator.duration = 500
                objectAnimator.start()
                closeCircleMenu()
            }
        } else {
            Toast.makeText(this, "点击了第" + (if (imageViews.indexOf(v) == -1) imageViews2.indexOf(v) else imageViews.indexOf(v)) + "个", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 显示扇形菜单
     */
    private fun showSectorMenu() {
        /***第一步，遍历所要展示的菜单ImageView */
        for (i in imageViews.indices) {
            val point = PointF()

            /***第二步，根据菜单个数计算每个菜单之间的间隔角度 */
            val avgAngle = 90 / (imageViews.size - 1)

            /**第三步，根据间隔角度计算出每个菜单相对于水平线起始位置的真实角度 */
            val angle = avgAngle * i
            Log.d(TAG, "angle=$angle")
            /**
             * 圆点坐标：(x0,y0)
             * 半径：r
             * 角度：a0
             * 则圆上任一点为：（x1,y1）
             * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
             * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )
             */
            /**第四步，根据每个菜单真实角度计算其坐标值 */
            point.x = cos(angle * (Math.PI / 180)).toFloat() * radius1
            point.y = (-sin(angle * (Math.PI / 180))).toFloat() * radius1
            Log.d(TAG, point.toString())
            /**第五步，根据坐标执行位移动画 */
            /**
             * 第一个参数代表要操作的对象
             * 第二个参数代表要操作的对象的属性
             * 第三个参数代表要操作的对象的属性的起始值
             * 第四个参数代表要操作的对象的属性的终止值
             */
            val objectAnimatorX = ObjectAnimator.ofFloat(imageViews[i], "translationX", 0f, point.x)
            val objectAnimatorY = ObjectAnimator.ofFloat(imageViews[i], "translationY", 0f, point.y)

            /**动画集合，用来编排动画 */
            val animatorSet = AnimatorSet()
            /**设置动画时长 */
            animatorSet.duration = 500
            /**设置同时播放x方向的位移动画和y方向的位移动画 */
            animatorSet.play(objectAnimatorX).with(objectAnimatorY)
            /**开始播放动画 */
            animatorSet.start()
        }
    }


    /**
     * 关闭扇形菜单
     */
    private fun closeSectorMenu() {
        for (i in imageViews.indices) {
            val point = PointF()
            val avgAngle = 90 / (imageViews.size - 1)
            val angle = avgAngle * i
            Log.d(TAG, "angle=$angle")
            point.x = cos(angle * (Math.PI / 180)).toFloat() * radius1
            point.y = (-sin(angle * (Math.PI / 180))).toFloat() * radius1
            Log.d(TAG, point.toString())
            val objectAnimatorX = ObjectAnimator.ofFloat(imageViews[i], "translationX", point.x, 0f)
            val objectAnimatorY = ObjectAnimator.ofFloat(imageViews[i], "translationY", point.y, 0f)
            val animatorSet = AnimatorSet()
            animatorSet.duration = 500
            animatorSet.play(objectAnimatorX).with(objectAnimatorY)
            animatorSet.start()
        }
    }


    /**
     * 显示圆形菜单
     */
    private fun showCircleMenu() {
        for (i in imageViews2.indices) {
            val point = PointF()
            val avgAngle = 360 / (imageViews2.size - 1)
            val angle = avgAngle * i
            Log.d(TAG, "angle=$angle")
            point.x = cos(angle * (Math.PI / 180)).toFloat() * radius2
            point.y = sin(angle * (Math.PI / 180)).toFloat() * radius2
            Log.d(TAG, point.toString())
            val objectAnimatorX = ObjectAnimator.ofFloat(imageViews2[i], "translationX", 0f, point.x)
            val objectAnimatorY = ObjectAnimator.ofFloat(imageViews2[i], "translationY", 0f, point.y)
            val animatorSet = AnimatorSet()
            animatorSet.duration = 500
            animatorSet.play(objectAnimatorX).with(objectAnimatorY)
            animatorSet.start()
        }
    }

    /**
     * 关闭圆形菜单
     */
    private fun closeCircleMenu() {
        for (i in imageViews2.indices) {
            val point = PointF()
            val avgAngle = 360 / (imageViews2.size - 1)
            val angle = avgAngle * i
            Log.d(TAG, "angle=$angle")
            point.x = cos(angle * (Math.PI / 180)).toFloat() * radius2
            point.y = sin(angle * (Math.PI / 180)).toFloat() * radius2
            Log.d(TAG, point.toString())
            val objectAnimatorX = ObjectAnimator.ofFloat(imageViews2[i], "translationX", point.x, 0f)
            val objectAnimatorY = ObjectAnimator.ofFloat(imageViews2[i], "translationY", point.y, 0f)
            val animatorSet = AnimatorSet()
            animatorSet.duration = 500
            animatorSet.play(objectAnimatorX).with(objectAnimatorY)
            animatorSet.start()
        }
    }
}