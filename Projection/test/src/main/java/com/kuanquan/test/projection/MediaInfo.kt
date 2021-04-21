package com.kuanquan.test.projection

import com.kuanquan.test.enum_p.MediaType
import java.io.Serializable

data class MediaInfo(
    /**
     * 投屏过来的类型 一般有视频、音频、图片等等，这里主要是视屏
     */
    var mediaType: MediaType? = null,
    /**
     * 视屏标题
     */
    var title: String? = null,
    /**
     * 视屏播放链接
     */
    var url: String? = null,
    /**
     * 专辑图片链接
     */
    var albumArtURI: String? = null
) : Serializable {

    override fun toString(): String {
        return "MediaInfo(mediaType=$mediaType, title=$title, url=$url, albumArtURI=$albumArtURI)"
    }
}