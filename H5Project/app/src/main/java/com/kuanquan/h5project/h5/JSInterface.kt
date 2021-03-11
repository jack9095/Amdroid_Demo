package com.kuanquan.h5project.h5

import android.webkit.JavascriptInterface

class JSInterface(onClickH5Listener: OnClickH5Listener) {

    @JavascriptInterface
    fun voiceAnnouncements(type: String) {
        mOnClickH5Listener.voiceAnnouncements(type)
    }

    @JavascriptInterface
    fun printingData(json: String) {
        mOnClickH5Listener.printingData(json)
    }

    private var mOnClickH5Listener: OnClickH5Listener = onClickH5Listener

    interface OnClickH5Listener {
        fun voiceAnnouncements(type: String)
        fun printingData(json: String)
    }
}