package com.kuanquan.koin.app

import android.app.Application
import com.kuanquan.koin.MainModule
import com.kuanquan.koin.MainModule.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WorkApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WorkApplication)
            androidFileProperties()  // 从assets中读取保存的数据
            modules(mainModule) // 需要初始化各个模块的module
//            modules(listOf(MainModule.mainModule)) // 需要初始化多个模块的module
        }
    }

}