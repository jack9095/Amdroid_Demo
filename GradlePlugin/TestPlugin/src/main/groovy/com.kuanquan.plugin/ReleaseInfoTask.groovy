package com.kuanquan.plugin

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction


/**
 * 更新版本信息的 Task
 * https://juejin.cn/post/6844904135314128903
 */
class ReleaseInfoTask extends DefaultTask {

    ReleaseInfoTask() {
        // 1、在构造器中配置了该 Task 对应的 Task group，即 Task 组，并为其添加上了对应的描述信息。
        group = 'version_manager'
        description = 'release info update'
    }

    // 2、在 gradle 执行阶段执行
    @TaskAction
    void doAction() {
        updateVersionInfo()
    }

    private void updateVersionInfo() {
        // 3、从 realeaseInfo Extension 属性中获取相应的版本信息
        def versionCodeMsg = project.extensions.releaseInfo.versionCode;
        def versionNameMsg = project.extensions.releaseInfo.versionName;
        def versionInfoMsg = project.extensions.releaseInfo.versionInfo;
        System.out.println ("==================versionInfo -> ${versionInfoMsg}=====================")
        def fileName = project.extensions.releaseInfo.fileName;
        def file = project.file(fileName)
        // 4、将实体对象写入到 xml 文件中
        def sw = new StringWriter()
        def xmlBuilder = new MarkupBuilder(sw)

        if (file.text != null && file.text.size() <= 0) {
            //没有内容
            xmlBuilder.releases {
                release {
                    versionCode(versionCodeMsg)
                    versionName(versionNameMsg)
                    versionInfo(versionInfoMsg)
                }
            }
            //直接写入
            file.withWriter { writer ->
                writer.append(sw.toString())
            }
        } else {
            //已有其它版本内容
            xmlBuilder.release {
                versionCode(versionCodeMsg)
                versionName(versionNameMsg)
                versionInfo(versionInfoMsg)
            }
            //插入到最后一行前面
            def lines = file.readLines()
            def lengths = lines.size() - 1
            file.withWriter { writer ->
                lines.eachWithIndex { line, index ->
                    if (index != lengths) {
                        writer.append(line + '\r\n')
                    } else if (index == lengths) {
                        writer.append('\r\r\n' + sw.toString() + '\r\n')
                        writer.append(lines.get(tlengths))
                    }
                }
            }
        }
    }
}