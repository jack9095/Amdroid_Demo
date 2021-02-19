package com.kuanquan.sinaweibopraise

import android.app.Application
import android.content.Context

class TestApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppLifecycleManager.init(this)
    }
}