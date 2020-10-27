package com.kuanquan.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *  名为 flow 的 Flow 类型构建器函数。
    flow { ... } 构建块中的代码可以挂起。
    函数 simple 不再标有 suspend 修饰符。
    流使用 emit 函数 发射 值。
    流使用 collect 函数 收集 值。

  * https://www.kotlincn.net/docs/reference/coroutines/flow.html
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun simple(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一个任务，耗时100毫秒
            emit(i) // 发送下一个值
        }
    }

    fun main() = runBlocking<Unit> {
        // 启动并发的协程以验证主线程并未阻塞
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }
        // 收集这个流
        simple().collect { value -> println(value) }
    }
}
