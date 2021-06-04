package com.kuanquan.lintapplication

import java.io.Serializable

/**
 * 实现了 Serializable 接口的类，引用类型成员变量也必须要实现 Serializable 接口
 */
class SerializableBean : Serializable {
    var serializableField: InnerSerializableBean? = null
}

class InnerSerializableBean : Serializable {
    var commonBean: CommonBean? = null
}

class CommonBean{
    private var s: String = "abc"
}