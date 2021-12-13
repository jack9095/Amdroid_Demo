package com.wangfei.traceplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * Gradle Plugin
 */
class TracePlugin :  Plugin<Project> {

    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        // 注册自定义的 Transform
        android.registerTransform(LogTransform())
    }

}