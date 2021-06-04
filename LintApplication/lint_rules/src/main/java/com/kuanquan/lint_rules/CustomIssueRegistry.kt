package com.kuanquan.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.kuanquan.lint_rules.detector.*

/**
 * Lint规则加载的入口，提供要检查的Issue列表
 */
@Suppress("UnstableApiUsage")
class CustomIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(
            LogDetector.ISSUE,
            KeyWordDetector.ISSUE,
            SerializableClassDetector.ISSUE,
            HandleExceptionDetector.ISSUE,
            ResourceNameDetector.ISSUE,
            AvoidUsageApiDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = 8
}