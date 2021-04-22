package com.kuanquan.test.controller

import android.content.Context
import android.content.pm.ActivityInfo
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.kuanquan.test.R
import com.kuanquan.test.player.SuperPlayer
import com.kuanquan.test.player.SuperPlayerDef
import com.kuanquan.test.player.SuperPlayerImpl
import com.kuanquan.test.player.SuperPlayerObserver
import com.kuanquan.test.projection.DLNAUtils
import com.kuanquan.test.seek.VerticalSeekBar
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * 播放器界面上的控制面板布局
 */
class VideoPlayerController : FrameLayout, View.OnClickListener, OnSeekBarChangeListener, LifecycleObserver {

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    private var mCenterStart: ImageView? = null // 中间播放按钮
    private var verticalSeekBar: VerticalSeekBar? = null // 垂直进度条，调整声音
    private var mTop: LinearLayout? = null // 顶部布局
    private var mBack: ImageView? = null // 顶部布局左上角返回按键
    private var mTitle: TextView? = null // 视频标题
    private var mBottom: LinearLayout? = null // 底部布局
    private var mRestartPause: ImageView? = null // 底部布局 左下角 播放、暂停控件
    private var mPositionTv: TextView? = null // 当前播放时间点
    private var mDurationTv: TextView? = null // 视频总时长
    private var mSeekBar: SeekBar? = null // 视频播放进度拖动条
    private var voiceIv: ImageView? = null // 播放器音量调节按钮
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

    /**
     * 把自定义 View 的生命周期和所在页面的 Activity 或 Fragment 的生命周期关联起来
     */
    fun addLifecycleObserver(lifecycleOwner: LifecycleOwner?){
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.video_palyer_controller, this)

        verticalSeekBar = findViewById(R.id.vertical_Seekbar)
        initVerticalSeekBar(verticalSeekBar)
        mCenterStart = findViewById(R.id.center_start)

        mTop = findViewById(R.id.top)
        mBack = findViewById(R.id.back)
        mTitle = findViewById(R.id.title)

        mBottom = findViewById(R.id.bottom)
        mRestartPause = findViewById(R.id.restart_or_pause)
        mPositionTv = findViewById(R.id.position)
        mDurationTv = findViewById(R.id.duration)
        mSeekBar = findViewById(R.id.seek)
        voiceIv = findViewById(R.id.voice_iv)
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
        mRestartPause?.setOnClickListener(this)
        mFullScreen?.setOnClickListener(this)
        mBack?.setOnClickListener(this)
        mShare?.setOnClickListener(this)

