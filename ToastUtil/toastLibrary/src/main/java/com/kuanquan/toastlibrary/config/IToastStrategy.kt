package com.kuanquan.toastlibrary.config

import android.app.Application

/**
 * 处理策略
 */
interface IToastStrategy {

    /**
     * 注册策略
     */
    fun registerStrategy(application: Application?)

    /**
     * 绑定样式
     */
    fun bindStyle(style: IToastStyle<*>?)

    /**
     * 创建 Toast
     */
    fun createToast(application: Application?): IToast?

    /**
     * 显示 Toast
     */
    fun showToast(text: CharSequence?)

    /**
     * 取消 Toast
     */
    fun cancelToast()

}