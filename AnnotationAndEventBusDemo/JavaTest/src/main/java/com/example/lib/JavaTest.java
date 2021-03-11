package com.example.lib;

import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.ProxyGenerator;

public class JavaTest {

    static int cnt = 6;
    static {
        cnt += 9;
    }

    public static void main(String[] args) {

        ClassLoader classLoader = System.class.getClassLoader();
        System.out.println("System = " + classLoader);


        new Thread(){
            @Override
            public void run() {
                super.run();
                // 获取当前线程的类加载器
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                System.out.println("Thread = " + contextClassLoader);
            }
        }.start();

//        Integer a = new Integer(9);

//        Integer b = new Integer(9);
//        Integer b = a;

//        Test a = new Test();
//        Test b = new Test();
//        b = a;

//        System.out.println(a==b);
//        System.out.println(divx());

//        int i = 18 / 5;
//        System.out.println(i);
//
//        int i1 = 18 % 5;

//        System.out.println("cnt =" + cnt);
//        System.out.println("cnt =" + JavaTest.class.getCanonicalName());
    }

    public static int div(){
        try {
            return 3;
        }catch (ArithmeticException e){
            System.out.println("catch in div");
            return 2;
        } finally {
            System.out.println("finally in div");
            return 1;
        }
    }

    public static int divx(){
        int a = 0;
        try {
            a = 3;
            System.out.println("try in div");
            return 6;
        }catch (ArithmeticException e){
            System.out.println("catch in div");
            a = 2;
            return a;
        } finally {
            a = 1;
            System.out.println("finally in div");
//            return a;
        }
    }

    static {
        cnt /= 3;

    }


    public static void generateClassFile(Class clazz,String proxyName)
    {
        //根据类信息和提供的代理类名称，生成字节码
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces());
        String paths = clazz.getResource(".").getPath();
        System.out.println(paths);
        System.out.println(paths);
        FileOutputStream out = null;

        try {
            //保留到硬盘中
            out = new FileOutputStream(paths+proxyName+".class");
            out.write(classFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
