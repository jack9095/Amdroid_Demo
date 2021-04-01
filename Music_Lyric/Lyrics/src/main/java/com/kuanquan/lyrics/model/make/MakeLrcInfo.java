package com.kuanquan.lyrics.model.make;

import com.kuanquan.lyrics.model.LyricsLineInfo;

/**
 * 制作歌词信息
 */

public class MakeLrcInfo {

    /**
     * 初始
     */
    public static final int STATUS_NONE = 0;

    /**
     * 完成
     */
    public static final int STATUS_FINISH = 1;
    /**
     * 状态
     */
    private int status = STATUS_NONE;

    /**
     * 歌词索引，-1是未读，-2是已经完成
     */
    private int lrcIndex = -1;
    /**
     * 行歌词
     */
    private LyricsLineInfo lyricsLineInfo;


    /**
     * 重置
     */
    public void reset() {
        status = STATUS_NONE;
        lrcIndex = -1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (this.status != STATUS_FINISH) {
            this.status = status;
        }
    }

    public int getLrcIndex() {
        return lrcIndex;
    }

    public void setLrcIndex(int lrcIndex) {
        this.lrcIndex = lrcIndex;
    }

    public LyricsLineInfo getLyricsLineInfo() {
        return lyricsLineInfo;
    }

    public void setLyricsLineInfo(LyricsLineInfo lyricsLineInfo) {
        this.lyricsLineInfo = lyricsLineInfo;
    }
}
