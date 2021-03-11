package com.example.lib.dynamicproxy;

/**
 * 动态代理
 * 被代理类 （目标类）
 */
//public class BeRepresent extends BaseBeRepresent {
public class BeRepresent implements InterfaceObject {
    private static final String TAG = BeRepresent.class.getSimpleName();
    @Override
    public void shopping() {
        System.out.println("买点东西回来");
    }
}