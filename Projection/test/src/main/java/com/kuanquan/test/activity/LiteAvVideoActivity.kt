package com.kuanquan.test.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.test.projection.CallbackInstance
import com.kuanquan.test.projection.MediaInfo
import com.kuanquan.test.R
import com.kuanquan.test.player.SuperPlayer
import com.kuanquan.test.player.SuperPlayerImpl
import com.kuanquan.test.player.SuperPlayerObserver
import com.kuanquan.test.projection.DLNAUtils
import com.tencent.rtmp.ui.TXCloudVideoView

/**
 * To play video media
 * TODO 5
 */
class LiteAvVideoActivity : AppCompatActivity() {

    private var mMediaInfo: MediaInfo? = null
    private var mSeekBar: SeekBar? = null
    private var mSuperPlayer : SuperPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liteav_video)

        val mTXCloudVideoView = findViewById<TXCloudVideoView>(R.id.superplayer_cloud_video_view)
        mSuperPlayer = SuperPlayerImpl(this, mTXCloudVideoView)
        mSuperPlayer?.setObserver(mSuperPlayerObserver)

        mMediaInfo = intent.getSerializableExtra(CallbackInstance.JUMP_VIDEO_PARAMS_KEY) as? MediaInfo?
        setCurrentMediaAndPlay()

        // 系统原生进度条，推荐使用
        seekBar()
    }

    var totalDuration = 0L // 视屏总时长

    private val mSuperPlayerObserver = object : SuperPlayerObserver(){
        override fun onPlayProgress(current: Long, duration: Long) {
            super.onPlayProgress(current, duration)
            totalDuration = duration
            findViewById<TextView>(R.id.tv_progress).text = DLNAUtils.formattedTime(current)
            findViewById<TextView>(R.id.tv_total).text = DLNAUtils.formattedTime(duration)

            if (!isDrag && totalDuration > 0) {
                Log.e("VideoActivity", "进度回掉 = ${(current * 100 / totalDuration).toInt()}")
                mSeekBar?.progress = (current * 100 / totalDuration).toInt()
            }
        }
    }

    var isDrag = false  // 是否是手动拖动
    private fun seekBar() {
        mSeekBar = findViewById<SeekBar>(R.id.sb)
        mSeekBar?.max = 100
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
                    val currentTime = progress * totalDuration / 100
                    mSuperPlayer?.seek(currentTime.toInt())
                }
            }

            // 监听停止时
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("VideoActivity", "结束")
                mSuperPlayer?.resume()
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
        val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
//        val url = "http://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"
        mSuperPlayer?.play(url)
        if (mMediaInfo != null) {

        }
    }
}