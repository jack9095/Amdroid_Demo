package com.kuanquan.test.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class BaseMainActivity: AppCompatActivity() {

    /************************************************  跳转第三方APP  ********************************************/
    fun aiqiyi(view: View?) {
        startAPP("com.qiyi.video")
    }

    fun TencentVideo(view: View?) {
        startAPP("com.tencent.qqlive")
    }

    fun youku(view: View?) {
        startAPP("com.youku.phone")
    }

    fun bilibili(view: View?) {
        startAPP("tv.danmaku.bili")
    }

    fun souhu(view: View?) {
        startAPP("com.sohu.sohuvideo")
    }

    fun haokan(view: View?) {
        startAPP("com.baidu.haokan")
    }

    fun xigua(view: View?) {
        startAPP("com.ss.android.article.video")
    }

    fun mangguo(view: View?) {
        startAPP("com.hunantv.imgo.activity")
    }

    fun hanju(view: View?) {
        startAPP("com.babycloud.hanju")
    }

    fun renren(view: View?) {
        startAPP("com.zhongduomei.rrmj.society")
    }

    private fun startAPP(packageName: String) {
        // 获取目标应用安装包的 Intent
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    /**
     * 检测 app 是否安装
     * @param context 上下文
     * @param packageName 包名
     * @return true 存在 false 不存在
     */
    @SuppressLint("WrongConstant")
    private fun uninstallSoftware(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
            if (packageInfo != null) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}