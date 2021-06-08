package com.kuanquan.lint_rules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.kuanquan.lint_rules.LintMatcher
import com.kuanquan.lint_rules.reportLint
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.util.isConstructorCall
import org.jetbrains.uast.util.isMethodCall

/**
 * 避免使用api检测器（目前可以检测方法调用、类创建、实现或者继承）
 */
@Suppress("UnstableApiUsage")
class AvoidUsageApiDetector: BaseDetector(), Detector.UastScanner {

    companion object {
        private const val REPORT_MESSAGE = "避免使用配置的api"
        val ISSUE = Issue.create(
            "AvoidUsageApiCheck",
            REPORT_MESSAGE,
            REPORT_MESSAGE,
            Category.CORRECTNESS,
            10,
            Severity.ERROR,
            Implementation(AvoidUsageApiDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableUastTypes(): List<java.lang.Class<out UElement>>? {
        return listOf(UCallExpression::class.java, UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler(){

            override fun visitCallExpression(node: UCallExpression) {
                if (node.isMethodCall()) {
                    checkMethodCall(context, node)
                } else if (node.isConstructorCall()) {
                    checkConstructorCall(context, node)
                }
            }

            override fun visitClass(node: UClass) {
                checkInheritClass(context, node)
            }
        }
    }

    private fun checkMethodCall(context: JavaContext, node: UCallExpression) {
        lintConfig.avoidUsageApi.method.forEach {
            if (LintMatcher.matchMethod(it, node)) {
                context.reportLint(ISSUE, context.getLocation(node), it)
                return
            }
        }
    }

    private fun checkConstructorCall(context: JavaContext, node: UCallExpression) {
        lintConfig.avoidUsageApi.construction.forEach {
            if (LintMatcher.matchConstruction(it, node)) {
                context.reportLint(ISSUE, context.getLocation(node), it)
                return
            }
        }

    }

    private fun checkInheritClass(context: JavaContext, node: UClass) {
        lintConfig.avoidUsageApi.inherit.forEach { avoidInheritClass ->
            if (LintMatcher.matchInheritClass(avoidInheritClass, node)) {
                context.reportLint(
                    ISSUE,
                    context.getLocation(node as UElement),
                    avoidInheritClass
                )
                return
            }
        }
    }

}