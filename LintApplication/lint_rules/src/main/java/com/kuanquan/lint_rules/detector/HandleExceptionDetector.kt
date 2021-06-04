package com.kuanquan.lint_rules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiElement
import com.kuanquan.lint_rules.LintMatcher
import com.kuanquan.lint_rules.reportLint
import org.apache.http.util.TextUtils
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UTryExpression
import org.jetbrains.uast.getParentOfType

@Suppress("UnstableApiUsage")
class HandleExceptionDetector : Detector(), Detector.UastScanner {

    companion object {
        private const val REPORT_MESSAGE = "需要加try-catch处理指定类型的异常"
        val ISSUE = Issue.create(
            "HandleExceptionCheck",
            REPORT_MESSAGE,
            REPORT_MESSAGE,
            Category.SECURITY,
            10,
            Severity.ERROR,
            Implementation(HandleExceptionDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableUastTypes(): List<java.lang.Class<out UElement>>? {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {

            override fun visitCallExpression(node: UCallExpression) {
                checkMethod(context, node)
            }

        }
    }

    private fun checkMethod(context: JavaContext, node: UCallExpression) {
        var exception = ""
        if (LintMatcher.matchMethod(node)) { // 核心判断
            exception = "java.lang.IllegalArgumentException"
        }

        if (TextUtils.isEmpty(exception)) {
            return
        }

        val tryExpression: UTryExpression? = node.getParentOfType(UTryExpression::class.java) // 获取try节点
        if (tryExpression == null) {
            context.reportLint(ISSUE, context.getLocation(node), REPORT_MESSAGE)
            return
        }
        for (catch in tryExpression.catchClauses) { // 拿到catch
            for (reference in catch.typeReferences) { // 拿到异常类型
                if (context.evaluator.typeMatches(
                        reference.type,
                        exception
                    )//同一个异常
                    || context.evaluator.extendsClass(
                        context.evaluator.findClass(exception),
                        reference.getQualifiedName()!!,
                        true
                    )//try的是异常的父类
                ) {
                    return
                }
            }
        }
        context.report(ISSUE, context.getLocation(node), REPORT_MESSAGE)
    }

}