package com.kuanquan.toastutil

import android.app.Application
import android.content.Context
import com.kuanquan.toastlibrary.ToastUtils

class WorkApplication: Application() {

    companion object {
        var mApplication: WorkApplication? = null
//        var mContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
//        mContext = this.applicationContext
        mApplication = this
        // 初始化 Toast 框架
        ToastUtils.init(this)
    }

}