package com.kuanquan.dynamicappicon

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 切换图标一
    fun changeIcon(view: View) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "$packageName.MainActivity"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            0
        )
    }

    // 切换图标二
    fun changeIconAlias(view: View) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "$packageName.alias"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            0
        )
    }
}