        mSeekBar?.setOnSeekBarChangeListener(this)
    }

    private fun initVerticalSeekBar(verticalSeekBar: VerticalSeekBar?) {
        verticalSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                handler.removeCallbacksAndMessages(null)
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mSuperPlayer?.setAudioPlayoutVolume(progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                setTopBottomVisible(true)
            }
        })
    }

    private var videoUrl: String? = null
    private var topBottomVisible = false // 播放器底部和顶部状态栏隐藏和显示
    private var mSuperPlayer: SuperPlayer? = null // 播放器常用操作接口类

    /**
     * 设置腾讯云播放器
     */
    fun semTXCloudVideoView(tXCloudVideoView: TXCloudVideoView) {
        setPlayer(SuperPlayerImpl(context, tXCloudVideoView))
    }

    /**
     * 设置播放器回掉
     */
    private fun setPlayer(player: SuperPlayer) {
        mSuperPlayer = player
        mSuperPlayer?.setObserver(mSuperPlayerObserver)
    }

    /**
     * 设置播放URL
     */
    fun setPlayUrl(url: String) {
        videoUrl = url
    }

    fun setTitle(title: String?) {
        mTitle?.text = title
    }

    /**
     * 恢复播放
     */
    fun resume(){
        mSuperPlayer?.resume()
    }

    /**
     * 开始播放
     */
    fun startPlayer(){
        mSuperPlayer?.play(videoUrl)
    }

    /**
     * 暂停播放
     */
    fun pausePlayer(){
        mSuperPlayer?.pause()
    }

    /**
     * 停止播放
     */
    fun stopPlayer(){
        mSuperPlayer?.stop()
    }

    /**
     * 硬解
     */
    fun enableHardwareDecode(){
        mSuperPlayer?.enableHardwareDecode(true)
    }

    /**
     * 隐藏播放器中间的播放按钮
     */
   fun hideView(){
       mCenterStart?.visibility = View.GONE
    }

    /**
     * 指定播放的位置
     */
    fun setSeekJump(jDuration: Long){
        jumpDuration = jDuration
    }

    /**
     * 指定播放的位置
     */
    fun setSeek(jDuration: Long){
        mSuperPlayer?.seek(jDuration.toInt())
    }

    private var totalDuration = 0L // 视屏总时长
    private var currentDuration = 0L // 当前播放的时间点
    private var jumpDuration = 0L // 跳转到指定地方播放

    /**
     * 播放器状态回掉
     */
    private val mSuperPlayerObserver = object : SuperPlayerObserver() {
        override fun onPlayProgress(current: Long, duration: Long) {
            super.onPlayProgress(current, duration)
            totalDuration = duration
            currentDuration = current
            mPositionTv?.text = DLNAUtils.formattedTime(current)
            mDurationTv?.text = DLNAUtils.formattedTime(duration)

            if (totalDuration > 0) {
                mSeekBar?.progress = (current * 100 / totalDuration).toInt()
            }
        }

        override fun onPlayStart(name: String?) {
            super.onPlayStart(name)
            Log.e("VideoPlayerController", "onPlayStart -》 onPlayStart ")
            if (jumpDuration > 0) {
                mSuperPlayer?.seek(jumpDuration.toInt())
                mSuperPlayer?.resume()
            }
            mCompleted?.visibility = View.GONE
            mRestartPause?.setImageResource(R.drawable.ic_player_pause)
        }

        override fun onPlayBegin(name: String?) {
            super.onPlayBegin(name)
            Log.e("VideoPlayerController", "onPlayBegin -》 onPlayBegin ")
            mCompleted?.visibility = View.GONE
            mRestartPause?.setImageResource(R.drawable.ic_player_pause)
        }

        override fun onPlayPause() {
            super.onPlayPause()
            mRestartPause?.setImageResource(R.drawable.ic_player_start)
        }

        override fun onPlayStop() {
            super.onPlayStop()
            mCompleted?.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            this -> { // 点击隐藏和显示顶部和底部导航栏用的
                if (mSuperPlayer?.playerState == SuperPlayerDef.PlayerState.PLAYING || mSuperPlayer?.playerState == SuperPlayerDef.PlayerState.PAUSE) {
                    setTopBottomVisible(!topBottomVisible)
                }
            }
            mCenterStart -> { // 播放器中间的播放按钮
                mSuperPlayer?.play(videoUrl)
                mCenterStart?.visibility = View.GONE
            }
            mReplay -> { // 重新播放
                mCompleted?.visibility = View.GONE
                mSuperPlayer?.reStart()
            }
            mShare -> {
                Toast.makeText(context, "分享", Toast.LENGTH_SHORT).show()
            }
            mRestartPause -> {  // 暂停或者播放
                if (mSuperPlayer?.playerState == SuperPlayerDef.PlayerState.PLAYING) {
                    mSuperPlayer?.pause()
                } else if (mSuperPlayer?.playerState == SuperPlayerDef.PlayerState.PAUSE) {
                    mSuperPlayer?.resume()
                }
            }
            mBack -> {
                mPageFinishListener?.onScreenIcon(currentDuration)
            }
            mFullScreen -> {
                mPageFinishListener?.onScreenIcon(0)
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        handler.removeCallbacksAndMessages(null)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        setTopBottomVisible(true)
        val currentTime = seekBar.progress * totalDuration / 100
        mSuperPlayer?.seek(currentTime.toInt())
        mSuperPlayer?.resume()
    }

    private fun reset() {
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
        verticalSeekBar?.visibility = if (topBottomVisible) View.VISIBLE else View.INVISIBLE
        this.topBottomVisible = topBottomVisible

        // 8秒显示还没有关闭就自动关闭
        if (topBottomVisible) {
            handler.postDelayed({
                mTop?.visibility = View.INVISIBLE
                mBottom?.visibility = View.INVISIBLE
                verticalSeekBar?.visibility = View.INVISIBLE
                this.topBottomVisible = false
            }, 8000)
        } else {
            handler.removeCallbacksAndMessages(null)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mSuperPlayer?.stop()
        mSuperPlayer?.destroy()
        reset()
        handler.removeCallbacksAndMessages(null)
    }

    fun setPageFinish(listener: PageFinishListener?) {
        mPageFinishListener = listener
    }

    private var mPageFinishListener: PageFinishListener? = null
    interface PageFinishListener {
        fun onScreenIcon(currentDuration: Long)
    }

}