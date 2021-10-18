package com.kuanquan.videocover.callback

interface LifecycleCallBack {
    fun onStart(activity: InstagramMediaProcessActivity?)
    fun onResume(activity: InstagramMediaProcessActivity?)
    fun onPause(activity: InstagramMediaProcessActivity?)
    fun onDestroy(activity: InstagramMediaProcessActivity?)
}