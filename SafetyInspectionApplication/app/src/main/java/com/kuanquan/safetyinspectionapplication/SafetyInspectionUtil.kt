package com.kuanquan.safetyinspectionapplication

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.FileReader


object SafetyInspectionUtil {

    /**
     * 检测双开
     */
    fun check(context: Context?) {
        context?.apply {
            try {
                val filePath = filesDir?.absolutePath
                if (filePath?.length!! > 60) {
                    Log.e("container", "file目录路径有问题 当前路径:{$filePath}");
                }
                Log.e("FilePathCheckUtil", "filePath:{$filePath}")
            } catch (ignore: Throwable) {

            }
        }
    }


    /**
     * 检测是否是模拟器 x86 模拟器
     */
    @JvmStatic
    fun checkCpu(context: Context?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        context?.apply {
            try {
                val abiSb = StringBuffer()
                abiSb.append("abis:")
                if (Build.SUPPORTED_ABIS != null) {
                    for (abi in Build.SUPPORTED_ABIS) {
                        abiSb.append(abi).append(",")
                    }
                }
                abiSb.append("abis32:")

                if (Build.SUPPORTED_32_BIT_ABIS != null) {
                    for (abi in Build.SUPPORTED_32_BIT_ABIS) {
                        abiSb.append(abi).append(",")
                    }
                }
                abiSb.append("abis64:")
                if (Build.SUPPORTED_64_BIT_ABIS != null) {
                    for (abi in Build.SUPPORTED_64_BIT_ABIS) {
                        abiSb.append(abi).append(",")
                    }
                }
                Log.e("checkCpu", "检测是否是模拟器 CPU:{${abiSb.toString()}}")
            } catch (ignore: Throwable) {

            }
        }
    }

    /**
     * 查找设备安装目录中是否存在hook工具
     */
    private fun findHookAppName(context: Context?): Boolean {
        val packageManager = context?.packageManager
        val applicationInfoList = packageManager
            ?.getInstalledApplications(PackageManager.GET_META_DATA)
        applicationInfoList?.forEach { applicationInfo ->
            if (applicationInfo.packageName == "de.robv.android.xposed.installer") {
                Log.wtf("HookDetection", "Xposed found on the system.")
                return true
            }
            if (applicationInfo.packageName == "com.saurik.substrate") {
                Log.wtf("HookDetection", "Substrate found on the system.")
                return true
            }
        }
        return false
    }

    /**
     * 查找设备存储中是否存在hook安装文件
     */
    private fun findHookAppFile(): Boolean {
        try {
            val libraries: MutableSet<String> = HashSet()
            val mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps"
            val reader = BufferedReader(FileReader(mapsFilename))
            var line: String
            while (reader.readLine().also { line = it } != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    val n = line.lastIndexOf(" ")
                    libraries.add(line.substring(n + 1))
                }
            }
            reader.close()
            for (library in libraries) {
                if (library.contains("com.saurik.substrate")) {
                    Log.wtf(
                        "HookDetection",
                        "Substrate shared object found: $library"
                    )
                    return true
                }
                if (library.contains("XposedBridge.jar")) {
                    Log.wtf("HookDetection", "Xposed JAR found: $library")
                    return true
                }
            }
        } catch (e: Exception) {
            Log.wtf("HookDetection", e.toString())
        }
        return false
    }

    /**
     * 查找程序运行的栈中是否存在hook相关类
     */
    private fun findHookStack(): Boolean {
        try {
            throw java.lang.Exception("findhook")
        } catch (e: java.lang.Exception) {

            // 读取栈信息
            // for(StackTraceElement stackTraceElement : e.getStackTrace()) {
            // Log.wtf("HookDetection", stackTraceElement.getClassName() + "->"+
            // stackTraceElement.getMethodName());
            // }
            var zygoteInitCallCount = 0
            for (stackTraceElement in e.stackTrace) {
                if (stackTraceElement.className == "com.android.internal.os.ZygoteInit") {
                    zygoteInitCallCount++
                    if (zygoteInitCallCount == 2) {
                        Log.wtf("HookDetection", "Substrate is active on the device.")
                        return true
                    }
                }
                if (stackTraceElement.className == "com.saurik.substrate.MS$2" && stackTraceElement.methodName == "invoked") {
                    Log.wtf(
                        "HookDetection",
                        "A method on the stack trace has been hooked using Substrate."
                    )
                    return true
                }
                if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" && stackTraceElement.methodName == "main") {
                    Log.wtf("HookDetection", "Xposed is active on the device.")
                    return true
                }
                if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" && stackTraceElement.methodName == "handleHookedMethod") {
                    Log.wtf(
                        "HookDetection",
                        "A method on the stack trace has been hooked using Xposed."
                    )
                    return true
                }
            }
        }
        return false
    }

    /**
     * 综合判断 （true为hook）
     */
    fun isHook(context: Context?): Boolean {
        return findHookAppName(context) || findHookAppFile() || findHookStack()
    }

    /**
     * 这种方式会弹窗，如果用户点击拒绝授权那么判断依然是没有root
     */
    private fun checkRootExecutable(): Boolean {
        var process: Process? = null
        var os: DataOutputStream? = null
        return try {
            process = Runtime.getRuntime().exec("su")
            os = DataOutputStream(process.outputStream)
            os.writeBytes("exit\n")
            os.flush()
            val exitValue: Int = process.waitFor()
            return exitValue == 0
        } catch (e: java.lang.Exception) {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: " + e.message)
            return false
        } finally {
            try {
                os?.close()
                process?.destroy()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}