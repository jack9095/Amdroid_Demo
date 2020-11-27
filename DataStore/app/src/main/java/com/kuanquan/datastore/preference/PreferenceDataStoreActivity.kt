package com.kuanquan.datastore.preference

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.kuanquan.datastore.*
import com.kuanquan.datastore.R
import kotlinx.android.synthetic.main.activity_datastore_preference.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException


class PreferenceDataStoreActivity : AppCompatActivity() {

    private val TAG = PreferenceDataStoreActivity::class.simpleName

    private val dsKey = preferencesKey<String>("test_name")
    private val spKey = preferencesKey<String>(SP_KEY_NAME)

    // 构建 DataStore
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastore_preference)

//        dataStore = createDataStore(name = DATA_STORE_PREFERENCE_NAME)
        /**
         *  传入 migrations 参数，构建一个 DataStore 之后
         */
        dataStore = createDataStore(
            name = DATA_STORE_PREFERENCE_NAME,
            migrations = listOf(
                SharedPreferencesMigration(
                    this,
                    SHARED_PREFERENCE_NAME
                ),
                SharedPreferencesMigration(
                    this,
                    SHARED_OTHER_PREFERENCE_NAME
                )
            )
        )
        observeUiPreferences()
        initViews()
    }

    private fun initViews() {
        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                writeData()
            }
        }

        readBtn.setOnClickListener {
            readData(spKey).asLiveData().observe(this@PreferenceDataStoreActivity, Observer {
                Log.e(TAG, "asLiveData -> $it")
            })

            lifecycleScope.launch {
                readData(dsKey).collect {
                    Log.e(TAG, "collect -> $it")
                }
            }
        }
    }

    /**
     *  DataStore 监听数据
     */
    private fun observeUiPreferences() {
        dataStore.data.asLiveData().observe(this, Observer { data ->
            Log.e(TAG, "$data")
        })
    }

    /**
     * DataStore 写入数据
     */
    private suspend fun writeData() {
        dataStore.edit { preferences ->
            preferences[dsKey] = "tom"
        }
    }

    /**
     * DataStore 读取数据
     */
    private fun readData(dsKey: Preferences.Key<String>): Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
//        .flowOn(Dispatchers.IO)
        .map { currentPreferences ->
            currentPreferences[dsKey] ?: "测试数据"
        }
}