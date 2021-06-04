package com.kuanquan.lint_rules.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

/**
    Issue：用来声明一个Lint规则。
    Detector：用于检测并报告代码中的Issue，每个Issue都要指定Detector。
    Scanner：用于扫描并发现代码中的Issue，每个Detector可以实现一到多个Scanner。

    https://blog.csdn.net/chenrenxiang/article/details/112381960
 */
@Suppress("UnstableApiUsage")
class LogDetector: Detector(), Detector.UastScanner {

    companion object {
        /**
         *  Issue: 声明一个 lint 的检测规则
            第1个参数为Issue的id；
            第2个参数为Issue的简单描述，可以理解为Issue的标题；
            第3个参数是Issue的详细描述，通常用来提示如何修改该问题；
            第4个参数是Issue的类别，如图中所示，可以分为各种类别；
            第5个参数是Issue的优先级，范围是1到10
            第6个参数表示Issue的严重程度，比如ERROR表示出错，必须修改；WARNING表示提醒，可能存在问题；
            第7个参数用来对应Issue和Detector，每个Issue都要有与之对应的Detector，表示用该Detector来查找该Issue。其中的Scope表示扫描的范围，Scope.JAVA_FILE_SCOPE表示只扫描Java源文件
         */
        val ISSUE: Issue = Issue.create(
            id = "Log_Issue",
            briefDescription = "不要直接使用系统提供的 Log",
            explanation = """
                    请使用 LogUtil 代替 Log
                    """,
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.ERROR,
            implementation = Implementation(LogDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    /**
     * 该方法返回Detector扫描的UAST节点类型
     * UCallExpression.class，表示LogDetector只扫描方法调用，就是当扫描代码的时候，只关心方法调用的语句
     */
    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UCallExpression::class.java)
    }

    /**
     * 需要返回一个自定义的 UElementHandler，用来处理UAST。
     * 这里我们要重写UElementHandler的 visitCallExpression方法，
     * 为什么要重写这个方法呢，因为我们在 getApplicableUastTypes 中指定了该Detector只扫描方法调用。
     * 这两个方法是相互对应的，在 getApplicableUastTypes方法中指定了什么类型，
     * 就需要重写 UElementHandler里面指定的visit方法
     */
    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                /**
                 * node.resolve() 解析掉用的方法
                 * context.evaluator 返回一个evaluator，它可以执行各种解析任务、计算继承查找等
                 *
                 * 找到 解析掉用的方法的类名为 android.util.Log 就报告
                 */
                if(context.evaluator.isMemberInClass(node.resolve(), "android.util.Log")){
                    /**
                     * 报告适用于给定AST节点的问题。AST节点用作检查抑制lint注释的作用域
                     * 参数1：要报告的问题
                     * 参数2：应用错误的AST节点范围。lint基础结构将检查此节点（或其封闭节点）上是否有suppress注释，如果有，则在不涉及客户端的情况下抑制警告
                     * 参数3：问题的位置，如果不知道则为空
                     * 参数4：此警告的消息
                     * 参数5：传递到IDE供快速修复程序使用的可选数据
                     */
                    context.report(
                        ISSUE, node,
                        context.getLocation(node),
                        ISSUE.getExplanation(TextFormat.TEXT))
                }
            }
        }
    }
}