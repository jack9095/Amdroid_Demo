package com.kuanquan.annotationandeventbusdemo.event;

import android.app.Activity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 这里 Activity 相当于 被代理的类
 */
public class ProxyHandler implements InvocationHandler {


    Activity activity;

    // key 为注解中写的方法名称，value 为在被代理类 Activity 中使用类注解的方法
    HashMap<String, Method> hashMap;

    public ProxyHandler(Activity activity) {
        this.activity = activity;
        hashMap = new HashMap<>();
    }


    public void mapMethod(String name, Method method) {
        hashMap.put(name, method);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String name = method.getName();

        Method realMethod = hashMap.get(name);

        if (realMethod != null) {
            // 将 onClick 方法映射到 Activity 中的 invokeOnClick() 方法中
            return realMethod.invoke(activity,args);
        }

        return null;
    }
}
