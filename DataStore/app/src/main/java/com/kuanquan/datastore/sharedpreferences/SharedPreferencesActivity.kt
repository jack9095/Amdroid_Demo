package com.kuanquan.datastore.sharedpreferences

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.kuanquan.datastore.R
import com.kuanquan.datastore.SHARED_PREFERENCE_NAME
import com.kuanquan.datastore.SP_KEY_NAME
import kotlinx.android.synthetic.main.activity_sp.*

class SharedPreferencesActivity : AppCompatActivity() {

    val sp by lazy {
        getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp)

        saveBtn.setOnClickListener {
            sp.edit {
                putString(SP_KEY_NAME,"test")
//                putInt("name",0)
            }
        }

        obtainBtn.setOnClickListener {
            sp.getString(SP_KEY_NAME,"")
        }
    }
}