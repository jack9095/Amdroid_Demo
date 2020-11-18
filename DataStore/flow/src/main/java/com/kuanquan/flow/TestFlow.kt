package com.kuanquan.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.math.sign

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
//            TestFlow().testFlow()
//            TestFlow().asFlowTEst()
//            TestFlow().mapTest()
//            TestFlow().transformTest()
//            TestFlow().takeTest()
//            TestFlow().collect()
//            TestFlow().reduce()
//            TestFlow().toList()
//            TestFlow().toSet()
//            TestFlow().first()
//            TestFlow().single()
//            TestFlow().filter()
//            TestFlow().flowOn()
//            TestFlow().buffe()
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
    private suspend fun requestData(requestParameter: Int): String{
        delay(1000) // 模仿长时间运行的移步任务
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
        (1..3).asFlow()
            .transform { request ->
                emit("Making request -> $request")
                emit(requestData(request))
            }
            .collect{ response ->
                println(response)
            }
    }


    fun numbers() = flow{
        try {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
            println("this line will not execute")
            emit(5)
        } finally {
            println("finally in number")
        }
    }

    // 限长操作符
    fun takeTest() = runBlocking {
        numbers()
            .take(4) // 只获取前四个
            .collect { data ->
                println(data)
            }
    }

    /************************************** 末端流操作符 start *****************************************/
    // collect
    fun collect() = runBlocking {
        (1..5).asFlow()
            .map {
                it * it // 数字 1 至 5 的平方
            }
            .collect{ a ->
                println(a)
            }
    }

    // 求和  runBlocking 阻塞协成
    fun reduce() = runBlocking {
        val sum = (1..5).asFlow()
            .map {
                it * it // 数字 1 至 5 的平方
            }
            .reduce{ a, b ->
                a + b // 求和
            }
        print(sum)
    }

    // 转化为各种集合 例如 toList 与 toSet
    fun toList() = runBlocking {
       val lists: List<Int> =  (1..5).asFlow()
            .map {
                it + it
            }
            .toList()
        for(i in lists){
            println(i)
        }
    }

    fun toSet() = runBlocking {
        val sets: Set<Int> =  (1..5).asFlow()
            .map {
                it + it
            }
            .toSet()
        for(i in sets){
            println(i)
        }
    }

    fun first() = runBlocking {
        val first =  (1..5).asFlow()
            .map {
                it + it
            }
            .first {
                it*5
                true
            }
        println(first)
    }

    fun single() = runBlocking {
//        val single: Int =  (1..5).asFlow()
//            .map {
//                it + it
//            }
//            .single{
//
//            }
//        println(single)
    }

//    fun fold() = runBlocking {
//        (1..5).asFlow()
//            .map {
//                it + it
//            }
//            .fold()
//    }

    /************************************** 末端流操作符 end *****************************************/

    fun filter() = runBlocking {
        (1..5).asFlow()
            .filter {
                it % 2 == 0
            }
            .map {
                println("map -> $it")
                "string $it"
            }
            .collect { value ->
                println("collect -> $value")
            }
    }


    // 在流中切换线程或者说 更改上下文
    fun flow() = flow<Int> {
        for (x in 1..3){
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            println("${Thread.currentThread().name} $x")
            emit(x)
        }
    }.flowOn(Dispatchers.Default)


    fun  flowOn() = runBlocking {
        flow().collect{
            println("${Thread.currentThread().name} $it")
        }
    }

    // 使用缓存
    fun buffe() = runBlocking {
        flow()
            .buffer() // 缓冲发射项
            .collect {
                println("${Thread.currentThread().name} $it")
            }
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