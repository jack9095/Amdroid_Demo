package com.kuanquan.virtualapkhost

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.didi.virtualapk.PluginManager
import java.io.File

class MainActivity : AppCompatActivity() {

    private val PLUGIN_PACKAGE_NAME = "com.wangyz.virtualapk.plugin"
    private val PLUGIN_NAME = "com.wangyz.virtualapk.plugin.MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPlugin()
    }


    private fun loadPlugin() {
        try {
            val pluginPath = Environment.getExternalStorageDirectory()
                .absolutePath + "/Plugin.apk"
            val plugin = File(pluginPath)
            PluginManager.getInstance(this).loadPlugin(plugin)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadPlugin(view: View?) {
        if (PluginManager.getInstance(this)
                .getLoadedPlugin(PLUGIN_PACKAGE_NAME) == null
        ) {
            Toast.makeText(applicationContext, "未加载插件", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        intent.component = ComponentName(
            PLUGIN_PACKAGE_NAME,
            PLUGIN_NAME
        )
        startActivity(intent)
    }
}