package com.kuanquan.toastlibrary

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Build
import android.os.Bundle

/**
 * WindowManager 生命周期管控
 */
internal class WindowLifecycle(
    /** 当前 Activity 对象  */
    var activity: Activity?
) :
    ActivityLifecycleCallbacks {
    /**
     * 获取 Activity
     */

    /** 自定义 Toast 实现类  */
    private var mToastImpl: ToastImpl? = null

    /**
     * [Application.ActivityLifecycleCallbacks]
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}

    // A 跳转 B 页面的生命周期方法执行顺序：
    // onPause(A) ---> onCreate(B) ---> onStart(B) ---> onResume(B) ---> onStop(A) ---> onDestroyed(A)
    override fun onActivityPaused(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        if (mToastImpl == null) {
            return
        }

        // 不能放在 onStop 或者 onDestroyed 方法中，因为此时新的 Activity 已经创建完成，必须在这个新的 Activity 未创建之前关闭这个 WindowManager
        // 调用取消显示会直接导致新的 Activity 的 onCreate 调用显示吐司可能显示不出来的问题，又或者有时候会立马显示然后立马消失的那种效果
        mToastImpl!!.cancel()
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (this.activity !== activity) {
            return
        }
        if (mToastImpl != null) {
            mToastImpl!!.cancel()
        }
        unregister()
        this.activity = null
    }

    /**
     * 注册
     */
    fun register(impl: ToastImpl?) {
        mToastImpl = impl
        if (activity == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity!!.registerActivityLifecycleCallbacks(this)
        } else {
            activity!!.application.registerActivityLifecycleCallbacks(this)
        }
    }

    /**
     * 反注册
     */
    fun unregister() {
        mToastImpl = null
        if (activity == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity!!.unregisterActivityLifecycleCallbacks(this)
        } else {
            activity!!.application.unregisterActivityLifecycleCallbacks(this)
        }
    }
}