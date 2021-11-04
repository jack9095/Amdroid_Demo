package com.kuanquan.toastutil

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuanquan.toastlibrary.ToastUtils
import com.kuanquan.toastlibrary.style.BlackToastStyle
import com.kuanquan.toastlibrary.style.WhiteToastStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun show1(view: View) {
        ToastUtils.show("我是普通的 Toast")
    }
    fun show2(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            ToastUtils.show("我是子线程中弹出的吐司")
        }
    }
    fun show3(view: View) {
        ToastUtils.style = WhiteToastStyle()
        ToastUtils.show("动态切换白色吐司样式成功")
    }
    fun show4(view: View) {
        ToastUtils.style = BlackToastStyle()
        ToastUtils.show("动态切换黑色吐司样式成功")
    }
    fun show5(view: View) {
        ToastUtils.setView(R.layout.toast_custom_view)
        ToastUtils.setGravity(Gravity.CENTER, 0, 0, 0.05f, 0.3f)
        ToastUtils.show("自定义 Toast 布局")
    }
    fun show6(view: View) {

    }
}