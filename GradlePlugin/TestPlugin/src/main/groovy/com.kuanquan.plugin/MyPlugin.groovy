package com.kuanquan.plugin

import groovyjarjarasm.asm.Handle
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 * https://blog.csdn.net/huachao1001/article/details/51810328
 */
class MyPlugin implements Plugin<Project> {

    void apply(Project project){
        project.task() {
            doLast {
            }
        }
//        project("app") {
//            apply plugin: 'com.android.application'
//            apply plugin: 'kotlin-android'
//        }
        System.out.println ("=======================================")
        System.out.println("hello gradle plugin")
        System.out.println ("=======================================")

        /**
         * 在 project.extensions.create 方法的内部其实质是 通过
         * project.extensions.create() 方法来获取在 releaseInfo
         * 闭包中定义的内容并通过反射将闭包的内容转换成一个 ReleaseInfoExtension 对象
         *
         */
        // 创建用于设置版本信息的扩展属性
        ReleaseInfoExtension releaseInfoExtension = project.extensions.create("releaseInfo", ReleaseInfoExtension.class)

        // 创建用于更新版本信息的 task
        project.tasks.create("releaseInfoTask", ReleaseInfoTask.class)
    }

}