package com.kuanquan.datastore.proto

import android.content.Context
import android.util.Log
import androidx.datastore.CorruptionException
import androidx.datastore.DataStore
import androidx.datastore.Serializer
import androidx.datastore.createDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

data class UserConfig(
    val name: String?,
    val age: Int
)

class ProtoPreferenceManager(context: Context) {
    private val dataStore: DataStore<User> =
        context.createDataStore(
            fileName = "user_prefs.pb",
            serializer = UserSerializer
        )

    val userPreference = dataStore.data.catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading sort order preferences.", it)
            emit(User.getDefaultInstance())
        } else {
            throw it
        }
    }.map {
        UserConfig("Jack", 20)
    }

    suspend fun saveData(data: UserConfig?) {
        dataStore.updateData { preferences ->
            preferences.toBuilder()
                .setName(data?.name)
                .setAge(data?.age ?: 0)
                .build()
        }
    }

    companion object {
        const val TAG = "ProtoPreferenceManager"
    }
}

object UserSerializer: Serializer<User> {
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

}