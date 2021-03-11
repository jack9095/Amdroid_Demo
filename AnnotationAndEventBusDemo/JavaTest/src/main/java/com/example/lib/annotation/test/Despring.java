package com.example.lib.annotation.test;

import java.lang.annotation.*;

@Target(ElementType.FIELD)   //  FIELD 成员变量
//@Target(ElementType.TYPE)   // TYPE 作用对象类／接口／枚举
//@Target(ElementType.METHOD)   // METHOD 成员方法
//@Target(ElementType.PARAMETER)   // PARAMETER 方法参数
//@Target(ElementType.ANNOTATION_TYPE)   // ANNOTATION_TYPE 注解的注解
//@Target({ElementType.TYPE,ElementType.METHOD})   // 作用域
@Retention(RetentionPolicy.RUNTIME)  // 源码、编译时、运行时
@Inherited                   // 允许子继承
@Documented                  //生成javadoc时会包含注解
public @interface Despring {  // 注解的名字为 Desprion

    String desc() default "怀念与想念";
    int age() default 18;
    String name() default "大卫";
}