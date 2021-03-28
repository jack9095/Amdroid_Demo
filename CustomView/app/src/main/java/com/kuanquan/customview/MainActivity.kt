package com.kuanquan.customview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.customview.databinding.ActivityMainBinding

/**
 * 如何让一个View按照曲线动起来？
 * 思路：贝塞儿曲线 + 平移属性动画
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.belowIv.setOnClickListener(object : View.OnClickListener, CustomListener{
            override fun onClick(v: View?) {

            }

            override fun demo() {

            }

        })
    }

    interface CustomListener {
        fun demo()
    }

    var mPath: AnimatorPath? = null

    fun onFabPressed(v: View){
//        this.view = v
        mPath = AnimatorPath()
        mPath?.moveTo(0f,0f)
        mPath!!.cubicTo(-200f, 400f, -400f, 200f, -600f, 50f) // 三阶贝塞尔曲线

//        mPath!!.quadTo(200f, 200f, 400f, 100f) // 二阶贝塞尔曲线

        mPath!!.lineTo(-900f, 200f) // 绘制直线
//        mPath!!.lineTo(0f, 0f) // 绘制直线

        mPath?.startAnimation(v, 2000)

    }
}
