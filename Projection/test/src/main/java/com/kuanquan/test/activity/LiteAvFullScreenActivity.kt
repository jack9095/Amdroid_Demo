package com.kuanquan.test.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.kuanquan.test.R
import com.kuanquan.test.controller.VideoPlayerController
import com.kuanquan.test.player.SuperPlayerImpl
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * 横屏播放的界面
 */
class LiteAvFullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_full_screen_liteav)

        val mTXCloudVideoView: TXCloudVideoView = findViewById(R.id.player_cloud_video_view)
        val mVideoPlayerController = findViewById<VideoPlayerController>(R.id.vpc)
        mVideoPlayerController.semTXCloudVideoView(mTXCloudVideoView)

        val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
//        val url = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
        mVideoPlayerController.run {
            keepScreenOn = true // 保持屏幕常亮
            setPlayUrl(url)
            setTitle("舌尖上的中国")
            addLifecycleObserver(this@LiteAvFullScreenActivity)
        }

        mVideoPlayerController.setPageFinish(object : VideoPlayerController.PageFinishListener{
            override fun onFinish() {
                finish()
            }
        })
    }
}