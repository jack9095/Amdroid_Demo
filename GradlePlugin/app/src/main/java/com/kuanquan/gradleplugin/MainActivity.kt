package com.kuanquan.gradleplugin

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileLock

/**
 * https://juejin.cn/post/6950091477192015902/
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleWebviewDir(this)
    }

    private fun handleWebviewDir(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        try {
            val pathSet: MutableSet<String> = HashSet()
            var suffix = ""
            val dataPath: String = context.dataDir.absolutePath
            val webViewDir = "/app_webview"
            val huaweiWebViewDir = "/app_hws_webview"
            val lockFile = "/webview_data.lock"
            val processName = ProcessUtil.getCurrentProcessName(context)
            if (!TextUtils.equals(context.packageName, processName)) { //判断不等于默认进程名称
                suffix =
                    if (TextUtils.isEmpty(processName)) context.packageName else processName
                WebView.setDataDirectorySuffix(suffix)
                suffix = "_$suffix"
                pathSet.add(dataPath + webViewDir + suffix + lockFile)
                if (RomUtils.isHuawei()) {
                    pathSet.add(dataPath + huaweiWebViewDir + suffix + lockFile)
                }
            } else {
                //主进程
                suffix = "_$processName"
                pathSet.add(dataPath + webViewDir + lockFile) //默认未添加进程名后缀
                pathSet.add(dataPath + webViewDir + suffix + lockFile) //系统自动添加了进程名后缀
                if (RomUtils.isHuawei()) { //部分华为手机更改了webview目录名
                    pathSet.add(dataPath + huaweiWebViewDir + lockFile)
                    pathSet.add(dataPath + huaweiWebViewDir + suffix + lockFile)
                }
            }
            for (path in pathSet) {
                val file = File(path)
                if (file.exists()) {
                    tryLockOrRecreateFile(file)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun tryLockOrRecreateFile(file: File) {
        try {
            val tryLock: FileLock = RandomAccessFile(file, "rw").channel.tryLock()
            if (tryLock != null) {
                tryLock.close()
            } else {
                createFile(file, file.delete())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            var deleted = false
            if (file.exists()) {
                deleted = file.delete()
            }
            createFile(file, deleted)
        }
    }

    private fun createFile(file: File, deleted: Boolean) {
        try {
            if (deleted && !file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}