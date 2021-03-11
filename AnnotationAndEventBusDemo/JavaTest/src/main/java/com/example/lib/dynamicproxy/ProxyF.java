package com.example.lib.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理
 * 代理类:每个代理类的对象都会关联一个表示内部处理逻辑的 InvocationHandler 接口的实现
 * 当使用者调用了代理对象，所实现的代理对象的接口时，这个调用的信息就会传递到 InvocationHandler
 * 中的 invoke 方法中
 */
public class ProxyF implements InvocationHandler {

    private static final String TAG = ProxyF.class.getSimpleName();
    private Object target; // 要代理的真实对象

    public ProxyF(Object target) {
        this.target = target;
    }

    /**
     * 相当于一个拦截的方法
     * @param proxy   代理对象
     * @param method  代理方法
     * @param objects 代理方法中的参数
     * @return 返回值会返回给使用者
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        System.out.println("代理对象:" + proxy.getClass().getName());
        System.out.println("代理方法中的参数:" + (objects != null && objects.length > 0 ? objects[0] : null));
        System.out.println("代理方法:" + method);
        System.out.println(" before ");
        method.invoke(target,objects);
        System.out.println(" after ");
        return null;
    }
}