package com.kuanquan.asm;

/**
 * https://blog.csdn.net/weixin_42484796/article/details/105408391
 */
public class HelloASM {

    public static void main(String[] args) throws InterruptedException {
        long s1 = System.currentTimeMillis();
        Thread.sleep(1000L);
        long s2 = System.currentTimeMillis();
        System.out.println(s2 - s1);
    }
}
