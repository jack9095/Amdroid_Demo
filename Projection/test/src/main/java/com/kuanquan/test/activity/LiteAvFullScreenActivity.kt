package com.kuanquan.test.activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.test.R
import com.kuanquan.test.controller.VideoPlayerController
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * 横屏播放的界面
 */
class LiteAvFullScreenActivity : AppCompatActivity() {

    private val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
//        val url = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
    private val RESULT_CODE: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_full_screen_liteav)
        val mTXCloudVideoView  = findViewById<TXCloudVideoView>(R.id.player_cloud_video_view)
        val mVideoPlayerController = findViewById<VideoPlayerController>(R.id.vpc)

        val currentDuration = intent?.getLongExtra("currentDuration",0L) ?: 0L

        mVideoPlayerController?.run {
            keepScreenOn = true // 保持屏幕常亮
            semTXCloudVideoView(mTXCloudVideoView)
            setPlayUrl(url)
            setSeek(currentDuration)
            setTitle("舌尖上的中国")
            addLifecycleObserver(this@LiteAvFullScreenActivity)
            setPageFinish(object : VideoPlayerController.PageFinishListener{
                override fun onScreenIcon(currentDuration: Long) {
                    val intent = Intent()
                    intent.putExtra("currentDuration", currentDuration)
                    setResult(RESULT_CODE, intent)
                    finish()
                }
            })
        }
    }
}