package com.kuanquan.videocover.widget

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.VideoView
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.callback.LifecycleCallBack
import com.kuanquan.videocover.callback.ProcessStateCallBack

class InstagramMediaSingleVideoContainer: FrameLayout, ProcessStateCallBack, LifecycleCallBack {

    private val mTopContainer // 整体布局根 View
            : FrameLayout? = null
    var mCoverView // 视频封面图选择器
            : CoverContainer? = null
    private val mVideoView // 视频播放控件
            : VideoView? = null
    private val mThumbView // 封面图
            : ImageView? = null
    private val mMediaPlayer // 系统播放控制器
            : MediaPlayer? = null
    private val mMedia // 视频及封面图实体类
            : LocalMedia? = null
    private val isStart // true 表示可以播放
            = false
    private val isFirst // 是不是第一次进来
            = false
    private val mCoverPlayPosition // 记录封面所在的播放点
            = 0
    private val isPlay // true 在播放状态， false 停止播放状态
            = false
    private val needPause // 需要暂停标识
            = false
    private val needSeekCover = false
    private val handler = Handler(Looper.getMainLooper())
    var currentPosition // 当前播放进度
            = 0
    private val seekDuration // 拖动的播放点
            = 0

    constructor(context: Context, media: LocalMedia, isAspectRatio: Boolean){

    }
}