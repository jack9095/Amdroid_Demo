package com.kuanquan.toastutil

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuanquan.toastlibrary.ToastUtils
import com.kuanquan.toastlibrary.style.BlackToastStyle
import com.kuanquan.toastlibrary.style.WhiteToastStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    var mImageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mImageView = findViewById<ImageView>(R.id.imageView)
    }

    fun show1(view: View) {
        mImageView?.setImageDrawable(R.drawable.shape_gradient.res2Drawable())
        ToastUtils.show(R.string.one_toast.res2String())
    }
    fun show2(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            ToastUtils.show(getString(R.string.two_toast))
        }
    }
    fun show3(view: View) {
        ToastUtils.apply {
            style = WhiteToastStyle()
            show("动态切换白色吐司样式", true)
        }
    }
    fun show4(view: View) {
        ToastUtils.apply {
            style = BlackToastStyle()
            show("动态切换黑色吐司样式")
        }
//        ToastUtils.style = BlackToastStyle()
//        ToastUtils.show("动态切换黑色吐司样式")
    }
    fun show5(view: View) {
        ToastUtils.apply {
            setView(R.layout.toast_custom_view)
            setGravity(Gravity.CENTER, 0, 0, 0.05f, 0.3f)
            show("自定义 Toast 布局", true)
        }
//        ToastUtils.setView(R.layout.toast_custom_view)
//        ToastUtils.setGravity(Gravity.CENTER, 0, 0, 0.05f, 0.3f)
//        ToastUtils.show("自定义 Toast 布局", true)
    }

}