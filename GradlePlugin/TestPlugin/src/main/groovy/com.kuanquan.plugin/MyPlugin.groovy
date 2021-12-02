package com.kuanquan.plugin

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
//        plugin: 'com.android.application'
//        plugin: 'kotlin-android'
        System.out.println ("=======================================")
        System.out.println("hello gradle plugin")
        System.out.println ("=======================================")
    }

}