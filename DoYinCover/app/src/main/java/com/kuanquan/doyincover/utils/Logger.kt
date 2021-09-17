package com.kuanquan.doyincover.utils

import android.util.Log

val defaultTag = "SSKLogger"
fun e(tag:String,msg:String){
//    if (BuildConfig.DEBUG)
    Log.e(tag,msg)
}

fun e(msg:String){
//    if (BuildConfig.DEBUG)
    Log.e(defaultTag,msg)
}
