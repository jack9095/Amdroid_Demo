package com.kuanquan.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
public class MyPlugin implements Plugin<Project> {

    void apply(Project project){
        println ("=======================================")
        println("hello gradle plugin")
        println ("=======================================")
    }

}