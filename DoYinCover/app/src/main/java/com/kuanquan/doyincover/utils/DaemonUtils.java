package com.kuanquan.doyincover.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DaemonUtils {

    /**
     * The method solved the issue of
     * "Fatal Exception: java.util.concurrent.TimeoutException
     * android.content.res.AssetManager.finalize() timed out after 120 seconds
     * android.content.res.AssetManager.destroy (AssetManager.java)
     * android.content.res.AssetManager.finalize (AssetManager.java:576)
     * java.lang.Daemons$FinalizerDaemon.doFinalize (Daemons.java:217)
     * java.lang.Daemons$FinalizerDaemon.run (Daemons.java:200)
     * java.lang.Thread.run (Thread.java:818)"
     * <p>
     * https://stackoverflow.com/questions/24021609/how-to-handle-java-util-concurrent-timeoutexception-android-os-binderproxy-fin
     */
    public static void fixFinalizerWatchdogDemon() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
