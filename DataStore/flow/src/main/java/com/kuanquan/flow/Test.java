package com.kuanquan.flow;



public class Test {

    public static void main(String[] args) {
        String str = null;
        String format = String.format(str != null ? str : "", "90");
        System.out.println("format -> " + format);
        System.out.println(17 / 3);
    }
}
