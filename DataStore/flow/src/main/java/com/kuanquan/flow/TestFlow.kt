package com.kuanquan.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 *   flow 为 Flow 类型构建器函数。
 *   Flow 是一种类似于序列的冷流
flow { ... } 构建块中的代码可以挂起。
函数 simple 不再标有 suspend 修饰符。
流使用 emit 函数 发射 值。
流使用 collect 函数 收集 值。

 * https://www.kotlincn.net/docs/reference/coroutines/flow.html
 */
class TestFlow {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TestFlow().testFlow()
            TestFlow().asFlowTEst()
            TestFlow().mapTest()
        }
    }

    // 这段 flow 构建器中的代码直到流被收集的时候才运行
    fun simple(): Flow<Int> = flow {
        println("Flow started")
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i) // 发送下一个值
        }
    }

    fun testFlow() = runBlocking<Unit> {
        println("Calling simple function...")
        val flow = simple()  // 该流在每次收集的时候启动
        println("Calling collect...")
        flow.collect { value -> println(value) }
        println("Calling collect again...")
        flow.collect { value -> println(value) }
    }

    // 流采用与协程同样的协作取消，流的收集可以在当流在一个可取消的挂起函数（例如 delay）中挂起的时候取消
    fun cancel() = runBlocking {

        withTimeoutOrNull(300) {  // 在300毫秒后超时
            val flow = simple()
            flow.collect {
                println(it)
            }
        }

        println("完成")
    }

    // 使用 .asFlow() 扩展函数，可以将各种集合与序列转换为流
    fun asFlowTEst() = runBlocking {
        // 将一个整数区间转化为流
        (1..3).asFlow().collect {
            println(it)
        }
    }

    // 模拟一个网络请求
    private fun requestData(requestParameter: Int): String{
        return "response $requestParameter"
    }

    // flow 的 map 操作符
    fun mapTest() = runBlocking {
        (1..5).asFlow().map {
            requestData(it)
        }.collect{
            println(it)
        }
    }

    // flow 的 transform 转换操作符
    fun transformTest() = runBlocking {

    }

    fun test() = runBlocking<Unit> {
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


//fun main(args: Array<String>) {
//
//}