package com.mumu.easyemoji.user

import java.io.Serializable

data class EmotionBean(val unicode: String? = "0",
                       val childList: MutableList<EmotionBean>? = null,
                       var type: Int = TYPE_COMMENT): Serializable {

    companion object {
        const val TYPE_HEAD = 1
        const val TYPE_COMMENT = 2
    }
}