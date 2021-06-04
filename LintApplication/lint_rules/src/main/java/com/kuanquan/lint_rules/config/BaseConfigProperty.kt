package com.kuanquan.lint_rules.config

import com.android.tools.lint.detector.api.Severity
import java.io.Serializable

/**
 * lint配置基础属性
 */
open class BaseConfigProperty(
    val name: String = "",
    val name_regex: String = "",
    val message: String = "",
    val exclude: List<String> = listOf(),
    val exclude_regex: String = "",
    private val severity: String? = "error"
) : Serializable {
    val lintSeverity
        get() =
            when (severity) {
                "fatal" -> Severity.FATAL
                "error" -> Severity.ERROR
                "warning" -> Severity.WARNING
                "informational" -> Severity.INFORMATIONAL
                "ignore" -> Severity.IGNORE
                else -> Severity.ERROR
            }

}