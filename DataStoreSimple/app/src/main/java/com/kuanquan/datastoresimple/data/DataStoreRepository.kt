package com.kuanquan.datastoresimple.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import com.kuanquan.datastoresimple.data.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


/**
 * DataStore 文件存放目录: /data/data/<包名>/files/datastore
 */
class DataStoreRepository(val context: Context) : IDataStoreRepository {

    private val PREFERENCE_NAME = "DataStore"
    // 创建一个名称为 "DataStore" 的 DataStore
    var dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )


    /**
     * DataStore 合并 SharedPreference 文件内容
     */
    override fun migrationSP2DataStore() {
        /**
         *  传入 migrations 参数，构建一个 DataStore 之后，
         *  需要执行 一次读取 或者 写入，DataStore 才会自动合并 SharedPreference 文件内容
         */
        dataStore = context.createDataStore(
            name = PREFERENCE_NAME,
            migrations = listOf(
                SharedPreferencesMigration(
                    context,
                    SharedPreferencesRepository.PREFERENCE_NAME
                )
            )
        )
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>) {
        dataStore.edit { mutablePreferences ->
            val value = mutablePreferences[key] ?: false
            mutablePreferences[key] = !value
        }
    }

    override fun readData(key: Preferences.Key<Boolean>): Flow<Boolean> =
        dataStore.data
            .catch {
                // 当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences，来重新使用
                // 但是如果是其他的异常，最好将它抛出去，不要隐藏问题
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map { preferences ->
                preferences[key] ?: false
            }
}