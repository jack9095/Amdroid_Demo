package com.kuanquan.videocover.callback

import android.content.Intent

interface ProcessStateCallBack {

    fun onBack(activity: InstagramMediaProcessActivity?)
    fun onProcess(activity: InstagramMediaProcessActivity?)
    fun onActivityResult(
        activity: InstagramMediaProcessActivity?,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )

}