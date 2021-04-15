package com.chen.videoplayer.player;

import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/6/5.
 *
 * 播放器和控制器耦合接口
 *
 */
public interface VideoPlayerControl {

    void start();         //播放
    void pause();         //暂停
    void seekTo(int pos); //进度条拖动
    void restart();
    void release();

    boolean isIdle();  //是否是空闲
    boolean isError();
    boolean isPreparing();
    boolean isPrepared();
    boolean isBufferingPlaying();
    boolean isBufferingPaused();
    boolean isPlaying();
    boolean isPaused();
    boolean isCompleted();

    int getDuration();
    int getCurrentProgress();
    int getBufferPercent();
    FrameLayout getContainer();

    boolean isFullScreen();  //全屏
    boolean isNormalScreen();//普通窗口
    boolean isTinyScreen();  //小窗口

    void enterFullScreen();    //进入全屏
    boolean exitFullScreen();  //退出全屏
    void enterTinyScreen();    //进入小屏
    boolean exitTinyScreen();  //退出小屏
}
