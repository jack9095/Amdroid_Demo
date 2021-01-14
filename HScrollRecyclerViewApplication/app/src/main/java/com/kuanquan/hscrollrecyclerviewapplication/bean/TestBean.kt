package com.kuanquan.hscrollrecyclerviewapplication.bean

import java.io.Serializable

data class TestBean(
    val id: String? = "0",
    val title: String? = "测试",
    val desc: String? = "前行",
    var type: Int = TYPE_NORMAL
) : Serializable {

    companion object {
        const val TYPE_NORMAL = 1
        const val TYPE_SCROLL = 2
    }

}