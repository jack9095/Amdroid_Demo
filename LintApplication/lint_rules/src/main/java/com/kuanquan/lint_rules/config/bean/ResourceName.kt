package com.kuanquan.lint_rules.config.bean

import com.kuanquan.lint_rules.config.BaseConfigProperty


/**
 * 资源命名规范
 */
data class ResourceName(
    val drawable: BaseConfigProperty = BaseConfigProperty(),
    val layout: BaseConfigProperty = BaseConfigProperty()
)