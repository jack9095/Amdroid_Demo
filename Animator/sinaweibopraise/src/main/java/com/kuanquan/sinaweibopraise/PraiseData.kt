package com.kuanquan.sinaweibopraise

import java.io.Serializable

data class PraiseData(
    /**
     * 表情icon id
     */
    val iconId: String?,
    /**
     * 表情icon地址
     */
    val iconUrl: String?,
    /**
     * 表情描述
     */
    val desc: String?
) : Serializable