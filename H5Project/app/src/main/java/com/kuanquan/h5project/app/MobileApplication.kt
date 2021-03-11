package com.enjoy.whole.family.app

import android.app.Application
import android.content.Context
import com.kuanquan.h5project.util.LogUtil
import com.kuanquan.h5project.util.SharedPreferencesUtils

class MobileApplication : Application() {

    companion object{
        private var instance : Application? = null
        fun getInstance() = instance
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val fly = LogUtil.Builder(this)
            .isLog(true)
            .isLogBorder(true)
            .setLogType(LogUtil.TYPE.E)
            .setTag("MobileApplication")
        LogUtil.init(fly)
        SharedPreferencesUtils.getInstance("mb_fl", this.applicationContext)
    }
}