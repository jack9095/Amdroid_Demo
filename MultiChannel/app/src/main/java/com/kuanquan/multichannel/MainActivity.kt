package com.kuanquan.multichannel

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val channel = getMetaData(applicationContext, "APP_CHANNEL")
        tv_channel_value.text = channel
        Toast.makeText(applicationContext, BuildConfig.FIELD_TEST, Toast.LENGTH_LONG).show()
    }

    private fun getMetaData(context: Context, key: String): String? {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context
                    .packageName, PackageManager.GET_META_DATA)
            return applicationInfo.metaData.getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}