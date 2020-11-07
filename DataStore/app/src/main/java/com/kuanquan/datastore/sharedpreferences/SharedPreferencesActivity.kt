package com.kuanquan.datastore.sharedpreferences

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.kuanquan.datastore.PREFERENCE_NAME
import com.kuanquan.datastore.R
import kotlinx.android.synthetic.main.activity_sp.*

class SharedPreferencesActivity : AppCompatActivity() {

    val sp by lazy {
        getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sp)

        saveBtn.setOnClickListener {
            sp.edit {
                putString("name","test")
//                putInt("name",0)
            }
        }

        obtainBtn.setOnClickListener {
            sp.getString("name","")
        }
    }
}