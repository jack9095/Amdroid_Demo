package com.kuanquan.customgradleplugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.joor.Reflect.onClass

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getReflect()
    }

    /**
     * 反射
     */
    private fun getReflect() {
        val world: String = onClass("java.lang.String") // 类似于 Class.forName()
            .create("Hello World") // 掉用类中的构造方法
            .call("substring", 6) // 掉用类中的方法
            .call("toString") // 再次掉用类中的方法
            .get()  // 获取包装好的对象
    }

    /**
     * 支持动态代理
     */
    private fun getProxy() {
        val substring: String? = onClass("java.lang.String") // 类似于 Class.forName()
            .create("Hello World") // 掉用类中的构造方法
            .`as`(StringProxy::class.java) // 创建一个代理类实例
            .substring(3)  // 掉用代理类中的方法
    }

    interface StringProxy {
        fun substring(beginIndex: Int): String?
    }
}