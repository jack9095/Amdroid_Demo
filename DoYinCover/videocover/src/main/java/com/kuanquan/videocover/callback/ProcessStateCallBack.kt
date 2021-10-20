package com.kuanquan.videocover.callback

import com.kuanquan.videocover.InstagramMediaProcessActivity

interface ProcessStateCallBack {

    fun onBack(activity: InstagramMediaProcessActivity?)
    fun onProcess(activity: InstagramMediaProcessActivity?)

}