package com.kuanquan.datastore.sharedpreferences

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.kuanquan.datastore.*
import kotlinx.android.synthetic.main.activity_sp.*

const val TAG = "SharedPreferencesActivity"
class SharedPreferencesActivity : AppCompatActivity() {

    val sp by lazy {
        getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    val spOther by lazy {
        getSharedPreferences(SHARED_OTHER_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp)

        saveBtn.setOnClickListener {
            spOther.edit {
                putString(SP_KEY_TITLE,"大数据")
            }
            sp.edit {
                putString(SP_KEY_NAME,"安卓")
//                putInt("name",0)
            }
        }

        obtainBtn.setOnClickListener {
            sp.getString(SP_KEY_NAME,"")

            val keys = sp.all.keys
            Log.e(TAG, "key长度${keys.size}")
            val values = sp.all.values
            Log.e(TAG, "value 长度${values.size}")
        }
    }
}