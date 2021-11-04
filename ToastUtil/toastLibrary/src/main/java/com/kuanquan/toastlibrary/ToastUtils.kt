package com.kuanquan.toastlibrary

import android.app.Application
import android.content.res.Resources.NotFoundException
import com.kuanquan.toastlibrary.config.IToastStrategy
import com.kuanquan.toastlibrary.config.IToastStyle
import com.kuanquan.toastlibrary.style.BlackToastStyle
import com.kuanquan.toastlibrary.style.LocationToastStyle
import com.kuanquan.toastlibrary.style.ViewToastStyle

/**
 * Toast 框架（专治 Toast 疑难杂症）
 */
object ToastUtils {
    /** Application 对象  */
    private var sApplication: Application? = null

    /** Toast 处理策略  */
    private var sToastStrategy: IToastStrategy? = null

    /** Toast 样式  */
    private var sToastStyle: IToastStyle<*>? = null

    /** 调试模式  */
    private val sDebugMode: Boolean? = null
    /**
     * 初始化 Toast 及样式
     */
    /**
     * 初始化 Toast，需要在 Application.create 中初始化
     *
     * @param application       应用的上下文
     */
    @JvmOverloads
    fun init(application: Application?, toastStyle: IToastStyle<*>? = sToastStyle) {
        var style: IToastStyle<*>? = toastStyle
        sApplication = application

        // 初始化 Toast 显示处理器
        if (sToastStrategy == null) {
            strategy = ToastStrategy()
        }
        if (style == null) {
            style = BlackToastStyle()
        }

        // 设置 Toast 样式
        ToastUtils.style = style
    }

    /**
     * 判断当前框架是否已经初始化
     */
    val isInit: Boolean
        get() = sApplication != null && sToastStrategy != null && sToastStyle != null

    /**
     * 显示一个对象的吐司
     *
     * @param object      对象
     */
    fun show(`object`: Any?) {
        show(`object`?.toString() ?: "null")
    }

    /**
     * 显示一个吐司
     *
     * @param id      如果传入的是正确的 string id 就显示对应字符串
     * 如果不是则显示一个整数的string
     */
    fun show(id: Int) {
        try {
            // 如果这是一个资源 id
            show(sApplication!!.resources.getText(id))
        } catch (ignored: NotFoundException) {
            // 如果这是一个 int 整数
            show(id.toString())
        }
    }

    private var hasDefaultStyle = false // 是否需要重新设置默认样式 true 需要

    /**
     * 显示一个吐司 默认格式的toast
     *
     * @param text  需要显示的文本
     * @param isCustomStyle  是否是自定义样式布局 true 表示是自定义的样式
     */
    fun show(text: CharSequence?, isCustomStyle: Boolean = false) {

        if (isCustomStyle) {
            hasDefaultStyle = true
        } else {
            if (hasDefaultStyle) {
                hasDefaultStyle = false
                style = BlackToastStyle()
            }
        }

        // 如果是空对象或者空文本就不显示
        if (text == null || text.isEmpty()) {
            return
        }
        sToastStrategy?.showToast(text)
    }

    /**
     * 取消吐司的显示
     */
    fun cancel() {
        sToastStrategy?.cancelToast()
    }

    /**
     * 设置吐司的位置
     *
     * @param gravity           重心
     */
    fun setGravity(gravity: Int) {
        setGravity(gravity, 0, 0)
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        setGravity(gravity, xOffset, yOffset, 0f, 0f)
    }

    fun setGravity(
        gravity: Int,
        xOffset: Int,
        yOffset: Int,
        horizontalMargin: Float,
        verticalMargin: Float
    ) {
        sToastStrategy?.bindStyle(
            LocationToastStyle(
                sToastStyle,
                gravity,
                xOffset,
                yOffset,
                horizontalMargin,
                verticalMargin
            )
        )
    }

    /**
     * 给当前 Toast 设置新的布局
     */
    fun setView(id: Int) {
        if (id <= 0) {
            return
        }
        style = ViewToastStyle(id, sToastStyle)
    }

    /**
     * 初始化全局的 Toast 样式
     *
     * @param style         样式实现类，框架已经实现两种不同的样式
     * 黑色样式：[BlackToastStyle]
     * 白色样式：[WhiteToastStyle]
     */
    var style: IToastStyle<*>?
        get() = sToastStyle
        set(style) {
            sToastStyle = style
            sToastStrategy?.bindStyle(style)
        }

    /**
     * 设置 Toast 显示策略
     */
    var strategy: IToastStrategy?
        get() = sToastStrategy
        set(strategy) {
            sToastStrategy = strategy
            sToastStrategy?.registerStrategy(sApplication)
        }
}