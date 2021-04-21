package com.kuanquan.test.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.test.R
import com.kuanquan.test.controller.VideoPlayerController
import com.kuanquan.test.projection.CallbackInstance
import com.kuanquan.test.projection.MediaInfo
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * To play video media
 * TODO 5
 */
class LiteAvVideoActivity : AppCompatActivity() {

    private var mMediaInfo: MediaInfo? = null
    private var mVideoPlayerController: VideoPlayerController? = null

    private val REQUEST_CODE: Int = 1
    private val RESULT_CODE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liteav_video)

        val mTXCloudVideoView  = findViewById<TXCloudVideoView>(R.id.player_cloud_video_view)
        mVideoPlayerController = findViewById(R.id.vpc)

        mVideoPlayerController?.run {
            keepScreenOn = true // 保持屏幕常亮
            semTXCloudVideoView(mTXCloudVideoView)
            setTitle("舌尖上的中国")
            addLifecycleObserver(this@LiteAvVideoActivity)
            setPageFinish(object : VideoPlayerController.PageFinishListener{
                override fun onScreenIcon(currentDuration: Long) {
                    val intent = Intent(this@LiteAvVideoActivity, LiteAvFullScreenActivity::class.java)
                    intent.putExtra("currentDuration", currentDuration)
                    startActivityForResult(intent, REQUEST_CODE)
                }
            })
        }

        mMediaInfo = intent.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_CODE) {
                val duration = data?.getLongExtra("currentDuration", 0L) ?: 0L
                mVideoPlayerController?.setSeek(duration)
                mVideoPlayerController?.resume()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        mMediaInfo = intent?.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()
    }

    // 设置视频播放的 url
    private fun setCurrentMediaAndPlay() {
        mMediaInfo?.url?.let { mVideoPlayerController?.setPlayUrl(it) }
    }
}