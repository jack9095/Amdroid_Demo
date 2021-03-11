package com.example.lib.annotation.test.copy_function;

import java.util.HashMap;

public class Demo {
    public static void main(String[] strs){
        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("aa","北京");
//        String orDefault = (String) hashMap.getOrDefault("aa", "上海");
//        System.out.println(orDefault); // map中存在"aa"，所以值为北京
//        String valueA = (String) hashMap.getOrDefault("bb", "上海");
//        System.out.println(valueA);   // map中存在"bb"，所以值为上海
//
//        Object obj = hashMap.putIfAbsent("cc", null);
//        if (obj == null) {
//            System.out.println("打印返回值 : null");
//        }
//        hashMap.putIfAbsent("cc",1);
//        hashMap.putIfAbsent("cc",2);
//        hashMap.putIfAbsent("dd", 100);
//        hashMap.putIfAbsent("dd", 200);
//        Integer i = (Integer)hashMap.putIfAbsent("dd", 300);
//        hashMap.put("ee", 400);
//        hashMap.put("ee", 500);
//        System.out.println(hashMap);
//        System.out.println(i);

        Person person = new Person("lucy","女");
        hashMap.put("lucy", person);
        HashMap<String,Object> cloneMap = (HashMap<String, Object>) hashMap.clone();
        System.out.println("*************************   不做改变   ***********************************");
        System.out.println("未改变之前, hashMap的值:"+hashMap.toString());
        System.out.println("未改变之前,cloneMap的值:"+cloneMap.toString());
        System.out.println("map和cloneMap是否指向同一内存地址:"+(hashMap==cloneMap));
        System.out.println("map和cloneMap中存储的Person是否指向同一内存地址:"+(hashMap.get("lucy")==cloneMap.get("lucy")));
        System.out.println();
        System.out.println("*************************  对cloneMap中的值进行改变，看是否能影响到hashMap  ***********************************");
        Person clonePerson = (Person) cloneMap.get("lucy");
        clonePerson.setSex("男");
        System.out.println("改变之后,cloneMap的值:" + cloneMap.toString());
        System.out.println("改变之后, hashMap的值:" + hashMap.toString());
        System.out.println();
        System.out.println("*************************  对hashMap新增  **********************************");
        Person newPerson = new Person("jack","男");
        hashMap.put("jack", newPerson);
        System.out.println("改变之后,cloneMap的值:" + cloneMap.toString());
        System.out.println("改变之后, hashMap的值:" + hashMap.toString());
    }
}