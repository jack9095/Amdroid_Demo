package com.kuanquan.datastore

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.datastore.preference.PreferenceDatastoreActivity
import com.kuanquan.datastore.proto.ProtoDatastoreActivity
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        buttonPreference.setOnClickListener {
            startActivity(Intent(this@MainActivity, PreferenceDatastoreActivity::class.java))
        }

        buttonProto.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProtoDatastoreActivity::class.java))
        }
    }
}