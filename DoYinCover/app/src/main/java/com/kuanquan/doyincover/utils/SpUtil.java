package com.kuanquan.doyincover.utils;

import android.content.SharedPreferences;

/**
 * Author:  lijie
 * Date:   2018/12/12
 * Email:  2607401801@qq.com
 */
public class SpUtil {
    private static SharedPreferences mSp;

    public static void init(SharedPreferences sp) {
        mSp = sp;
    }

    public static void putInt(String key, int value) {
        mSp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int value) {
        return mSp.getInt(key, value);
    }

    public static void saveOrUpdate(String key, String value) {
        mSp.edit().putString(key, value).apply();
    }

    public static String find(String key) {
        return mSp.getString(key, null);
    }

    public static void delete(String key) {
        mSp.edit().remove(key).apply();
    }

    public static void clearAll() {
        mSp.edit().clear().apply();
    }

    public static boolean has(String key) {
        return mSp.contains(key);
    }

    //存boolean值
    public static void saveOrUpdateBoolean(String key, Boolean value) {
        mSp.edit().putBoolean(key, value).apply();
    }

    //取boolean值
    public static boolean findBoolean(String key) {
        return mSp.getBoolean(key, false);
    }
}
