package com.kuanquan.koin

import android.util.Log
import android.widget.TextView
import com.kuanquan.koin.app.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.currentScope

class MainActivity : BaseActivity<MainPresenter>() {

    private val mainModel : MainModule by inject<MainModule>()

    override fun initLayout(): Int {
        return R.layout.activity_main
    }

    override fun initInject() {
        mPresenter = currentScope.get() // get()
    }

    override fun initViewAndData() {
        findViewById<TextView>(R.id.textView).setOnClickListener {
//            Log.e("MainActivity", mainModel)
            mPresenter?.getData("调用presenter")
        }
    }

}