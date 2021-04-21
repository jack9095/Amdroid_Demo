package com.kuanquan.test.projection

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
        val mediaInfo: MediaInfo? =
            getMediaInfoFromMeta(meta)
        if (mediaInfo?.mediaType === MediaType.TYPE_UNKNOWN) {
            mediaInfo.mediaType =
                guessTypeFromURL(url)
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
        val mediaInfo: MediaInfo =
            MediaInfo()
        var mediaType = MediaType.TYPE_UNKNOWN
        if (!TextUtils.isEmpty(meta)) {
            var didlLite: DIDLLite? = null
            try {
                val serializer: Serializer = Persister()
                didlLite = serializer.read<DIDLLite>(
                    DIDLLite::class.java, meta)
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

    /**
     * 将秒数转换为hh:mm:ss的格式
     *
     * @param second
     * @return
     */
    fun formattedTime(second: Long): String? {
        val formatTime: String
        val h: Long
        val m: Long
        val s: Long
        h = second / 3600
        m = second % 3600 / 60
        s = second % 3600 % 60
        formatTime = if (h == 0L) {
            asTwoDigit(m) + ":" + asTwoDigit(s)
        } else {
            asTwoDigit(h) + ":" + asTwoDigit(m) + ":" + asTwoDigit(s)
        }
        return formatTime
    }

    private fun asTwoDigit(digit: Long): String {
        var value = ""
        if (digit < 10) {
            value = "0"
        }
        value += digit.toString()
        return value
    }

    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    private fun generateTime(time: Long): String? {
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) String.format("%02dh%02dm%02ds", hours, minutes, seconds) else String.format("%02dm%02ds", minutes, seconds)
    }
}