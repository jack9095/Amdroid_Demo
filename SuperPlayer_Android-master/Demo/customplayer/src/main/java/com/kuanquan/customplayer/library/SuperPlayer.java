package com.kuanquan.customplayer.library;

import com.tencent.rtmp.ui.TXCloudVideoView;

public interface SuperPlayer {

    /**
     * 开始播放
     *
     * @param url 视频地址
     */
    void play(String url);

    /**
     * 重播
     */
    void reStart();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 销毁播放器
     */
    void destroy();

    /**
     * 切换播放器模式
     *
     * @param playerMode {@link SuperPlayerDef.PlayerMode#WINDOW  }          窗口模式
     *                   {@link SuperPlayerDef.PlayerMode#FULLSCREEN  }      全屏模式
     */
    void switchPlayMode(SuperPlayerDef.PlayerMode playerMode);

    /**
     * 是否开启硬解 true 硬解
     * @param enable
     */
    void enableHardwareDecode(boolean enable);

    void setPlayerView(TXCloudVideoView videoView);

    void seek(int position);

    /**
     * 倍速
     * @param speedLevel
     */
    void setRate(float speedLevel);

    /**
     * 镜像
     * @param isMirror
     */
    void setMirror(boolean isMirror);

    String getPlayURL();

    /**
     * 获取当前播放器模式
     *
     * @return {@link SuperPlayerDef.PlayerMode#WINDOW  }          窗口模式
     * {@link SuperPlayerDef.PlayerMode#FULLSCREEN  }              全屏模式
     */
    SuperPlayerDef.PlayerMode getPlayerMode();

    /**
     * 获取当前播放器状态
     *
     * @return {@link SuperPlayerDef.PlayerState#PLAYING  }     播放中
     * {@link SuperPlayerDef.PlayerState#PAUSE  }               暂停中
     * {@link SuperPlayerDef.PlayerState#LOADING  }             缓冲中
     * {@link SuperPlayerDef.PlayerState#END  }                 结束播放
     */
    SuperPlayerDef.PlayerState getPlayerState();

    /**
     * 设置播放器状态回调
     *
     * @param observer {@link SuperPlayerObserver}
     */
    void setObserver(SuperPlayerObserver observer);
}
