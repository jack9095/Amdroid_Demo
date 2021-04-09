package com.kuanquan.demo;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * xml 解析框架生成的 bean
 */
@Root(name = "DIDL-Lite", strict = false)
public class DIDLLite {

    @Element(name = "item", required = false)
    public MediaItem item;

    @Root(name = "item", strict = false)
    public static class MediaItem {

        @Element(name = "class", required = false)
        public String objectClass;

        @Element(name = "title", required = false)
        public String title;

        @Element(name = "creator", required = false)
        public String creator;

        @Element(name = "album", required = false)
        public String album;

        @Element(name = "artist", required = false)
        public String artist;

        @Element(name = "albumArtURI", required = false)
        public String albumArtURI;
    }
}
