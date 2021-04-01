package com.kuanquan.lyrics.model.make;


/**
 * 制作额外歌词行数据
 */

public class MakeExtraLrcLineInfo extends MakeLrcInfo {
    /**
     * 该行额外歌词
     */
    private String mExtraLineLyrics;

    public String getExtraLineLyrics() {
        return mExtraLineLyrics;
    }

    public void setExtraLineLyrics(String mExtraLineLyrics) {
        this.mExtraLineLyrics = mExtraLineLyrics;
    }

    @Override
    public void reset() {
        super.reset();
        mExtraLineLyrics = "";
    }
}
