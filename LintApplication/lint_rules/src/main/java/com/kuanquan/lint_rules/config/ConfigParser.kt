package com.kuanquan.lint_rules.config

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.kuanquan.lint_rules.config.bean.AvoidUsageApi
import com.kuanquan.lint_rules.config.bean.DependencyApi
import com.kuanquan.lint_rules.config.bean.HandleExceptionMethod
import com.kuanquan.lint_rules.config.bean.ResourceName
import java.io.File

/**
 * lint配置解析器
 */
class ConfigParser(configFile: File) {

    private var configJson = JsonObject()

    companion object {
        const val KEY_AVOID_USAGE_API = "avoid_usage_api"
        const val KEY_HANDLE_EXCEPTION_METHOD = "handle_exception_method"
        const val KEY_DEPENDENCY_API = "dependency_api"
        const val KEY_RESOURCE_NAME = "resource_name"
        const val KEY_SERIALIZABLE_CONFIG = "serializable_config"
    }

    init {
        if (configFile.exists() && configFile.isFile) {
            configJson = Gson().fromJson(configFile.bufferedReader(), JsonObject::class.java)
        }
    }

    /**
     * 避免使用的api包含 方法、构造方法、字段等
     */
    fun getAvoidUsageApi(): AvoidUsageApi {
        return Gson().fromJson(
            configJson.getAsJsonObject(KEY_AVOID_USAGE_API),
            AvoidUsageApi::class.java
        ) ?: AvoidUsageApi()
    }

    /**
     * 调用指定API时，需要加try-catch处理指定类型的异常
     */
    fun getHandleExceptionMethod(): List<HandleExceptionMethod> {
        return Gson().fromJson(
            configJson.getAsJsonArray(KEY_HANDLE_EXCEPTION_METHOD),
            object : TypeToken<List<HandleExceptionMethod>>() {}.type
        ) ?: listOf()
    }

    /**
     * 有依赖关系的api
     */
    fun getDependencyApiList(): List<DependencyApi> {
        return Gson().fromJson(
            configJson.getAsJsonArray(KEY_DEPENDENCY_API),
            object : TypeToken<List<DependencyApi>>() {}.type
        ) ?: listOf()
    }

    /**
     * 获取资源命名
     */
    fun getResourceName(): ResourceName {
        return Gson().fromJson(
            configJson.getAsJsonObject(KEY_RESOURCE_NAME),
            object : TypeToken<ResourceName>() {}.type
        ) ?: ResourceName()
    }
    /**
     * 获取SerializableConfig
     */
    fun getSerializableConfig(): BaseConfigProperty {
        return Gson().fromJson(
            configJson.getAsJsonObject(KEY_SERIALIZABLE_CONFIG),
            object : TypeToken<BaseConfigProperty>() {}.type
        ) ?: BaseConfigProperty()
    }
}