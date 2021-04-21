package com.kuanquan.test.player;

import com.tencent.rtmp.TXLiveConstants;

/**
 * 超级播放器全局配置类
 */
public class SuperPlayerGlobalConfig {

    private static class Singleton {
        private static SuperPlayerGlobalConfig sInstance = new SuperPlayerGlobalConfig();
    }

    public static SuperPlayerGlobalConfig getInstance() {
        return Singleton.sInstance;
    }

    /**
     * 默认播放填充模式 （ 默认播放模式为 自适应模式 ）
     */
    public int renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;

    /**
     * 播放器最大缓存个数 （ 默认缓存 5 ）
     */
    public int maxCacheItem = 5;

    /**
     * 是否开启硬件加速 （ 默认开启硬件加速 ）
     */
    public boolean enableHWAcceleration = true;

    /**
     * 时移域名 （修改为自己app的时移域名）
     */
    public String playShiftDomain = "liteavapp.timeshift.qcloud.com";
}
