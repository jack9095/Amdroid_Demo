package com.mouble.baselibrary.http

import com.mouble.baselibrary.util.LogUtil
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {

    private val TAG = this.javaClass.simpleName

    private val mMessage = StringBuilder()

    override fun log(message: String) {
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0)
        }
        if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith("]")) {
//                        LogUtil.json(message)
        }
        mMessage.append(message + "\n")
        if (message.startsWith("<-- END HTTP")) {
            LogUtil.e(TAG, mMessage.toString())
        }
    }
}