package com.kuanquan.test.seek

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener
import com.devbrackets.android.exomedia.listener.OnCompletionListener
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.ui.widget.VideoControls
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.kuanquan.test.projection.CallbackInstance
import com.kuanquan.test.projection.MediaInfo
import com.kuanquan.test.R

/**
 * To play video media
 * TODO 5
 */
class VideoActivityCopy : AppCompatActivity(), OnPreparedListener, OnCompletionListener, OnBufferUpdateListener {

    private var mVideoView: VideoView? = null
    private var mMediaInfo: MediaInfo? = null
    private var mSeekBar: SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_copy)
        mVideoView = findViewById(R.id.video_view)
        mVideoView?.run {
            setHandleAudioFocus(false)
            setOnPreparedListener(this@VideoActivityCopy)
            setOnCompletionListener(this@VideoActivityCopy)
            setOnBufferUpdateListener(this@VideoActivityCopy)
        }
        mMediaInfo = intent.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()

        // 自定义View的进度条
        customSeekBar()

        // 系统原生进度条，推荐使用
        seekBar()

//        mVideoView?.setOnPreparedListener {
//            val duration = mVideoView?.duration
//            Log.e("VideoActivity", "视频时长 = $duration")
//        }

        // 第一个参数是总共时间，第二个参数是间隔触发时间
        val timer = object : CountDownTimer(10000, 1000) {
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

    private fun customSeekBar() {
        //普通进度条
        val mCustomSeekBar = findViewById<CustomSeekBar>(R.id.customSeekBar)
        mCustomSeekBar.setEnabled(true)
        mCustomSeekBar.setMax(100)
        mCustomSeekBar.setProgress(0)
        mCustomSeekBar.setOnChangeListener(object : CustomSeekBar.OnChangeListener {
            override fun onProgressChanged(seekBar: CustomSeekBar?) {
                Log.e("VideoActivity", """
                    mCustomSeekBar:progress = ${seekBar?.progress}
                """.trimIndent())
            }

            override fun onTrackingTouchFinish(seekBar: CustomSeekBar?) {
                Log.e("VideoActivity", "onTrackingTouchFinish >>> ")
            }
        })

        //音乐进度条
        val mMusicSeekBar = findViewById<MusicSeekBar>(R.id.musicSeekBar)
        mMusicSeekBar.setEnabled(true)
        mMusicSeekBar.setMax(100)
        mMusicSeekBar.setProgress(0)
        mMusicSeekBar.setOnMusicListener(object : MusicSeekBar.OnMusicListener {
            override fun onProgressChanged(seekBar: MusicSeekBar?) {
                Log.e("VideoActivity", """
                    mMusicSeekBar:progress = ${seekBar?.progress}
                """.trimIndent())
            }

            override fun getLrcText(): String {
                return "爱就一个字"
            }

            override fun onTrackingTouchFinish(seekBar: MusicSeekBar?) {
                Log.e("VideoActivity", "MusicSeekBar >>> onTrackingTouchFinish >>> ")
            }

            override fun getTimeText(): String {
                return "00:11"
            }
        })
    }

    var isDrag = false  // 是否是手动拖动
    private fun seekBar() {

        val tv_sb = findViewById<TextView>(R.id.tv_sb)
        mSeekBar = findViewById<SeekBar>(R.id.sb)
        mSeekBar?.max = 100;
//        mSeekBar.setMin(10);
        //SeekBar的监听事件
        mSeekBar?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            // 监听点击时
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.e("VideoActivity", "开始")
                tv_sb.text = "开始"
                isDrag = true
            }

            // 监听滑动时
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.e("VideoActivity", "变化${progress.toLong()}")
                tv_sb.text = "进度条${progress.toLong()}"
                if (isDrag) {
                    val currentTime = progress.toLong() * mVideoView?.duration!! / 100
                    mVideoView?.seekTo(currentTime)
                }
            }

            // 监听停止时
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("VideoActivity", "结束")
//                tv_sb.text = "结束"
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

        mVideoView?.currentPosition

        if (mMediaInfo != null) {
            val videoControls = mVideoView?.videoControlsCore
            if (videoControls is VideoControls) {
                videoControls.setTitle(mMediaInfo!!.title)
            }
            val uri = Uri.parse(mMediaInfo?.url)
            Log.e("VideoActivity", "uri ->$uri")
            mVideoView?.setVideoURI(uri)



//            val mediaPlayer = MediaPlayer()
//            mediaPlayer.setDataSource(mMediaInfo?.url)
//            mediaPlayer.prepare()
//            val duration = mediaPlayer.duration
//            Log.e("VideoDurationUtil", "视频时长 = $duration")

//            Observable.create { emitter: ObservableEmitter<File?>? ->
//                val tempFile = VideoDurationUtil.getFileByUrl(mMediaInfo?.url)
//                emitter?.onNext(tempFile) // 把数据发射出去
//                emitter?.onComplete()
//            }.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    val videoDuration = VideoDurationUtil.getDuration(it)
//                    Log.e("VideoDurationUtil", "视频时长 = $videoDuration")
//                }
        }
    }

    // 准备好开始播放
    override fun onPrepared() {
        if (!mVideoView!!.isPlaying) {
            mVideoView?.start()
            findViewById<TextView>(R.id.tv_total).text = generateTime(mVideoView?.duration ?: 0L)
        }
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

//            val currentTime = percent.toLong() * mVideoView?.duration!! / 100
//            findViewById<TextView>(R.id.tv_progress).text = generateTime(currentTime)
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