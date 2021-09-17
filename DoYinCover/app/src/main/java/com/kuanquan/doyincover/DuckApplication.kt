package com.kuanquan.doyincover

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class DuckApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}