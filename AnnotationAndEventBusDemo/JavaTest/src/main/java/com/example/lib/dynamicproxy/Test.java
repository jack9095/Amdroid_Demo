package com.example.lib.dynamicproxy;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        InterfaceObject mBeRepresent = new BeRepresent();
        ProxyF proxy = new ProxyF(mBeRepresent); // 代理类
        // java.lang.reflect.Proxy.newProxyInstance(....)方法获得真实对象的代理对象
        /**
         * 第一个参数，代理类实现接口的类加载器
         * 第二个参数，代理类要实现的接口中的方法
         * 第三个参数，InvocationHandler
         */
        InterfaceObject mInterfaceObject = (InterfaceObject) Proxy.newProxyInstance(
                mBeRepresent.getClass().getClassLoader(),
                mBeRepresent.getClass().getInterfaces(),
                proxy
        );
        // 通过代理对象调用真实对象相关接口中实现的方法，这个时候就会跳转到这个代理对象所关联的InvocationHandler的invoke方法中
        mInterfaceObject.shopping();
        // 获得真实对象的代理对象所对应的class对象的名称，用字符串表示
        System.out.println("代理对象名称 = " + mInterfaceObject.getClass().getName());
    }

}