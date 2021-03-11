package com.kuanquan.annotationandeventbusdemo.eventbus.annotation;

import com.kuanquan.annotationandeventbusdemo.eventbus.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解在程序中没有任何功能性作用，他就是一个表示，告诉虚拟机这个是做什么用的，相当于大街上各种商店的牌匾一样，你看一眼就知道这个商店是做什么的
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    ThreadMode value() default ThreadMode.POSTING;
    boolean stick() default false;
}
