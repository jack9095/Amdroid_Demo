package com.kuanquan.lint_rules

import com.intellij.psi.PsiClass
import com.kuanquan.lint_rules.config.BaseConfigProperty
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.getQualifiedName
import java.util.regex.Pattern

object LintMatcher {

    /**
     * 匹配方法
     */
    fun matchMethod(node: UCallExpression): Boolean {
        return match(
            "android.graphics.Color.parseColor",
            "",
            node.getQualifiedName(),
            node.getContainingUClass()?.qualifiedName,
           null,
            ""
        )
    }

    /**
     * 匹配方法
     */
    fun matchMethod(
        baseConfig: BaseConfigProperty,
        node: UCallExpression
    ): Boolean {
        return match(
            baseConfig.name,
            baseConfig.name_regex,
            node.getQualifiedName(),
            node.getContainingUClass()?.qualifiedName,
            baseConfig.exclude,
            baseConfig.exclude_regex
        )
    }

    /**
     * 匹配构造方法
     */
    fun matchConstruction(
        baseConfig: BaseConfigProperty,
        node: UCallExpression
    ): Boolean {
        return match(
            baseConfig.name,
            baseConfig.name_regex,
            //不要使用node.resolve()获取构造方法，在没定义构造方法使用默认构造的时候返回值为null
            node.classReference.getQualifiedName(),
            node.getContainingUClass()?.qualifiedName,
            baseConfig.exclude,
            baseConfig.exclude_regex
        )
    }

    /**
     * 匹配继承或实现类
     */
    fun matchInheritClass(
        baseConfig: BaseConfigProperty,
        node: UClass
    ): Boolean {
        node.supers.forEach {
            if (match(
                    baseConfig.name,
                    baseConfig.name_regex,
                    it.qualifiedName,
                    node.qualifiedName,
                    baseConfig.exclude,
                    baseConfig.exclude_regex
                )
            ) return true
        }
        return false
    }

    /**
     * 匹配文件名
     */
    fun matchFileName(rule: String,fileName: String) = match(
        "",
        rule,
        fileName
    )

    /**
     *  匹配类
     */
    fun matchClass(node: PsiClass): Boolean {
        return match(
            "",
            "^(com\\.kuanquan\\.lint_rules|com\\.kuanquan\\.lintapplication)",
            node.qualifiedName,
            node.containingClass?.qualifiedName
        )
    }

    /**
     * @param name 是完全匹配，
     * @param nameRegex 是正则匹配，匹配优先级上name > nameRegex   TODO  这个需要和自己的项目的包名匹配
     * @param qualifiedName
     * @param inClassName 是当前需要匹配的方法所在类
     * @param exclude 是要排除匹配的类（目前以类的粒度去排除）
     * @param excludeRegex
     */
    fun match(name: String?, nameRegex: String?, qualifiedName: String?, inClassName: String? = null,
        exclude: List<String>? = null, excludeRegex: String? = null ): Boolean {

        qualifiedName ?: return false

        //排除
        if (inClassName != null && inClassName.isNotEmpty()) {
            if (exclude != null && exclude.contains(inClassName)) return false

            if (excludeRegex != null &&
                excludeRegex.isNotEmpty() &&
                Pattern.compile(excludeRegex).matcher(inClassName).find()
            ) {
                return false
            }
        }

        if (name != null && name.isNotEmpty() && name == qualifiedName) { // 优先匹配 name
            return true
        }

        // 在匹配nameRegex
        if (nameRegex != null && nameRegex.isNotEmpty() && Pattern.compile(nameRegex).matcher(qualifiedName).find()) {
            return true
        }
        return false
    }
}