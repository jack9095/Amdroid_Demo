package com.kuanquan.videocover.widget

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.kuanquan.videocover.InstagramMediaProcessActivity
import com.kuanquan.videocover.R
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.callback.ProcessStateCallBack
import com.kuanquan.videocover.util.SdkVersionUtils
import kotlinx.coroutines.*

@SuppressLint("ViewConstructor")
class InstagramMediaSingleVideoContainer(
    context: Context, media: LocalMedia, isAspectRatio: Boolean
) : FrameLayout(context), ProcessStateCallBack, LifecycleObserver {

    private var mTopContainer: FrameLayout? = null // 整体布局根 View
    var mCoverView: CoverContainer? = null // 视频封面图选择器
    private var mVideoView: VideoView? = null // 视频播放控件
    private var mThumbView: ImageView? = null // 封面图
    private var mMediaPlayer: MediaPlayer? = null // 系统播放控制器
    private var isStart = false // true 表示可以播放
    private var mCoverPlayPosition = 0 // 记录封面所在的播放点
    private var isPlay = false // true 在播放状态， false 停止播放状态
    private var needPause = false // 需要暂停标识
           
    private var needSeekCover = false
    private var mHandler = Handler(Looper.getMainLooper())
    private var currentPosition = 0 // 当前播放进度
           
    private var seekDuration = 0 // 拖动的播放点
    private var count = 0 // 开始播放到400毫秒就暂停
    private val mainScope = MainScope()
    private var mLifecycleOwner: LifecycleOwner? = null

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            // 获得当前播放时间和当前视频的长度
            currentPosition = mVideoView!!.currentPosition
            Log.e("监听播放进度", "currentPosition ->$currentPosition")
            count++
            if (count >= 10) {
                Log.e("wangfei", "播放暂停")
                startVideo(false)
                mHandler.removeCallbacks(this)
                startVideo(true)
                mVideoView?.seekTo(seekDuration)
            } else {
                mHandler.postDelayed(this, 100)
            }
        }
    }

    init {
        initView(context, media, isAspectRatio)
    }

    fun addLifecycleObserver(lifecycleOwner: LifecycleOwner?){
        mLifecycleOwner = lifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun initView(context: Context, media: LocalMedia, isAspectRatio: Boolean) {
        val rootView = inflate(context, R.layout.aaa_video, this)
        mTopContainer = rootView.findViewById(R.id.root_view)
        mVideoView = rootView.findViewById(R.id.video_view)
        mVideoView?.isVisible = false
        if (SdkVersionUtils.checkedAndroid_Q() && SdkVersionUtils.isContent(media.path)) {
            mVideoView?.setVideoURI(Uri.parse(media.path))
        } else {
            mVideoView?.setVideoPath(media.path)
        }
        mVideoView?.setOnClickListener { startVideo(!isStart) }
        mVideoView?.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            mMediaPlayer = mediaPlayer
            mediaPlayer.isLooping = true
            changeVideoSize(mediaPlayer, isAspectRatio)
            mediaPlayer.setOnInfoListener { _: MediaPlayer?, what: Int, _: Int ->
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    mHandler.postDelayed(runnable, 100)
                    isPlay = true
                    if (needSeekCover && mCoverPlayPosition >= 0) {
                        mVideoView?.seekTo(mCoverPlayPosition)
                        mCoverPlayPosition = -1
                        needSeekCover = false
                    }
                    if (needPause) {
                        needPause = false
                    }
                    return@setOnInfoListener true
                }
                false
            }
        }
        mThumbView = rootView.findViewById(R.id.image_view)
        mCoverView = rootView.findViewById(R.id.cover_container)
        mCoverView?.addLifeCycleObserver(mLifecycleOwner)
        mCoverView?.run {
            getFrame(getContext(), media)
            setOnSeekListener(object : CoverContainer.onSeekListener {
                override fun onSeek(percent: Float, isStart: Boolean) {
                    startVideo(isStart)
                    seekDuration = (media.duration * percent).toInt()
                    Log.e("seekto", "seekDuration -> $seekDuration")
                    Log.e("seekto", "totalDuration -> ${media.duration}")
                    seekDuration += 1000 // 实现拖动的帧与播放器的seekTo对不起的情况
                    if (seekDuration >= media.duration - 2000) {
                        seekDuration = (media.duration - 2000).toInt()
                    }
                    mVideoView?.seekTo(seekDuration)
                }

                override fun onSeekEnd() {
                    needPause = true
                    if (isStart && isPlay) {
                        startVideo(false)
                    }
                }
            })
        }
        startVideo(true)
    }

    /**
     * @param start true 播放，false 暂停
     */
    private fun startVideo(start: Boolean) {
        isStart = start
        mVideoView?.isVisible = true
        if (!start) {
            mVideoView?.pause()
        } else {
            Log.e("wangfei", "开始播放")
            count = 0
            mVideoView?.start()
        }
    }

    override fun onBack(activity: InstagramMediaProcessActivity?) {
        activity?.finish()
    }

    // 最后生成封面的操作
    override fun onProcess(activity: InstagramMediaProcessActivity?) {
        mCoverView?.cropCover()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (mVideoView?.isPlaying == false) {
            mVideoView?.start()
        }
        needPause = true
        needSeekCover = true
        isStart = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mCoverPlayPosition = mVideoView?.currentPosition ?: 0
        if (mVideoView?.isPlaying == true) {
            mVideoView?.stopPlayback()
        }
        isPlay = false
        needPause = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mainScope.cancel()
        mMediaPlayer?.release()
        mMediaPlayer = null
        mVideoView = null
        mHandler.run {
            removeCallbacks(runnable)
            removeCallbacksAndMessages(null)
        }
    }

    private fun changeVideoSize(mediaPlayer: MediaPlayer?, isAspectRatio: Boolean) {
        if (mediaPlayer == null || mVideoView == null) {
            return
        }
        try {
            mediaPlayer.videoWidth
        } catch (e: Exception) {
            return
        }
        val videoWidth = mediaPlayer.videoWidth
        val videoHeight = mediaPlayer.videoHeight
        val parentWidth = measuredWidth
        val parentHeight = measuredWidth
        val instagramAspectRatio: Float = SdkVersionUtils.getInstagramAspectRatio(videoWidth, videoHeight)
        val targetAspectRatio = videoWidth * 1.0f / videoHeight
        val height = (parentWidth / targetAspectRatio).toInt()
        val adjustWidth: Int
        val adjustHeight: Int
        if (isAspectRatio) {
            if (height > parentHeight) {
                adjustWidth =
                    (parentWidth * if (instagramAspectRatio > 0) instagramAspectRatio else targetAspectRatio).toInt()
                adjustHeight = height
            } else {
                if (instagramAspectRatio > 0) {
                    adjustWidth = (parentHeight * targetAspectRatio).toInt()
                    adjustHeight = (parentHeight / instagramAspectRatio).toInt()
                } else {
                    adjustWidth = parentWidth
                    adjustHeight = height
                }
            }
        } else {
            if (height < parentHeight) {
                adjustWidth = (parentHeight * targetAspectRatio).toInt()
                adjustHeight = parentHeight
            } else {
                adjustWidth = parentWidth
                adjustHeight = height
            }
        }
        val layoutParams = mVideoView?.layoutParams as? LayoutParams
        layoutParams?.width = adjustWidth
        layoutParams?.height = adjustHeight
        mVideoView?.layoutParams = layoutParams
    }
}