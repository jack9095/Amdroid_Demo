package com.kuanquan.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.MainScope

import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import com.kuanquan.datastore.databinding.ActivityDataStoreBinding
import com.kuanquan.datastore.databinding.ActivityMainBinding
import com.kuanquan.datastore.model.AGE_KEY
import com.kuanquan.datastore.model.NAME_KEY
import com.kuanquan.datastore.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


/**
 * Preferences DataStore
 */
class DataStoreActivity : AppCompatActivity() {

    private val scope = MainScope() // Coroutines Scope for executing suspend function 用于执行挂起函数的协程作用域

    private lateinit var binding: ActivityDataStoreBinding

    private lateinit var dataStore: DataStore<Preferences>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataStoreBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        // with Preferences DataStore 创建 DataStore
        dataStore = createDataStore(
            name = "userPref"
        )

         // 保存数据
        binding.writeBtn.setOnClickListener {

            if (binding.nameTextField.editText?.text.toString()
                    .isNotEmpty() && binding.nameTextField.editText?.text.toString().isNotEmpty()
            ) {

                scope.launch {
                    saveUserDetails(
                        UserData(
                            binding.nameTextField.editText?.text.toString(),
                            binding.ageTextField.editText?.text.toString().toInt()
                        )
                    )
                }

            }
        }

        binding.readBtn.setOnClickListener {
            scope.launch {
                readUserData().collect { userData ->
                    binding.resultTv.text =
                        "Hello! My name is ${userData.name}\nI am ${userData.age} years old."
                }
            }
        }

    }


    /**
     * DataStore 读取数据
     */
    private fun readUserData(): Flow<UserData> = dataStore.data
        .map { currentPreferences ->
            // 通过 key 获取 value
            val name = currentPreferences[NAME_KEY] ?: ""
            val age = currentPreferences[AGE_KEY] ?: 0
            UserData(name, age)
        }


    /**
     * DataStore 保存数据
     */
    private suspend fun saveUserDetails(userData: UserData) {
        dataStore.edit { userDetails ->
            userDetails[NAME_KEY] = userData.name // Key = Value
            userDetails[AGE_KEY] = userData.age
        }
    }
}