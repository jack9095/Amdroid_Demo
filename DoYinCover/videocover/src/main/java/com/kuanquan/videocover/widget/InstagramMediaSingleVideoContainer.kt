package com.kuanquan.videocover.widget

import android.animation.ObjectAnimator
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
import com.kuanquan.videocover.util.GetFrameBitmap
import com.kuanquan.videocover.util.SdkVersionUtils
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.CountDownLatch

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
    private var autoPlay = false // 循环自动播放
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
                autoPlay = true
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
//                        mVideoView.pause()
                        needPause = false
                    }
                    if (mThumbView?.visibility == VISIBLE && !autoPlay) {
//                        ObjectAnimator.ofFloat(mThumbView, "alpha", 1.0f, 0f).setDuration(400)
//                            .start()
                        mThumbView?.isVisible = false
                    }
                    return@setOnInfoListener true
                }
                false
            }
        }
        mThumbView = rootView.findViewById(R.id.image_view)
        mCoverView = CoverContainer(context, media).apply {
            addLifeCycleObserver(mLifecycleOwner)
        }
        addView(mCoverView)
        mCoverView?.run {
            getFrame(getContext(), media)
            setOnSeekListener(object : CoverContainer.onSeekListener {
                override fun onSeek(percent: Float, isStart: Boolean) {
                    if (isStart) {
                        startVideo(true)
                    }
                    seekDuration = (media.duration * percent).toInt()
                    mVideoView?.seekTo(seekDuration)
                }

                override fun onSeekEnd() {
                    needPause = true
                    autoPlay = false
                    if (isStart && isPlay) {
                        startVideo(false)
                    }
                }
            })
        }

        mainScope.launch {
            val getFrameBitmap = GetFrameBitmap()
            val job = async(Dispatchers.IO) {
                getFrameBitmap.setParams(context, media, isAspectRatio, 0)
                getFrameBitmap.doInBackground()
            }
            val await = job.await()
            await?.let { mThumbView?.setImageBitmap(it) }
        }
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        mTopContainer?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        )
        mCoverView?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height - width, MeasureSpec.EXACTLY)
        )
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
        var viewTop = 0
        val viewLeft = 0
        mTopContainer?.layout(
            viewLeft,
            viewTop,
            viewLeft + mTopContainer!!.measuredWidth,
            viewTop + mTopContainer!!.measuredHeight
        )
        viewTop = mTopContainer?.measuredHeight ?: 0
        mCoverView?.layout(
            viewLeft,
            viewTop,
            viewLeft + mCoverView!!.measuredWidth,
            viewTop + mCoverView!!.measuredHeight
        )
    }

    override fun onBack(activity: InstagramMediaProcessActivity?) {
        activity?.finish()
    }

    override fun onProcess(activity: InstagramMediaProcessActivity?) {
        var c = 1
        c++
        val count = CountDownLatch(c)
        mCoverView?.cropCover(count)
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