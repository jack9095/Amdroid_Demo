package com.kuanquan.library.view.panel

import androidx.annotation.IdRes
import com.kuanquan.library.interfaces.ViewAssertion

/**
 * 扩展panelView
 * 同时需要实现 ViewAssertion 校验 trigger 绑定的有效性，实现 IPanelView 应该是一个 ViewGroup
 * created by yummylau on 2020/06/01 🧒儿童节
 */
interface IPanelView : ViewAssertion {

    @IdRes
    fun getBindingTriggerViewId(): Int

    fun isTriggerViewCanToggle(): Boolean

    fun isShowing():Boolean
}