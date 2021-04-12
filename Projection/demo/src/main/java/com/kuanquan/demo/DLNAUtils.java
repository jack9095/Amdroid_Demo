package com.kuanquan.demo;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.kuanquan.demo.enum_p.MediaType;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 获取投屏视频信息的工具类
 */
public class DLNAUtils {

    public static final String BASE_TYPE_VIDEO = "video";
    public final static String OBJECT_CLASS_VIDEO = "object.item.videoItem";

    /**
     * Get media info from url and meta
     * 通过 URL 和 元 获取多媒体数据信息
     * @param url url
     * @param meta meta
     * @return MediaInfo
     */
    public static MediaInfo getMediaInfo(String url, String meta) {
        MediaInfo mediaInfo = getMediaInfoFromMeta(meta);
        if (mediaInfo.mediaType == MediaType.TYPE_UNKNOWN) {
            mediaInfo.mediaType = guessTypeFromURL(url);
        }
        mediaInfo.url = url;
        return mediaInfo;
    }

    /**
     * 通过 meta 获取多媒体数据信息
     * @param meta 元从 native 回掉出来的
     * @return 多媒体信息
     */
    private static MediaInfo getMediaInfoFromMeta(String meta) {
        MediaInfo mediaInfo = new MediaInfo();
        MediaType mediaType = MediaType.TYPE_UNKNOWN;
        if (!TextUtils.isEmpty(meta)) {
            DIDLLite didlLite = null;
            try {
                Serializer serializer = new Persister();
                didlLite = serializer.read(DIDLLite.class, meta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String objectClass;
            DIDLLite.MediaItem objectItem;
            if (didlLite != null && (objectItem = didlLite.item) != null) {
                mediaInfo.title = objectItem.title;
                mediaInfo.albumArtURI = objectItem.albumArtURI;
                if ((objectClass = objectItem.objectClass) != null) {
                    if (objectClass.startsWith(OBJECT_CLASS_VIDEO)) {
                        mediaType = MediaType.TYPE_VIDEO;
                    }
                }
            }
        }
        mediaInfo.mediaType = mediaType;
        return mediaInfo;
    }

    /**
     * 通过解析 url 获取从其他APP 投屏过来的信息
     * @param url 从  native（从其他APP 投屏过来的信息） 回掉过来的数据
     * @return 返回投屏过来的类型
     */
    private static MediaType guessTypeFromURL(String url) {
        MediaType mediaType = MediaType.TYPE_UNKNOWN;
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        if (mime != null) {
            if (mime.startsWith(BASE_TYPE_VIDEO)) {
                mediaType = MediaType.TYPE_VIDEO;
            }
        }
        return mediaType;
    }
}
