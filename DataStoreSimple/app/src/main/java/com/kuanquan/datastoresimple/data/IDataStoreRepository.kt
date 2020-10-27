package com.kuanquan.datastoresimple.data

import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.flow.Flow


interface IDataStoreRepository  {
    suspend fun saveData(key: Preferences.Key<Boolean>)

    fun readData(key: Preferences.Key<Boolean>): Flow<Boolean>

    fun migrationSP2DataStore()
}