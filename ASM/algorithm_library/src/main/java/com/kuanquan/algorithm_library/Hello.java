package com.kuanquan.algorithm_library;

/**
 * 使用 ASM 对 Hello 类 show() 方法的耗时进行计算并打印
 */
public class Hello {

    public static void main(String[] args) {
        show();
    }

    public static void show(){
        System.out.println("Hello World");
    }
}
