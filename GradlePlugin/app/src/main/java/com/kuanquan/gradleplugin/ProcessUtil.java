package com.kuanquan.gradleplugin;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 进程相关工具类
 * 参考 https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/src/main/
 * java/com/blankj/utilcode/util/ProcessUtil.java
 *
 * @author xzy
 */
public class ProcessUtil {
    private static String currentProcessName;

    /**
     * @return 当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //1)通过Application的API获取当前进程名
        currentProcessName = getCurrentProcessNameByApplication(context);
        if (BuildConfig.DEBUG) {
            Log.i("ProcessUtil", "1-> " + currentProcessName);
        }
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //2)通过Linux /proc/pid/cmdline 进程文件获取当前进程名
        currentProcessName = getCurrentProcessNameByCmdFile();
        if (BuildConfig.DEBUG) {
            Log.i("ProcessUtil", "2-> " + currentProcessName);
        }
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //3)通过反射ActivityThread获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityThread();
        if (BuildConfig.DEBUG) {
            Log.i("ProcessUtil", "3-> " + currentProcessName);
        }
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }

        //4)通过ActivityManager获取当前进程名
        currentProcessName = getCurrentProcessNameByActivityManager(context);
        if (BuildConfig.DEBUG) {
            Log.i("ProcessUtil", "4-> " + currentProcessName);
        }

        if (TextUtils.isEmpty(currentProcessName)) {
            currentProcessName = "non";
        }
        if (BuildConfig.DEBUG) {
            Log.i("ProcessUtil", "5-> " + currentProcessName);
        }
        return currentProcessName;
    }

    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    private static String getCurrentProcessNameByApplication(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName();
        }
        return null;
    }

    /**
     * 通过Linux /proc/pid/cmdline 进程文件获取进程名
     */
    private static String getCurrentProcessNameByCmdFile() {
        String processName = null;
        BufferedReader mBufferedReader = null;
        try {
            int pid = android.os.Process.myPid();
            File file = new File("/proc/" + pid + "/cmdline");
            mBufferedReader = new BufferedReader(new FileReader(file));
            processName = mBufferedReader.readLine().trim();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (mBufferedReader != null) {
                    mBufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了IPC
     */
    private static String getCurrentProcessNameByActivityThread() {
        String processName = null;
        try {
            final Method declaredMethod = Class.forName(
                    "android.app.ActivityThread",
                    false,
                    Application.class.getClassLoader())
                    .getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke != null && invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return processName;
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    private static String getCurrentProcessNameByActivityManager(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningAppList = am.getRunningAppProcesses();
            if (runningAppList != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppList) {
                    if (processInfo != null && processInfo.pid == pid) {
                        return processInfo.processName;
                    }
                }
            }
        }
        return null;
    }

}
