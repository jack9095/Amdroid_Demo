package com.kuanquan.lint_rules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.evaluateString

/**
 * 检测是否使用了 某个（比如 lint ） 关键字
 */
@Suppress("UnstableApiUsage")
class KeyWordDetector: Detector(), Detector.UastScanner {

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "ShortUniqueId",
            briefDescription = "Lint 方法",
            explanation = " 此检查突出显示代码中提到单词“lint”的字符串文字.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(KeyWordDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement?>> {
        return listOf(ULiteralExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitLiteralExpression(node: ULiteralExpression) {
                val string = node.evaluateString() ?: return
                if (string.contains("lint") && string.matches(Regex(".*\\blint\\b.*"))) {
                    context.report(
                        ISSUE, node, context.getLocation(node),
                        "这段代码提到了 lint 关键字**"
                    )
                }
            }
        }
    }

}