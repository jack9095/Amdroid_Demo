//package com.kuanquan.datastore
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.kuanquan.datastore.preference.PreferenceDataStoreActivity
//import com.kuanquan.datastore.proto.ProtoDataStoreActivity
//import com.kuanquan.datastore.sharedpreferences.SharedPreferencesActivity
//import kotlinx.android.synthetic.main.activity_main.*
//
//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        initViews()
//    }
//
//    private fun initViews() {
//        buttonPreference.setOnClickListener {
//            startActivity(Intent(this@MainActivity, PreferenceDataStoreActivity::class.java))
//        }
//
//        buttonProto.setOnClickListener {
//            startActivity(Intent(this@MainActivity, ProtoDataStoreActivity::class.java))
//        }
//        buttonSp.setOnClickListener {
//            startActivity(Intent(this@MainActivity, SharedPreferencesActivity::class.java))
//        }
//    }
//}