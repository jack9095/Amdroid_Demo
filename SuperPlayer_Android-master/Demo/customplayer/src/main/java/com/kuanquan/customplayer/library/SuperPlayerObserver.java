package com.kuanquan.customplayer.library;

import com.tencent.rtmp.TXLivePlayer;

/**
 * 播放器状态回调
 */
public abstract class SuperPlayerObserver {

    /**
     * 开始播放
     * @param name 当前视频名称
     */
    public void onPlayBegin(String name) {}

    /**
     * 播放暂停
     */
    public void onPlayPause() {}

    /**
     * 播放器停止
     */
    public void onPlayStop() {}

    /**
     * 播放器进入Loading状态
     */
    public void onPlayLoading() {}

    /**
     * 播放进度回调
     *
     * @param current
     * @param duration
     */
    public void onPlayProgress(long current, long duration) {}

    public void onSeek(int position) {}

    public void onError(int code, String message) {}

    public void onPlayTimeShiftLive(TXLivePlayer player, String url) {}

}
