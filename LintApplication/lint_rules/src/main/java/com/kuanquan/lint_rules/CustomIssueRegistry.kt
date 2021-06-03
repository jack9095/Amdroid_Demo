package com.kuanquan.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

/**
 * Lint规则加载的入口，提供要检查的Issue列表
 */
@Suppress("UnstableApiUsage")
class CustomIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(LogDetector.ISSUE, KeyWordDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = 8
}