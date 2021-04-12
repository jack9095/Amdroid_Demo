package com.kuanquan.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.devbrackets.android.exomedia.listener.*
import com.devbrackets.android.exomedia.ui.widget.*

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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mMediaInfo = intent?.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()
    }

    private fun setCurrentMediaAndPlay() {
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