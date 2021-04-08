package com.kuanquan.koin.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T : BasePresenter> : AppCompatActivity() {

    var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        initInject()
        initViewAndData()
    }

    protected abstract fun initLayout(): Int

    protected abstract fun initInject()

    open fun initViewAndData() {}
}
