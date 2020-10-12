package com.mouble.baselibrary.base

import com.mouble.baselibrary.http.HttpHelper

open class BaseApiServiceViewModel<T>(clazz: Class<T>) : BaseViewModel() {

    var serviceApi: T? = null

    init {
        serviceApi = HttpHelper.getInstance()?.create(clazz)
    }
}