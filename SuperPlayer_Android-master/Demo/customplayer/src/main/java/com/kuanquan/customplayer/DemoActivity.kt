package com.kuanquan.customplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuanquan.customplayer.library.SuperPlayerImpl
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * 最简易的视屏播放方式
 */
class DemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        val mTXCloudVideoView = findViewById<TXCloudVideoView>(R.id.superplayer_cloud_video_view)
        val mSuperPlayer = SuperPlayerImpl(this, mTXCloudVideoView)

        val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
//        val url = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
        mSuperPlayer.play(url)
    }
}