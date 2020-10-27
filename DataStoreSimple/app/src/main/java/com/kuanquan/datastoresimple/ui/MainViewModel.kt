package com.kuanquan.datastoresimple.ui

import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kuanquan.datastoresimple.data.IDataStoreRepository
import com.kuanquan.datastoresimple.data.IRepository
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private var spRepo: IRepository? = null
    private var dataStoreRepo: IDataStoreRepository? = null

    fun setData(spRepo: IRepository, dataStoreRepo: IDataStoreRepository) {
        this.spRepo = spRepo
        this.dataStoreRepo = dataStoreRepo
    }

    // 使用 SharedPreferences
    fun saveData(key: String) = spRepo?.saveData(key)
    fun getData(key: String) = spRepo?.readData(key)

    // 使用 DataStore
    fun saveDataByDataStore(key: Preferences.Key<Boolean>) {
        viewModelScope.launch {
            dataStoreRepo?.saveData(key)
        }
    }

    fun getDataStore(key: Preferences.Key<Boolean>) = dataStoreRepo?.readData(key)?.asLiveData()

    // 合并 SharedPreferences to DataStore
    fun migrationSP2DataStore() = dataStoreRepo?.migrationSP2DataStore()
    fun testMigration(key: Preferences.Key<Boolean>) = dataStoreRepo?.readData(key)
}

object PreferencesKeys {
    // SharedPreferences 的测试的 key
    val KEY_ACCOUNT = "ByteCode"

    // DataStore 的测试的 key
    val KEY_BYTE_CODE = preferencesKey<Boolean>("ByteCode")
    val KEY_WEI_BO = preferencesKey<Boolean>("weibo")
    val KEY_GITHUB = preferencesKey<Boolean>("GitHub")
}