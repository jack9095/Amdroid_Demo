package com.mouble.baselibrary.http

const val ERROR_STATE = "1"
const val LOADING_STATE = "2"
const val SUCCESS_STATE = "3"
const val NET_WORK_STATE = "4"
interface StateConstants {
    // 网络请求失败
    val ERROR_STATE: String
        get() = "1"
//        private set  // 允许外部读取但不写入

    // 正在请求中
    val LOADING_STATE: String
        get() = "2"

    // 请求成功
    val SUCCESS_STATE: String
        get() = "3"

    // 网络不好请稍后重试
    val NET_WORK_STATE: String
        get() = "4"
}