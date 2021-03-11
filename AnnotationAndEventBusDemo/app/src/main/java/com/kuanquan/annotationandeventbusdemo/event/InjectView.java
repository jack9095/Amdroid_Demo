package com.kuanquan.annotationandeventbusdemo.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {

   // 方法名称必须得写 value() 不然在使用的时候还得 方法名 = 输入的值
   int value();


//   int valueA();
}

