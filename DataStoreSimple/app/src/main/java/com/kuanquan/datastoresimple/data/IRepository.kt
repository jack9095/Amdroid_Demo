package com.kuanquan.datastoresimple.data


interface IRepository {

    fun saveData(key: String)
    fun readData(key: String): Boolean
}