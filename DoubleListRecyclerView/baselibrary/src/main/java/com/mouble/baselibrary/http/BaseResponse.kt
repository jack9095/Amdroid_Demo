package com.mouble.baselibrary.http

import java.io.Serializable

/**
 * 返回结果封装
 */
open class BaseResponse: Serializable {
//    var code: Int = 0 // 返回的code   0 成功
//    var results: T? = null // 具体的数据结果
    var msg: String? = null // message 可用来返回接口的说明
    var error: Boolean = false

    override fun toString(): String {
        return "BaseResponse(msg=$msg, error=$error)"
    }

}