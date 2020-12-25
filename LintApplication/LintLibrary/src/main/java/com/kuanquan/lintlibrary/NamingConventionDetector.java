package com.kuanquan.lintlibrary;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * 自定义 Lint 规则
 * https://juejin.cn/post/6844903895429464078
 */
public class NamingConventionDetector extends Detector implements Detector.UastScanner{

    public static final Issue ISSUE = Issue.create(
            "NamingConventionWarning",
            "命名规范错误",
            "使用驼峰命名法，方法命名开头小写",
            Category.USABILITY,
            5,
            Severity.WARNING,
            new Implementation(NamingConventionDetector.class, EnumSet.of(Scope.JAVA_FILE))
    );

    // 返回我们所有感兴趣的类，即返回的类都被会检查
    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.<Class<? extends UElement>>singletonList(UClass.class);
    }

    /**
     * 重写该方法，创建自己的处理器
     * @param context
     * @return
     */
    @Nullable
    @Override
    public UElementHandler createUastHandler(@NotNull JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(@NotNull UClass node) {
                node.accept(new NamingConventionVisitor(context, node));
            }
        };
    }
}
