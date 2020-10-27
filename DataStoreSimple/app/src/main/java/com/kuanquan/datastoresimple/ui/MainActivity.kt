package com.kuanquan.datastoresimple.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.kuanquan.datastoresimple.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * com.tencent.mobileqq
 *
 * com.tencent.mm
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

//    private val mainViewModel: MainViewModel by viewModels()
    private val mainViewModel by viewModels<MainViewModel>()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a = 30
        a.toFloat()
        if (uninstallSoftware(this, "com.tencent.mobileqq")) {
            Log.e("MainActivity", "安装 = " + android.os.Build.VERSION.RELEASE)
        } else {
            Log.e("MainActivity", "没有安装 = " + android.os.Build.VERSION.RELEASE)
        }

//        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        mainViewModel.setData(RepositoryModule().provideSharedPreferencesRepository(),RepositoryModule().provideDataStoreRepository())
        with(switchAccount) {
            isChecked = mainViewModel.getData(PreferencesKeys.KEY_ACCOUNT) ?: false
            setOnClickListener(this@MainActivity)
        }

        switchWeiBo.setOnClickListener(this@MainActivity)
        switchGitHub.setOnClickListener(this@MainActivity)
        btnMrge.setOnClickListener(this)

        initObserve()
    }

    private fun initObserve() {
        /**
         * KEY_WEI_BO 或者 KEY_GITHUB 只要其中一个变化了，都会收到通知
         */
        mainViewModel.getDataStore(PreferencesKeys.KEY_WEI_BO)
            ?.observe(this@MainActivity) {
                switchWeiBo.isChecked = it
            }

        mainViewModel.getDataStore(PreferencesKeys.KEY_GITHUB)
            ?.observe(this@MainActivity) {
                switchGitHub.isChecked = it
            }
    }

    override fun onClick(v: View) {
        when (v) {
            // 公众号
            switchAccount -> mainViewModel.saveData(PreferencesKeys.KEY_ACCOUNT)
            // 微博
            switchWeiBo -> mainViewModel.saveDataByDataStore(PreferencesKeys.KEY_WEI_BO)
            // GitHub
            switchGitHub -> mainViewModel.saveDataByDataStore(PreferencesKeys.KEY_GITHUB)
            // 迁移
            btnMrge -> {
                /**
                 *  传入 migrations 参数，构建一个 DataStore 之后，
                 *  需要执行 一次读取 或者 写入，DataStore 才会自动合并 SharedPreference 文件内容
                 */
                mainViewModel.migrationSP2DataStore()
                lifecycleScope.launch {
                    mainViewModel.testMigration(PreferencesKeys.KEY_BYTE_CODE)?.collect()
                }
                Toast.makeText(this@MainActivity, "迁移成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun uninstallSoftware(context: Context, packageName: String): Boolean {
        val packageManager: PackageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            )
            if (packageInfo != null) {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

}