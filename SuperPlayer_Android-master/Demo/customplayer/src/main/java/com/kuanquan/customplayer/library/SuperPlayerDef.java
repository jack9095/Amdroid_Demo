package com.kuanquan.customplayer.library;

public class SuperPlayerDef {

    public enum PlayerMode {
        WINDOW,     // 窗口模式
        FULLSCREEN, // 全屏模式
    }

    public enum PlayerState {
        PLAYING,    // 播放中
        PAUSE,      // 暂停中
        LOADING,    // 缓冲中
        END         // 结束播放
    }

    public enum Orientation {
        LANDSCAPE,  // 横屏
        PORTRAIT    // 竖屏
    }
}
