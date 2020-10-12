package com.mouble.baselibrary.util

class EventCenter<T> {
    private var type: String? = null
    private var obj: T? = null

    constructor(type: String?, obj: T) {
        this.type = type
        this.obj = obj
    }

    constructor(type: String?) {
        this.type = type
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun getObject(): T? {
        return obj
    }

    fun setObject(obj: T) {
        this.obj = obj
    }

    override fun toString(): String {
        return "EventCenter{" +
                "type='" + type + '\'' +
                ", obj=" + obj +
                '}'
    }
}