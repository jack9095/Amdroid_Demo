package com.kuanquan.videocover.bean

import java.io.Serializable

data class LocalMedia(
    /**
     * 视频路径
     */
    var path: String?,
    /**
     * 封面图路径
     */
    var coverPath: String?,
    /**
     * 文件名称
     */
    var fileName: String?,
    /**
     * 视频时长 毫秒
     */
    var duration: Long,
    /**
     * 文件大小
     */
    var size: Long = 0): Serializable
