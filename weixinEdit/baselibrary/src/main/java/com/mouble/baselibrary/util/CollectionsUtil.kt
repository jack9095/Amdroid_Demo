package com.mouble.baselibrary.util

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.widget.TextView

object CollectionsUtil {

    fun isListEmpty(list: List<*>?): Boolean {
        return list?.isEmpty() ?: true
    }

    fun isMapEmpty(map: Map<*, *>?): Boolean {
        return map?.isEmpty() ?: true
    }

    fun isSetEmpty(set: Set<*>?): Boolean {
        return set?.isEmpty() ?: true
    }

    fun setTextView(tv: TextView, str: String?) {
        if (!TextUtils.isEmpty(str) && !TextUtils.equals("null", str)) {
            tv.text = str
        } else {
            tv.text = ""
        }
    }

    fun setTextViewOne(tv: TextView, str: String?) {
        if (!TextUtils.isEmpty(str) && !TextUtils.equals("null", str)) {
            tv.text = str
        } else {
            tv.text = "--"
        }
    }

    /**
     * 获取应用程序的版本号
     * @return
     */
    @JvmStatic
    fun getVersionName(context: Context): String? { //1.包的管理者，获取应用程序中清单文件中信息
        val packageManager = context.packageManager
        return try { //2.根据包名获取应用程序相关信息
            //packageName : 应用程序的包名
            //flags ： 指定信息的标签，指定了标签就会获取相应标签对应的相关信息
            //PackageManager.GET_ACTIVITIES : 获取跟activity相关的信息
            //getPackageName() : 获取应用程序的包名
            val packageInfo =
                packageManager.getPackageInfo(context.packageName, 0)
            //3.获取应用程序版本号名称
            val versionName = packageInfo.versionName
            val longVersionCode = packageInfo.versionCode.toLong()
            versionName
        } catch (e: PackageManager.NameNotFoundException) { //找不到包名的异常
            e.printStackTrace()
            null
        }
    }
}