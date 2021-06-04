package com.kuanquan.lint_rules

import com.android.tools.lint.detector.api.*
import com.kuanquan.lint_rules.config.BaseConfigProperty
import org.jetbrains.uast.UCallExpression

/**
 * 获取该表达式的标准名称
 * 例：android.content.ContextWrapper.getSharedPreferences
 */
fun UCallExpression.getQualifiedName(): String {
    return resolve()?.containingClass?.qualifiedName + "." + resolve()?.name
}

/**
 * 扩展报告 ISSUE 的方法
 */
fun Context.reportLint(issue: Issue, location: Location, message: String) {
    this.report(
        issue,
        location,
        message
    )
}

fun Context.reportLint(
    issue: Issue,
    location: Location,
    baseProperty: BaseConfigProperty
) {
    this.report(
        Issue.create(
            issue.id,
            baseProperty.message,
            issue.getExplanation(TextFormat.TEXT),
            issue.category,
            issue.priority,
            baseProperty.lintSeverity,
            issue.implementation
        ),
        location,
        baseProperty.message
    )
}