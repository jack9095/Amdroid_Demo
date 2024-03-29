package com.kuanquan.datastore.proto

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.protobuf.InvalidProtocolBufferException
import com.kuanquan.datastore.*
import kotlinx.android.synthetic.main.activity_datastore_preference.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

const val TAG = "ProtoDataStoreActivity"

class ProtoDataStoreActivity : AppCompatActivity() {

    private val sp by lazy {
        getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private val spOther by lazy {
        getSharedPreferences(SHARED_OTHER_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }
    private lateinit var dataStore: DataStore<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStore = createDataStore(fileName = "user_prefs.pb", serializer = UserSerializer)
//        dataStore = createDataStore(
//            fileName = "user_prefs.pb",
//            serializer = UserSerializer,
//            migrations = listOf(shardPrefsMigration)
//        )


        setContentView(R.layout.activity_datastore_preference)
        observePreferences()
        initViews()
    }

    private fun initViews() {
        saveBtn.setOnClickListener {
            lifecycleScope.launch {
                saveData(UserConfig("Tom", 28))
            }
        }

        readBtn.setOnClickListener {
            readData().asLiveData().observe(this@ProtoDataStoreActivity, Observer {
                Log.e(TAG, "asLiveData -> $it")
            })
        }
    }

    /**
     * 数据监听
     */
    private fun observePreferences() {
        dataStore.data.asLiveData().observe(this, Observer {
            Log.e(TAG, it.name)
        })
    }

    /**
     * ProtoDataStore 读取数据
     */
    private fun readData(): Flow<User> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(User.getDefaultInstance())
            } else {
                throw it
            }
        }


    /**
     * ProtoDataStore 保存数据
     */
    private suspend fun saveData(data: UserConfig) {
        dataStore.updateData { user ->
            user.toBuilder().setAge(data.age).setName(data.name).build()
        }
    }


    /**
     * 当 DataStore 对象构建完了之后，需要执行一次读取或者写入操作，
     * 即可完成 SharedPreferences 迁移到 DataStore，当迁移成功之后，会自动删除 SharedPreferences 使用的文件
     */
    private val shardPrefsMigration =
        SharedPreferencesMigration<User>(
            this,
            SHARED_PREFERENCE_NAME
        ) { sharedPreferencesView, user ->

            // 获取 SharedPreferences 的数据
            val name = sharedPreferencesView.getString(SP_KEY_NAME, "")

            if (!TextUtils.equals(name, user.name)) {
                // 将 SharedPreferences 每一对 key-value 的数据映射到 Proto DataStore 中
                user.toBuilder()
                    .setName(name)
                    .build()
            } else {
                user
            }

        }

}

data class UserConfig(
    val name: String?,
    val age: Int
)

// 序列化 读写
object UserSerializer : Serializer<User> {
    override fun readFrom(input: InputStream): User {
        try {
            return User.parseFrom(input) // 是编译器自动生成的，用于读取并解析 input 的消息
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output) // t.writeTo(output) 是编译器自动生成的，用于写入序列化消息
    }

    override val defaultValue: User
        get() = User.getDefaultInstance()

}
