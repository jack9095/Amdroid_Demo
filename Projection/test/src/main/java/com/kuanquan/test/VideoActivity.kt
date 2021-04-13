package com.kuanquan.test

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.kuanquan.test.seek.CustomSeekBar
import com.kuanquan.test.seek.MusicSeekBar

/**
 * To play video media
 * TODO 5
 */
class VideoActivity : AppCompatActivity(), OnPreparedListener, OnCompletionListener, OnBufferUpdateListener {

    private var mVideoView: VideoView? = null
    private var mMediaInfo: MediaInfo? = null

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

        // 自定义View的进度条
        customSeekBar()

        // 系统原生进度条，推荐使用
        seekBar()

//        mVideoView?.setOnPreparedListener {
//            val duration = mVideoView?.duration
//            Log.e("VideoActivity", "视频时长 = $duration")
//        }
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

    private fun seekBar() {

        val tv_sb = findViewById<TextView>(R.id.tv_sb)
        val sb = findViewById<SeekBar>(R.id.sb)
        sb.setMax(100);
//        sb.setMin(10);
        //SeekBar的监听事件
        sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            //监听点击时
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.e("xiaobing", "开始")
                tv_sb.setText("开始")
            }

            //监听滑动时
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.e("xiaobing", "变化$progress")
                tv_sb.setText("进度条$progress")
            }

            //监听停止时
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("xiaobing", "结束")
                tv_sb.setText("结束")
            }
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mMediaInfo = intent?.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()
    }

    @SuppressLint("CheckResult")
    private fun setCurrentMediaAndPlay() {
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

    override fun onPrepared() {
        if (!mVideoView!!.isPlaying) {
            mVideoView?.start()
        }
    }

    override fun onCompletion() {
        mVideoView?.restart()
        mVideoView?.seekTo(0)
    }

    override fun onBufferingUpdate(percent: Int) {

    }
}