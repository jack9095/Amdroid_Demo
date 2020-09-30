package com.mouble.baselibrary.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 * 类名：SharedPreferencesUtils
 * 类描述：
 * 创建人：fei.wang
 * 创建日期： 2020/5/7.
 * 版本：V1.0
 */
object SharedPreferencesUtils {
    private var mSharedPreferences: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    @SuppressLint("CommitPrefEdits")
    fun getInstance(PREF_NAME: String, context: Context) {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        mEditor = mSharedPreferences?.edit()
    }

    fun getSharePrefString(key: String?, defValue: String?): String? {
        return mSharedPreferences!!.getString(key, defValue)
    }

    fun getSharePrefString(key: String?): String? {
        return mSharedPreferences!!.getString(key, "")
    }

    fun getSharePrefBoolean(key: String?, defValue: Boolean): Boolean {
        return mSharedPreferences!!.getBoolean(key, defValue)
    }

    fun getSharePrefBoolean(key: String?): Boolean {
        return mSharedPreferences!!.getBoolean(key, false)
    }

    fun getSharePrefInteger(key: String?): Int {
        return mSharedPreferences!!.getInt(key, -1)
    }

    fun getSharePrefInteger(key: String?, defValue: Int): Int {
        return mSharedPreferences!!.getInt(key, defValue)
    }

    fun getSharePrefLong(key: String?): Long {
        return mSharedPreferences!!.getLong(key, -1)
    }

    fun getSharePrefLong(key: String?, value: Int): Long {
        return mSharedPreferences!!.getLong(key, -1)
    }

    fun getSharePrefFloat(key: String?): Float {
        return mSharedPreferences!!.getFloat(key, -1f)
    }

    fun putSharePrefString(key: String?, value: String?): Boolean {
        mEditor?.putString(key, value)
        return mEditor?.commit()!!
    }

    fun putSharePrefBoolean(key: String?, value: Boolean): Boolean {
        mEditor?.putBoolean(key, value)
        return mEditor?.commit()!!
    }

    fun putSharePrefFloat(key: String?, value: Float): Boolean {
        mEditor?.putFloat(key, value)
        return mEditor?.commit()!!
    }

    fun putSharePrefLong(key: String?, value: Long): Boolean {
        mEditor?.putLong(key, value)
        return mEditor?.commit()!!
    }

    fun putSharePrefInteger(key: String?, value: Int): Boolean {
        mEditor?.putInt(key, value)
        return mEditor?.commit()!!
    }

    fun putStrListValue(key: String, strList: List<String?>?) {
        if (null == strList) {
            return
        }
        // 保存之前先清理已经存在的数据，保证数据的唯一性
//        removeStrList(key);
        val size = strList.size
        putSharePrefInteger(key + "size", size)
        for (i in 0 until size) {
            putSharePrefString(key + i, strList[i])
        }
    }

    fun getStrListValue(key: String): MutableList<String?> {
        val strList: MutableList<String?> =
            ArrayList()
        val size = getSharePrefInteger(key + "size", 0)
        //Log.d("sp", "" + size);
        for (i in 0 until size) {
            strList.add(getSharePrefString(key + i, null))
        }
        return strList
    }

    fun removeStrList(key: String) {
        val size = getSharePrefInteger(key + "size", 0)
        if (0 == size) {
            return
        }
        removeKey(key + "size")
        for (i in 0 until size) {
            removeKey(key + i)
        }
    }

    /**
     * @Description TODO 清空List<String>单条数据
     * @param context
     * @param key
     * List<String>对应的key
     * @param str
     * List<String>中的元素String
    </String></String></String> */
    fun removeStrListItem(context: Context?, key: String, str: String) {
        val size = getSharePrefInteger(key + "size", 0)
        if (0 == size) {
            return
        }
        val strList = getStrListValue(key)
        // 待删除的List<String>数据暂存
        val removeList: MutableList<String?> = ArrayList()
        for (i in 0 until size) {
            if (str == strList[i]) {
                if (i in 0 until size) {
                    removeList.add(strList[i])
                    removeKey(key + i)
                    putSharePrefInteger(key + "size", size - 1)
                }
            }
        }
        strList.removeAll(removeList)
        // 删除元素重新建立索引写入数据
        putStrListValue(key, strList)
    }

    fun removeKey(key: String?): Boolean {
        mEditor?.remove(key)
        return mEditor?.commit()!!
    }

    fun clear(): Boolean {
        mEditor?.clear()
        return mEditor?.commit()!!
    }
}