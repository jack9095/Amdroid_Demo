package com.kuanquan.lintlibrary;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 用于注册要检查的Issue(规则)，只有注册了Issue,该Issue才能被使用
 */
public class Register extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(NamingConventionDetector.ISSUE);
    }
}
