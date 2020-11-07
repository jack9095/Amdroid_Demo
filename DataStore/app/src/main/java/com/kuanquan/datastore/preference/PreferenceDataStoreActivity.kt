package com.kuanquan.datastore.preference

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.kuanquan.datastore.PREFERENCE_NAME
import com.kuanquan.datastore.R
import com.kuanquan.datastore.SHARED_PREFERENCE_NAME
import kotlinx.android.synthetic.main.activity_datastore_preference.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class PreferenceDataStoreActivity : AppCompatActivity() {

    val TAG = PreferenceDataStoreActivity::class.simpleName

    /*
        这里和我们之前使用 SharedPreferences 的有点不一样，
        在 Preferences DataStore 中 Key 是一个 Preferences.Key<T> 类型，
        只支持 Int , Long , Boolean , Float , String
     */
    val dsKey = preferencesKey<String>("test_name")
    val spKey = preferencesKey<String>("name")

    // 构建 DataStore
//    private val dataStore: DataStore<Preferences> = createDataStore(name = PREFERENCE_NAME)
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastore_preference)

        migrationSP2DataStore()
        observeUiPreferences()
        initViews()
    }

    private fun observeUiPreferences() {
        dataStore.data.asLiveData().observe(this, Observer { data ->
            Log.e(TAG, "$data")
        })
    }

    private fun initViews() {
        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                dataStore.edit { preferences ->
                    preferences[dsKey] = "tom"
                }
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
     * DataStore 读取数据
     */
    private fun readData(dsKey: Preferences.Key<String>): Flow<String> = dataStore.data
        .catch {
            // 当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
            // 但是如果是其他的异常，最好将它抛出去，不要隐藏问题
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { currentPreferences ->
            val data = currentPreferences[dsKey] ?: "测试数据"
            data
        }

    /**
     * 迁移 SharedPreferences 到 DataStore
     */
    private fun migrationSP2DataStore() {
        /**
         *  传入 migrations 参数，构建一个 DataStore 之后，
         *  需要执行 一次读取 或者 写入，DataStore 才会自动合并 SharedPreference 文件内容
         */
        dataStore = createDataStore(
            name = PREFERENCE_NAME,
            migrations = listOf(
                SharedPreferencesMigration(
                    this,
                    SHARED_PREFERENCE_NAME
                )
            )
        )
    }
}