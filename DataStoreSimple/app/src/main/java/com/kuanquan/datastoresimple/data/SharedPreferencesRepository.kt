package com.kuanquan.datastoresimple.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * SharedPreferences 文件存放目录: /data/data/<包名>/shared_prefs
 * SharedPreferences 工具类
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SharedPreferencesRepository(context: Context) : IRepository {

    val sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    override fun saveData(key: String) {
        val value = !readData(key)
        sp.edit().putBoolean(key, value).apply()
//        sp.edit {
//            val value = !readData(key)
//            putBoolean(key, value)
//        }
    }

    override fun readData(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    companion object {
        val PREFERENCE_NAME = "Preferences"
    }
}