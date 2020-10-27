package com.kuanquan.bitmask;

public class TestClass {
    public static void main(String[] args) {
//        int a = 10;
//        int b = -10;
//        System.out.println(Integer.toBinaryString(a)); // 1010；toBinaryString方法会省略掉前面的0
//        System.out.println(Integer.toBinaryString(b)); // 11111111111111111111111111110110；确实是反码+1


        int a = 10; //00000000000000000000000000001010
        int b = 9; //00000000000000000000000000001001
        System.out.println(a & b); //8，00000000000000000000000000001000，都为1则为1，否则为0
        System.out.println(a | b); //11，00000000000000000000000000001011，只要有一个为1就为1
        System.out.println(a ^ b); //3，00000000000000000000000000000011，相同为0，不同为1
        System.out.println(~a); //-11，11111111111111111111111111110101，按位取反

    }
}