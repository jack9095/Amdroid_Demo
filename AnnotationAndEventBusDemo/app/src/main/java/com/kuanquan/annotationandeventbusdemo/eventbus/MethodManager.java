package com.kuanquan.annotationandeventbusdemo.eventbus;

import java.lang.reflect.Method;

public class MethodManager {

    private Class<?> type; // 方法参数
    private Method method; // 方法
    private ThreadMode threadMode; // 线程模型
    private boolean isStick; // 是否是粘性事件

    public MethodManager() {
    }

    public MethodManager(Class<?> type, Method method, ThreadMode threadMode, boolean isStick) {
        this.type = type;
        this.method = method;
        this.threadMode = threadMode;
        this.isStick = isStick;
    }

    public Class<?> getType() {
        return type;
    }

    public Method getMethod() {
        return method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public boolean isStick() {
        return isStick;
    }
}
