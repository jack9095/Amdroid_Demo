package com.kuanquan.lint_rules.detector

import com.android.SdkConstants
import com.android.resources.ResourceFolderType
import com.android.tools.lint.detector.api.*
import com.kuanquan.lint_rules.LintMatcher
import com.kuanquan.lint_rules.reportLint

/**
 * 这个规则目前在 Android Studio 中不能实时提示，得执行 ./gradlew lint 才能看到
 */
@Suppress("UnstableApiUsage")
class ResourceNameDetector: Detector(), Detector.XmlScanner {

    companion object {
        private const val REPORT_MESSAGE = "资源命名请按规定的配置的规则"
        private const val drawable = "^(bg|shape)_"
        private const val layout = "^(activity|dialog|item|view|page)_"

        val ISSUE = Issue.create(
            "ResourceNameCheck",
            REPORT_MESSAGE,
            REPORT_MESSAGE,
            Category.CORRECTNESS,
            10,
            Severity.WARNING,
            Implementation(ResourceNameDetector::class.java, Scope.RESOURCE_FILE_SCOPE)
        )
    }

    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.TAG_RESOURCES)
    }

    override fun beforeCheckFile(context: Context) {
        if (context !is XmlContext) {
            return
        }

        val fileName = getBaseName(context.file.name)
        val resourceName = when (context.resourceFolderType) {
            ResourceFolderType.DRAWABLE -> {
                if (!LintMatcher.matchFileName(drawable,fileName)) {
                    context.reportLint(ISSUE, Location.create(context.file), "drawable命名不符合 (bg|shape)_ 规则")
                } else {}
            }
            ResourceFolderType.LAYOUT -> {
                if (!LintMatcher.matchFileName(layout,fileName)) {
                    context.reportLint(ISSUE, Location.create(context.file), "layout命名不符合 (activity|dialog|item|view|page)_ 规则")
                } else {}
            }
            else -> null
        } ?: return
    }
}