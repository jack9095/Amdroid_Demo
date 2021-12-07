package com.kuanquan.hook.reportSizeConfigurations;

import android.app.ActivityManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * https://www.bianchengquan.com/article/231529.html
 * 9.0系统上经常出现的异常
 */
public class ReportSizeConfigurationsHook {

    private static final String TAG = "ReportSizeHook";

    public static void hook() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            Log.i(TAG, "start attachBaseContext");
            try {
                Field am = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
                am.setAccessible(true);
                Object iActivityManagerSingleton = am.get(null);
                if (iActivityManagerSingleton == null) {
                    return;
                }

                Class<?> singletonCls = iActivityManagerSingleton.getClass().getSuperclass();
                if (singletonCls == null) {
                    return;
                }

                Field instance = singletonCls.getDeclaredField("mInstance");
                instance.setAccessible(true);
                Object iActivityManager = instance.get(iActivityManagerSingleton);

                Class<?> iActivityManagerCls = Class.forName("android.app.IActivityManager");
                Class<?>[] classes = {iActivityManagerCls};
                Object iActivityManageProxy = Proxy.newProxyInstance(
                        iActivityManagerCls.getClassLoader(),
                        classes,
                        new IActivityManagerProxy(iActivityManager));
                instance.set(iActivityManagerSingleton, iActivityManageProxy);
                Log.i(TAG, "hook IActivityManager success!");
            } catch (Throwable e) {
                Log.e(TAG, "hook IActivityManager failed! [" + e.getMessage() + "]");
            }
        } else {
            Log.i(TAG, "only hook Android 9.0, other return");
        }
    }

    private static class IActivityManagerProxy implements InvocationHandler {
        private final Object mActivityManager;

        public IActivityManagerProxy(Object iActivityManager) {
            mActivityManager = iActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("reportSizeConfigurations".equals(method.getName())) {
                try {
                    Log.i(TAG, "reportSizeConfigurations invoke");
                    return method.invoke(mActivityManager, args);
                } catch (Exception e) {
                    Log.e(TAG, "reportSizeConfigurations exception: " + e.getMessage());
                    return null;
                }
            }
            return method.invoke(mActivityManager, args);
        }
    }

}
