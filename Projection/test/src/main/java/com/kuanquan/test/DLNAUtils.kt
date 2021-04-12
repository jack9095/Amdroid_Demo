package com.kuanquan.test

import android.text.TextUtils
import android.webkit.MimeTypeMap
import com.kuanquan.test.enum_p.MediaType
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister

/**
 * 获取投屏视频信息的工具类
 */
object DLNAUtils {

    private const val BASE_TYPE_VIDEO = "video"
    private const val OBJECT_CLASS_VIDEO = "object.item.videoItem"

    /**
     * Get media info from url and meta
     * 通过 URL 和 元 获取多媒体数据信息
     * @param url url
     * @param meta meta
     * @return MediaInfo
     */
    fun getMediaInfo(url: String?, meta: String?): MediaInfo? {
        val mediaInfo:MediaInfo? = getMediaInfoFromMeta(meta)
        if (mediaInfo?.mediaType === MediaType.TYPE_UNKNOWN) {
            mediaInfo.mediaType = guessTypeFromURL(url)
        }
        mediaInfo?.url = url
        return mediaInfo
    }

    /**
     * 通过 meta 获取多媒体数据信息
     * @param meta 元从 native 回掉出来的
     * @return 多媒体信息
     */
    private fun getMediaInfoFromMeta(meta: String?): MediaInfo? {
        val mediaInfo: MediaInfo = MediaInfo()
        var mediaType = MediaType.TYPE_UNKNOWN
        if (!TextUtils.isEmpty(meta)) {
            var didlLite: DIDLLite? = null
            try {
                val serializer: Serializer = Persister()
                didlLite = serializer.read<DIDLLite>(DIDLLite::class.java, meta)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            var objectClass: String? = null
            var objectItem: DIDLLite.MediaItem? = null
            if (didlLite != null && didlLite.item.also { objectItem = it } != null) {
                mediaInfo.title = objectItem?.title
                mediaInfo.albumArtURI = objectItem?.albumArtURI
                if (objectItem?.objectClass.also { objectClass = it } != null) {
                    if (objectClass?.startsWith(OBJECT_CLASS_VIDEO) == true) {
                        mediaType = MediaType.TYPE_VIDEO
                    }
                }
            }
        }
        mediaInfo.mediaType = mediaType
        return mediaInfo
    }

    /**
     * 通过解析 url 获取从其他APP 投屏过来的信息
     * @param url 从  native（从其他APP 投屏过来的信息） 回掉过来的数据
     * @return 返回投屏过来的类型
     */
    private fun guessTypeFromURL(url: String?): MediaType? {
        var mediaType = MediaType.TYPE_UNKNOWN
        val ext = MimeTypeMap.getFileExtensionFromUrl(url)
        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        if (mime?.startsWith(BASE_TYPE_VIDEO) == true) {
            mediaType = MediaType.TYPE_VIDEO
        }
        return mediaType
    }
}