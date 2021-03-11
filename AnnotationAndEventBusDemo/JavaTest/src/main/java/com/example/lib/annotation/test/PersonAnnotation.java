package com.example.lib.annotation.test;


/**
 * 使用注解
 */
public class PersonAnnotation {

    @Despring(name = "北京")
    private String name;

    @Despring(age = 28)
    private int age;

    @Override
    public String toString() {
        return "PersonAnnotation{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}