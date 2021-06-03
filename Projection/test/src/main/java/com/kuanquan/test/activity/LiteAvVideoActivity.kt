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
//        setCurrentMediaAndPlay()
    }

    // 设置视频播放的 url
    private fun setCurrentMediaAndPlay() {
//        val url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4"
        val url = "http://cache.m.iqiyi.com/mus/101738001/c9e8c9c713ae4e56bc30d2ec8bf21d10/afbe8fd3d73448c9/0/20200619/20/ba/79c880af528b16f98c06993f6ae078b3.m3u8?qd_time=1621321856512&preIdAll=a888096a8215f6b44d903110fdb8d26b_0&qd_originate=sys&k_uid=537fe876565436ab1abde4e4ef7e2457110f&_lnt=0&ve=26f37f041c524e37265747e97c40733c&bossStatus=0&tvid=100077400&src=02022001010000000000-04000000001000000000-01&tm=1621321858252&qd_vip=0&ff=ts&code=2&vt=2&qd_uri=dash&rpt=2&sgti=10_537fe876565436ab1abde4e4ef7e2457110f_1621321858252&vf=34724a153f6d89f0837566ba42de8d46"
//        mMediaInfo?.url?.let { mVideoPlayerController?.setPlayUrl(it) }
        val str =
            "http://upos-sz-mirrorkodo.bilivideo.com/upgcxcode/46/57/241775746/241775746_nb2-1-64.flv?e=ig8euxZM2rNcNbRzhWdVhwdlhWh1hwdVhoNvNC8BqJIzNbfqXBvEuENvNC8aNEVEtEvE9IMvXBvE2ENvNCImNEVEIj0Y2J_aug859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&ua=tvproj&uipk=5&nbs=1&deadline=1622122624&gen=playurlv2&os=kodobv&oi=1039254948&trid=4cc35a5448564a15ad2eed83266162c5T&upsig=dad2ab2a5b196b3d4e63c6557cbb1ed8&uparams=e,ua,uipk,nbs,deadline,gen,os,oi,trid&mid=602485009&orderid=0,3&logo=80000000&nva_ext=%7B%22ver%22%3A2%2C%22content%22%3A%7B%22sessionId%22%3A%2277780bee-d509-4237-b625-d3dbc4e6ec13%22%2C%22mobileVersion%22%3A6250300%2C%22aid%22%3A414859846%2C%22cid%22%3A241775746%2C%22seasonId%22%3A34684%2C%22epId%22%3A343358%2C%22quality%22%3A64%2C%22contentType%22%3A1%2C%22autoNext%22%3Afalse%2C%22isOpen%22%3Atrue%2C%22seekTs%22%3A4%2C%22accessKey%22%3A%227317e55011809327a6a2d4fbf42dec51%22%7D%7D"

        str?.let {
            mVideoPlayerController?.setPlayUrl(it)
            mVideoPlayerController?.startPlayer()
        }
    }


    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}