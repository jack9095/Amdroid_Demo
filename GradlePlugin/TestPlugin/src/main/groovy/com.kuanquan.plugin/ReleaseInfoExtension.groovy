package com.kuanquan.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * 负责 Release 版本管理的扩展属性区域
 * 自定义 Extension
 */
class ReleaseInfoExtension {
    String versionName;
    String versionCode;
    String versionInfo;
    String fileName;
}