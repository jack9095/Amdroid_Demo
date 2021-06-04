package com.kuanquan.lintapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

/**
 * https://blog.csdn.net/u011272lint795/article/details/106844712
 * https://juejin.cn/post/6844903774104879111
 * https://juejin.cn/post/6861562664582119432
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_a_layout)
        Log.e("wf", "test")
        val s = "lint"
    }

    fun test(){

    }
}