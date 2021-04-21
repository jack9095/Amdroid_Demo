package com.kuanquan.test.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devbrackets.android.exomedia.listener.*
import com.devbrackets.android.exomedia.ui.widget.*
import com.kuanquan.test.projection.CallbackInstance
import com.kuanquan.test.projection.MediaInfo
import com.kuanquan.test.R

/**
 * To play video media
 * TODO 5
 */
class VideoActivity : AppCompatActivity(), OnPreparedListener, OnCompletionListener, OnBufferUpdateListener {

    val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
    //        val url = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
    private var mVideoView: VideoView? = null
    private var mMediaInfo: MediaInfo? = null
    private var mSeekBar: SeekBar? = null
    private var mCountDownTimer : CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        mVideoView = findViewById(R.id.video_view)
        mVideoView?.run {
            setHandleAudioFocus(false)
            setOnPreparedListener(this@VideoActivity)
            setOnCompletionListener(this@VideoActivity)
            setOnBufferUpdateListener(this@VideoActivity)
        }
        mMediaInfo = intent.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()

        // 系统原生进度条，推荐使用
        seekBar()

        // 第一个参数是总共时间，第二个参数是间隔触发时间
        val timer = object : CountDownTimer(mVideoView?.duration!!, 1000) {
            override fun onFinish() {
                Log.e("VideoActivity", "onfinish...." + "结束了")
            }

            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.tv_progress).text = generateTime(mVideoView?.currentPosition!!)
                Log.e("VideoActivity", "ontick...." + millisUntilFinished / 1000 + "s后结束")
            }
        }
        timer.start()
    }

    var isDrag = false  // 是否是手动拖动
    private fun seekBar() {

        mSeekBar = findViewById<SeekBar>(R.id.sb)
        mSeekBar?.max = 100;
        //SeekBar的监听事件
        mSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            // 监听点击时
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.e("VideoActivity", "开始")
                isDrag = true
            }

            // 监听滑动时
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.e("VideoActivity", "变化${progress.toLong()}")
                if (isDrag) {
                    val currentTime = progress.toLong() * mVideoView?.duration!! / 100
                    mVideoView?.seekTo(currentTime)
                }
            }

            // 监听停止时
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("VideoActivity", "结束")
                isDrag = false
            }
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mMediaInfo = intent?.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()
    }

    // 设置视频播放的 url
    @SuppressLint("CheckResult")
    private fun setCurrentMediaAndPlay() {

        val videoUrl = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
        mVideoView?.setVideoPath(videoUrl)

        if (mMediaInfo != null) {
            val videoControls = mVideoView?.videoControlsCore
            if (videoControls is VideoControls) {
                videoControls.setTitle(mMediaInfo!!.title)
            }
            val uri = Uri.parse(mMediaInfo?.url)
            Log.e("VideoActivity", "uri ->$uri")
            mVideoView?.setVideoURI(uri)
        }
    }

    // 准备好开始播放
    override fun onPrepared() {
        if (!mVideoView!!.isPlaying) {
            mVideoView?.start()
            findViewById<TextView>(R.id.tv_total).text = generateTime(mVideoView?.duration ?: 0L)

            Log.e("VideoActivity", "看看走几次")

            // 第一个参数是总共时间，第二个参数是间隔触发时间
            mCountDownTimer = object : CountDownTimer(mVideoView?.duration!!, 1000) {
                override fun onFinish() {
                    Log.e("VideoActivity", "onFinish...." + "结束了")
                }

                override fun onTick(millisUntilFinished: Long) {
                    findViewById<TextView>(R.id.tv_progress).text = generateTime(mVideoView?.currentPosition!!)
                    Log.e("VideoActivity", "onTick...." + millisUntilFinished / 1000 + "s后结束")
                }
            }
            mCountDownTimer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 防止内存泄漏
        mCountDownTimer?.cancel()
    }

    // 播放完成
    override fun onCompletion() {
//        mVideoView?.restart()
//        mVideoView?.seekTo(0)
    }

    override fun onBufferingUpdate(percent: Int) {
        Log.e("VideoActivity", "进度回掉 = $percent")
        if (!isDrag) {
            mSeekBar?.progress = percent
        }
    }

    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    private fun generateTime(time: Long): String? {
        val totalSeconds = (time / 1000).toInt()
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) String.format("%02dh%02dm%02ds", hours, minutes, seconds) else String.format("%02dm%02ds", minutes, seconds)
    }
}