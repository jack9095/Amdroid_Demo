package com.kuanquan.test.projection

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import java.io.Serializable

/**
 * xml 解析框架生成的 bean
 */
@Root(name = "DIDL-Lite", strict = false)
class DIDLLite: Serializable {

    @Element(name = "item", required = false)
    var item: MediaItem? = null

    @Root(name = "item", strict = false)
    class MediaItem: Serializable {
        @Element(name = "class", required = false)
        var objectClass: String? = null

        @Element(name = "title", required = false)
        var title: String? = null

        @Element(name = "creator", required = false)
        var creator: String? = null

        @Element(name = "album", required = false)
        var album: String? = null

        @Element(name = "artist", required = false)
        var artist: String? = null

        @Element(name = "albumArtURI", required = false)
        var albumArtURI: String? = null
    }
}