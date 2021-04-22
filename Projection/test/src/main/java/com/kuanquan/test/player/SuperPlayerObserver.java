package com.kuanquan.test.player;

/**
 * 播放器状态回调
 */
public abstract class SuperPlayerObserver {

    /**
     * 开始播放
     * @param name 当前视频名称
     */
    public void onPlayStart(String name) {}

    /**
     * 开始再次播放
     * @param name 当前视频名称
     */
    public void onPlayBegin(String name) {}

    /**
     * 播放暂停
     */
    public void onPlayPause() {}

    /**
     * 播放器停止（）
     */
    public void onPlayStop() {}

    /**
     * 播放器进入Loading状态
     */
    public void onPlayLoading() {}

    /**
     * 播放进度回调
     *
     * @param current  当前播放的时间点 秒
     * @param duration 视频总时长 秒
     */
    public void onPlayProgress(long current, long duration) {}

    public void onSeek(int position) {}

    public void onError(int code, String message) {}

}
