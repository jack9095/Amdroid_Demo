package com.kuanquan.datastore.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.kuanquan.datastore.R
import com.kuanquan.datastore.preference.SettingsManager
import com.kuanquan.datastore.preference.UiMode
import kotlinx.android.synthetic.main.activity_datastore_preference.*
import kotlinx.android.synthetic.main.activity_main.rootView
import kotlinx.coroutines.launch

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class PreferenceDatastoreActivity : AppCompatActivity() {

    private lateinit var settingsManager: SettingsManager
    private var isDarkMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datastore_preference)

        settingsManager = SettingsManager(applicationContext)

        observeUiPreferences()
        initViews()
    }

    private fun observeUiPreferences() {
        settingsManager.uiModeFlow.asLiveData().observe(this, Observer { uiMode ->
            when (uiMode) {
                UiMode.LIGHT -> onLightMode()
                UiMode.DARK -> onDarkMode()
            }
        })
    }

    private fun initViews() {
        imageButton.setOnClickListener {
            lifecycleScope.launch {
                when (isDarkMode) {
                    true -> settingsManager.setUiMode(UiMode.LIGHT)
                    false -> settingsManager.setUiMode(UiMode.DARK)
                }
            }
        }
    }

    private fun onLightMode() {
        isDarkMode = false

        rootView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        imageButton.setImageResource(R.drawable.ic_moon)

        // Actually turn on Light mode using AppCompatDelegate.setDefaultNightMode() here
    }

    private fun onDarkMode() {
        isDarkMode = true

        rootView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
        imageButton.setImageResource(R.drawable.ic_sun)

        // Actually turn on Dark mode using AppCompatDelegate.setDefaultNightMode() here
    }
}