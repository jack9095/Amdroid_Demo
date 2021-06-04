package com.kuanquan.lint_rules.detector

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiClassType
import com.kuanquan.lint_rules.LintMatcher
import com.kuanquan.lint_rules.reportLint
import org.jetbrains.uast.UClass

/**
 * 实现了 Serializable 接口的类，引用类型成员变量也必须要实现 Serializable 接口
 */
@Suppress("UnstableApiUsage")
class SerializableClassDetector: Detector(), Detector.UastScanner {

    companion object {
        const val CLASS_SERIALIZABLE = "java.io.Serializable"
        private const val REPORT_MESSAGE = "该对象必须要实现Serializable接口，因为外部类实现了Serializable接口"
        val ISSUE = Issue.create(
            "SerializableClassCheck",
            REPORT_MESSAGE,
            REPORT_MESSAGE,
            Category.CORRECTNESS,
            10,
            Severity.WARNING,
            Implementation(SerializableClassDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    /**
     * 返回此检测器关心的超类的完全限定名列表。
     * 如果不为 null，则仅当当前类是指定超类之一的子类时，**才**调用此检测器。
     * Lint 在遇到这些类型的子类和lambda时会调用[.visitClass]（有时还会调用[.visitClass]）。
     *
     * @return 完全限定名的列表
     */
    override fun applicableSuperClasses(): List<String>? {
        return listOf(CLASS_SERIALIZABLE)
    }

    /**
     * 为扩展使用[.ApplicatableSuperclases]指定的超级类之一的每个类调用
     */
    override fun visitClass(context: JavaContext, declaration: UClass) {
        for (field in declaration.fields) {
            // 字段是引用类型，并且可以拿到该 class
            val psiClass = (field.type as? PsiClassType)?.resolve() ?: continue
            if (!LintMatcher.matchClass(psiClass)) {
                return
            }
            if (!context.evaluator.implementsInterface(psiClass, CLASS_SERIALIZABLE, true)) {
                context.reportLint(
                    ISSUE,
                    context.getLocation(field.typeReference!!),
                    "该对象必须要实现Serializable接口，因为外部类实现了Serializable接口"
                )
            }
        }
    }
}