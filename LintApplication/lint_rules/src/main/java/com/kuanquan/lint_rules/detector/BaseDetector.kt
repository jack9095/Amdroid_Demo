package com.kuanquan.lint_rules.detector

import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector
import com.kuanquan.lint_rules.config.LintConfig

@Suppress("UnstableApiUsage")
open class BaseDetector : Detector() {

    lateinit var lintConfig: LintConfig

    override fun beforeCheckRootProject(context: Context) {
        super.beforeCheckRootProject(context)
        lintConfig = LintConfig.getInstance(context)
    }
}