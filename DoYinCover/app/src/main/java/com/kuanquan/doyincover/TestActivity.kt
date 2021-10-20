package com.kuanquan.doyincover

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.Serializable
import java.util.List
import java.util.ArrayList

/**
 * 判断两个集合是否相等
 *
 */
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

//        val a = A("Alex", 20)
//        val b = A("Alex", 30)
//        Log.e("fei.wang ->", "${a == b}")
//        Log.e("fei.wang -> ", "${a === b}")

        val list = ArrayList<A>()
        val list1 = ArrayList<A>()

        for (i in 0..10) {
            list.add(A("Alex", i))
            list1.add(A("Alex", i))
        }

        Log.e("wangfei -> ", "size -> ${list.size}")
        for (i in list.indices) {
            Log.e("wangfei -> ", "i -> $i")
            list.add(A("Alex", i))
            list1.add(A("Alex", i))
        }

//        Log.e("wangfei -> ", "${checkDiffrent(list, null)}")
    }
}

private fun checkDiffrent5(list: MutableList<A>?, list1: MutableList<A>?): Boolean {
    val st = System.nanoTime();
    Log.e("wangfei -> ", "消耗时间为： " + (System.nanoTime() - st));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        list?.sortWith(Comparator.comparing{ it.hashCode() })
//        list1?.sortWith(Comparator.comparing(A::hashCode))
        list1?.sortWith(Comparator.comparing{ it.hashCode() })
    }
    return list.toString() == list1.toString()
}

private fun checkDiffrent(list: MutableList<A>?, list1: MutableList<A>?): Boolean {
    val st = System.nanoTime();
    Log.e("wangfei -> ", "消耗时间为： " + (System.nanoTime() - st))
    if (list.isNullOrEmpty() || list1.isNullOrEmpty()) {
        return false
    }
    return !list.retainAll(list1)
}


data class A(var a: String = "test", var b: Int = 0) : Serializable

data class B(var a: String = "test", var b: Int = 0) : Serializable