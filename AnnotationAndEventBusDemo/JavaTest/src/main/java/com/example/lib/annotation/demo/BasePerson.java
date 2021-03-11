package com.example.lib.annotation.demo;

@MyAnnotation
public interface BasePerson {

    @MyAnnotation
    void name();

    @MyAnnotation
    void say();

    @MyAnnotation
    void age();
}