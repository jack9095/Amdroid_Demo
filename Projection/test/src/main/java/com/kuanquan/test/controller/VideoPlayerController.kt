package com.kuanquan.test.controller

import android.content.Context
import android.os.*
import android.util.AttributeSet
import android.view.*
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.kuanquan.test.R

class VideoPlayerController: FrameLayout, View.OnClickListener, OnSeekBarChangeListener {

    constructor(context: Context) : super(context){
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        initView(context)
    }

    private var mCenterStart: ImageView? = null // 中间播放按钮
    private var mTop: LinearLayout? = null // 顶部布局
    private var mBack: ImageView? = null // 顶部布局左上角返回按键
    private var mTitle: TextView? = null // 视频标题
    private var mBottom: LinearLayout? = null // 底部布局
    private var mRestartPause: ImageView? = null // 底部布局 左下角 播放、暂停控件
    private var mPosition: TextView? = null // 当前播放时间点
    private var mDuration: TextView? = null // 视频总时长
    private var mSeek: SeekBar? = null // 视频播放进度拖动条
    private var mFullScreen: ImageView? = null // 横竖屏切换控件
    private var mLoading: LinearLayout? = null // 加载视频的布局
    private var mLoadText: TextView? = null // 加载视频布局的文案
    private var mError: LinearLayout? = null // 视频加载错误的布局
    private var mRetry: TextView? = null // 点击重试
    private var mCompleted: LinearLayout? = null // 视频播放完成现实的布局
    private var mReplay: TextView? = null // 重新播放
    private var mShare: TextView? = null // 分享

    private val screenWidth: Int by lazy { context.resources.displayMetrics.widthPixels }

    private val screenHeight: Int by lazy { context.resources.displayMetrics.heightPixels }

    private var topBottomVisible = false // 播放器底部和顶部状态栏隐藏和显示

    private val MSG_ID = 0x01 // handler 发送消息的id

    // 用来更新进度的 handler
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            updateProgress()
            sendEmptyMessageDelayed(MSG_ID, 300)
        }
    }

    /**
     * 更新进度，在视频准备就绪及拖动进度条的时候掉用
     */
    private fun startUpdateProgress() {
        handler.sendEmptyMessageDelayed(MSG_ID, 300)
    }

    /**
     * 取消更新，在开始拖动进度的时候掉用
     */
    private fun cancelUpdateProgress() {
        handler.removeMessages(MSG_ID)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.video_palyer_controller, this)

        mCenterStart = findViewById(R.id.center_start)

        mTop = findViewById(R.id.top)
        mBack = findViewById(R.id.back)
        mTitle = findViewById(R.id.title)

        mBottom = findViewById(R.id.bottom)
        mRestartPause = findViewById(R.id.restart_or_pause)
        mPosition = findViewById(R.id.position)
        mDuration = findViewById(R.id.duration)
        mSeek = findViewById(R.id.seek)
        mFullScreen = findViewById(R.id.full_screen)

        mLoading = findViewById(R.id.loading)
        mLoadText = findViewById(R.id.load_text)

        mError = findViewById(R.id.error)
        mRetry = findViewById(R.id.retry)

        mCompleted = findViewById(R.id.completed)
        mReplay = findViewById(R.id.replay)
        mShare = findViewById(R.id.share)

        mCenterStart?.setOnClickListener(this)
        setOnClickListener(this)
        mReplay?.setOnClickListener(this)
        mSeek?.setOnSeekBarChangeListener(this)
        mRestartPause?.setOnClickListener(this)
        mFullScreen?.setOnClickListener(this)
        mBack?.setOnClickListener(this)
    }

    private fun updateProgress() {
//        val duration: Int = mVideoPlayerControl.getDuration()
//        val currentPosition: Int = mVideoPlayerControl.getCurrentProgress()
//        mSeek?.secondaryProgress = mVideoPlayerControl.getBufferPercent()
//        mSeek?.progress = (currentPosition * 1.0f / duration * 100).toInt()
//        mPosition.setText(VideoPlayerUtil.formatTime(currentPosition))
//        mDuration.setText(VideoPlayerUtil.formatTime(duration))
    }

    fun setTitle(title: String?) {
        mTitle?.text = title
    }

    override fun onClick(v: View?) {
        when(v) {
            mCenterStart -> {

            }
            this -> { // 点击隐藏和显示顶部和底部导航栏用的
//            if (mVideoPlayerControl.isPlaying()
//                    || mVideoPlayerControl.isPaused()
//                    || mVideoPlayerControl.isBufferingPlaying()
//                    || mVideoPlayerControl.isBufferingPaused()) {
                setTopBottomVisible(!topBottomVisible)
//            }
            }
            mReplay -> { // 重新播放
                mCompleted?.visibility = View.GONE
            }
            mShare -> {
                Toast.makeText(context, "分享", Toast.LENGTH_SHORT).show()
            }
            mRestartPause -> {  // 暂停或者播放

            }
            mFullScreen -> { // 横竖屏切换控件
//            if (mVideoPlayerControl.isNormalScreen()) {
//                mVideoPlayerControl.enterFullScreen()
//            } else if (mVideoPlayerControl.isFullScreen()) {
//                mVideoPlayerControl.exitFullScreen()
//            }
            }
            mBack -> {
//            if (mVideoPlayerControl.isFullScreen()) {
//                mVideoPlayerControl.exitFullScreen()
//            }
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        cancelUpdateProgress()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
//        val progress = (mVideoPlayerControl.getDuration() * seekBar!!.progress / 100f) as Int
//        mVideoPlayerControl.seekTo(progress)
        startUpdateProgress()
    }

    fun reset() {
        mError?.visibility = View.GONE
        mLoading?.visibility = View.GONE
        mCompleted?.visibility = View.GONE
    }

    /**
     * 设置头部和底部布局是否隐藏
     */
    private fun setTopBottomVisible(topBottomVisible: Boolean) {
        mTop?.visibility = if (topBottomVisible) View.VISIBLE else View.INVISIBLE
        mBottom?.visibility = if (topBottomVisible) View.VISIBLE else View.INVISIBLE
        this.topBottomVisible = topBottomVisible

        // 3秒显示还没有关闭就自动关闭
        if (topBottomVisible) {
            handler.postDelayed({
                mTop?.visibility = View.INVISIBLE
                mBottom?.visibility = View.INVISIBLE
                this.topBottomVisible = false
            }, 3000)
        }
    }

   fun destroy(){
        handler.removeCallbacksAndMessages(null)
    }
}