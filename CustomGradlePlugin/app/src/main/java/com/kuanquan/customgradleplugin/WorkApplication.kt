package com.kuanquan.customgradleplugin

import android.app.Activity
import android.app.Application
import android.os.Bundle

class WorkApplication : Application() {

    companion object{
        private var instance: Application? = null
        fun getInstance() = instance
    }

    // 栈顶的 Activity
    private var stackTopActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 注册对 APP 内所有 activity 的监听
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            stackTopActivity = activity
        }

        override fun onActivityResumed(activity: Activity) {
            // 在 Activity 可视的时候设置栈顶 Activity
            stackTopActivity = activity
        }

    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks?) {
        super.unregisterActivityLifecycleCallbacks(callback)
    }

    private fun getStackTopActivity(): Activity? {
        return stackTopActivity
    }
}