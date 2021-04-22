package com.kuanquan.test.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
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

    private var isScreen = true  // true 竖屏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liteav_video)

        val mTXCloudVideoView  = findViewById<TXCloudVideoView>(R.id.player_cloud_video_view)
        mVideoPlayerController = findViewById(R.id.vpc)

        val frameLayout  = findViewById<FrameLayout>(R.id.fl)
        val lp = frameLayout?.layoutParams as? RelativeLayout.LayoutParams

        mVideoPlayerController?.run {
            keepScreenOn = true // 保持屏幕常亮
            semTXCloudVideoView(mTXCloudVideoView)
            setTitle("舌尖上的中国")
            addLifecycleObserver(this@LiteAvVideoActivity)
            setPageFinish(object : VideoPlayerController.PageFinishListener{
                override fun onScreenIcon(currentDuration: Long) {
                    if (currentDuration == 0L) {
                        if (isScreen) {
                            isScreen = false
                            lp?.height = ViewGroup.LayoutParams.MATCH_PARENT
                            lp?.width = ViewGroup.LayoutParams.MATCH_PARENT
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                            val lp = window.attributes
                            // 顶部状态栏完全隐藏 直接对它flags变量操作   LayoutParams.FLAG_FULLSCREEN 表示设置全屏
                            lp.flags = lp.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                            window.attributes = lp
                            // 允许窗口扩展到屏幕之外
                            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        } else {
                            isScreen = true
                            lp?.height = dip2px(this@LiteAvVideoActivity,210f)
//                lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            lp?.width = ViewGroup.LayoutParams.MATCH_PARENT
                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


                            // 顶部状态栏显示但是悬浮在视频上面显示的
//                window.attributes.flags = window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
//                window.attributes = window.attributes
//                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


                            //获得 WindowManager.LayoutParams 属性对象
                            val lp2 = window.attributes
                            //LayoutParams.FLAG_FULLSCREEN 强制屏幕状态条栏弹出
                            lp2.flags = lp2.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                            //设置属性
                            window.attributes = lp2
                            //不允许窗口扩展到屏幕之外  clear掉了
//                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                        }
                        frameLayout?.layoutParams = lp



                    } else {
                        mVideoPlayerController?.pausePlayer()
                        val intent = Intent(this@LiteAvVideoActivity, LiteAvFullScreenActivity::class.java)
                        intent.putExtra("currentDuration", currentDuration)
                        startActivityForResult(intent, REQUEST_CODE)
                    }
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
        val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
//        mMediaInfo?.url?.let { mVideoPlayerController?.setPlayUrl(it) }
        url?.let { mVideoPlayerController?.setPlayUrl(it) }
    }


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}