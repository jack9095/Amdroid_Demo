package com.kuanquan.test

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener
import com.devbrackets.android.exomedia.listener.OnCompletionListener
import com.devbrackets.android.exomedia.listener.OnPreparedListener
import com.devbrackets.android.exomedia.ui.widget.VideoControls
import com.devbrackets.android.exomedia.ui.widget.VideoView


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

        mVideoView?.setOnPreparedListener {
            val duration = mVideoView?.duration
            Log.e("VideoActivity", "视频时长 = $duration")
        }
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

//            VideoDurationUtil.getRingDuring(mMediaInfo?.url)
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