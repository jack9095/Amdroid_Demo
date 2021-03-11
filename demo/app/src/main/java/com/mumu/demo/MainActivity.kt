package com.mumu.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.mumu.demo.CustomDialogFragment.*

/**
 * LifecycleOwner 具有生命周期的类
 */
class MainActivity : AppCompatActivity() {

    private var lifecycleRegister: LifecycleRegistry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button).setOnClickListener {
            newInstance().show(supportFragmentManager)
        }

        // 创建生命周期注册对象
        lifecycleRegister = LifecycleRegistry(this)
        // 添加标记
        lifecycleRegister?.markState(Lifecycle.State.CREATED)
    }
}