package com.kuanquan.lint_rules.config.bean

import com.google.gson.annotations.SerializedName
import com.kuanquan.lint_rules.config.BaseConfigProperty

/**
 * User: Rocket
 * Date: 2020/6/17
 * Time: 4:12 PM
 */
data class DependencyApi(
    @SerializedName("trigger_method")
    val triggerMethod: String = "",
    @SerializedName("dependency_method")
    val dependencyMethod: String = ""
) : BaseConfigProperty()