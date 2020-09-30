package com.mouble.baselibrary.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

object GsonUtils {
    private var gson: Gson? = null

    fun getInstance(): Gson? {
        if (gson == null) gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
        return gson
    }

    /**
     * 返回一个对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    @JvmStatic
    fun <T> toObject(json: String?, clazz: Class<*>?): T? {
        try {
            return getInstance()?.fromJson(json, clazz) as? T
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 返回一个对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    fun <T> toObject(json: String?, type: Type?): T? {
        try {
            return getInstance()?.fromJson<Any>(json, type) as? T
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将Map参数转成json格式传输
     *
     * @param map
     * @return
     */
    fun toJson(map: Map<*, *>?): String? {
        return getInstance()?.toJson(map)
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List></List><yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List></List><yourbean>>(){}.getType() List<LeaveSchedule> tmpBacklogList = gson.fromJson(backlogJsonStr, new TypeToken<List></List><LeaveSchedule>>() {}.getType());
     * @return ArrayList<ContentMsg> json1 = gson.fromJson(s.toString(),new TypeToken<List></List><ContentMsg>>() {}.getType());
     */
    fun toList(json: String?, type: Type?): List<*>? {
//        val gson = Gson()
        return getInstance()?.fromJson<List<*>>(json, type)
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @return
     */
    fun toJson(obj: Any?): String? {
        val gson = Gson()
        return if (obj == null) {
            ""
        } else {
            gson.toJson(obj)
        }
    }

    /**
     * json字符串转成对象
     *
     * @param str
     * @param type
     * @return
     */
    fun <T> fromJson(str: String?, type: Class<T>?): T {
        val gson = Gson()
        return gson.fromJson(str, type)
    }

    /**
     * json字符串转成对象或集合（数组）
     *
     * @param str
     * @param type java.lang.reflect.Type type = new TypeToken<HashMap></HashMap><Integer></Integer>, String>>() {}.getType();
     * @return
     */
    fun <T> fromJsonType(str: String?, type: Type?): T {
        val gson = Gson()
        return gson.fromJson(str, type)
    }
}