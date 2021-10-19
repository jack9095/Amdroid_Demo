package com.kuanquan.videocover.callback

import com.kuanquan.videocover.InstagramMediaProcessActivity

interface LifecycleCallBack {
    fun onStart(activity: InstagramMediaProcessActivity?)
    fun onResume(activity: InstagramMediaProcessActivity?)
    fun onPause(activity: InstagramMediaProcessActivity?)
    fun onDestroy(activity: InstagramMediaProcessActivity?)
}