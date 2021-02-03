package com.kuanquan.commentutillibrary

import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.TypedValue
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

object ScreenUtil {

    private const val TAG = "ScreenUtil"
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private const val FLYME = "flyme"
    private const val ZTEC2016 = "zte c2016"
    private const val ZUKZ1 = "zuk z1"
    private const val ESSENTIAL = "essential"
    private const val XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar"
    private const val HUAWAI_DISPLAY_NOTCH_STATUS = "display_notch_status"
    private const val XIAOMI_DISPLAY_NOTCH_STATUS = "force_black"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private var sIsTabletChecked = false
    private var sIsTabletValue = false
    private val BRAND = Build.BRAND.toLowerCase(Locale.ROOT)

    init {
        val properties = Properties()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
                fileInputStream =
                    FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
                properties.load(fileInputStream)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        try {
            val clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun _isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为平板设备
     */
    fun isTablet(context: Context): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue = _isTablet(context)
        sIsTabletChecked = true
        return sIsTabletValue
    }

    /**
     * 判断是否是flyme系统
     */
    fun isFlyme(): Boolean =
        !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean = !TextUtils.isEmpty(sMiuiVersionName)

    fun isMIUIV5(): Boolean = "v5" == sMiuiVersionName

    fun isMIUIV6(): Boolean = "v6" == sMiuiVersionName

    fun isMIUIV7(): Boolean = "v7" == sMiuiVersionName

    fun isMIUIV8(): Boolean = "v8" == sMiuiVersionName

    fun isMIUIV9(): Boolean = "v9" == sMiuiVersionName

    fun isFlymeLowerThan8(): Boolean {
        var isLower = false
        if (sFlymeVersionName != null && sFlymeVersionName != "") {
            val pattern = Pattern.compile("(\\d+\\.){2}\\d")
            val matcher = pattern.matcher(sFlymeVersionName)
            if (matcher.find()) {
                val versionString = matcher.group()
                if (versionString != null && versionString != "") {
                    val version = versionString.split("\\.".toRegex()).toTypedArray()
                    if (version.isNotEmpty()) {
                        if (version[0].toInt() < 8) {
                            isLower = true
                        }
                    }
                }
            }
        }
        return isMeizu() && isLower
    }

    fun isFlymeVersionHigher5_2_4(): Boolean {
        //查不到默认高于5.2.4
        var isHigher = true
        try {
            if (sFlymeVersionName != null && sFlymeVersionName != "") {
                val pattern = Pattern.compile("(\\d+\\.){2}\\d")
                val matcher = pattern.matcher(sFlymeVersionName)
                if (matcher.find()) {
                    val versionString = matcher.group()
                    if (versionString != null && versionString != "") {
                        val version = versionString.split("\\.".toRegex()).toTypedArray()
                        if (version.size == 3) {
                            if (version[0].toInt() < 5) {
                                isHigher = false
                            } else if (version[0].toInt() > 5) {
                                isHigher = true
                            } else {
                                if (version[1].toInt() < 2) {
                                    isHigher = false
                                } else if (version[1].toInt() > 2) {
                                    isHigher = true
                                } else {
                                    if (version[2].toInt() < 4) {
                                        isHigher = false
                                    } else if (version[2].toInt() >= 5) {
                                        isHigher = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isMeizu() && isHigher
    }

    fun isMeizu(): Boolean = isPhone(MEIZUBOARD) || isFlyme()

    /**
     * 判断是否为小米
     * https://dev.mi.com/doc/?p=254
     */
    fun isXiaomi(): Boolean {
        return Build.MANUFACTURER.toLowerCase() == "xiaomi"
    }

    fun isVivo(): Boolean = BRAND.contains("vivo") || BRAND.contains("bbk")

    fun isOppo(): Boolean = BRAND.contains("oppo")

    fun isHuawei(): Boolean = BRAND.contains("huawei") || BRAND.contains("honor")

    fun isEssentialPhone(): Boolean = BRAND.contains("essential")

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    fun isZUKZ1(): Boolean {
        val board = Build.MODEL
        return board != null && board.toLowerCase(Locale.ROOT).contains(ZUKZ1)
    }

    fun isZTKC2016(): Boolean {
        val board = Build.MODEL
        return board != null && board.toLowerCase(Locale.ROOT).contains(ZTEC2016)
    }

    private fun isPhone(boards: Array<String>): Boolean {
        val board = Build.BOARD ?: return false
        for (board1 in boards) {
            if (board == board1) return true
        }
        return false
    }

    /**
     * 判断悬浮窗权限（目前主要用户魅族与小米的检测）。
     */
    fun isFloatWindowOpAllowed(context: Context?): Boolean {
        if (context == null) return false
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
        } else {
            try {
                context.applicationInfo.flags and 1 shl 27 == 1 shl 27
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method = manager.javaClass.getDeclaredMethod(
                    "checkOp",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                val property =
                    method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
                return AppOpsManager.MODE_ALLOWED == property
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }
        }
        if (name != null) name = name.toLowerCase(Locale.ROOT)
        return name
    }

    fun huaweiIsNotchSetToShowInSetting(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        // 0: 默认
        // 1: 隐藏显示区域
        val result = Settings.Secure.getInt(context.contentResolver, HUAWAI_DISPLAY_NOTCH_STATUS, 0)
        return result == 0
    }

    fun dp2px(dp: Float): Int {
        return dp2px(null, dp)
    }

    fun dp2px(context: Context?, dp: Float): Int {
        context ?: (dp * 3).toInt()
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context?.resources?.displayMetrics
        )
        return px.roundToInt()
    }
}