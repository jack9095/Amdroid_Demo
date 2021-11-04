package com.kuanquan.toastutil

import android.app.Application
import com.kuanquan.toastlibrary.ToastUtils

class WorkApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化 Toast 框架
        ToastUtils.init(this)
    }

}