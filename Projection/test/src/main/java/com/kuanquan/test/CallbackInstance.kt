package com.kuanquan.test

import android.content.Intent
import android.util.Log
import com.kuanquan.test.app.WorkApplication
import com.kuanquan.test.enum_p.MediaType
import com.plutinosoft.platinum.CallbackTypes
import com.plutinosoft.platinum.DLNACallback

/**
 * 接收 native 传递上来的消息回调实例
 * 枚举单例
 * TODO 4
 */
object CallbackInstance {
    private const val TAG = "CallbackInstance"
    // 跳转到视频播放页面的参数 key
    const val JUMP_VIDEO_PARAMS_KEY = "EXTRA_MEDIA_INFO_KEY"

    // c++ 层检测到的投屏数据回掉类
    private var mDLNACallback: DLNACallback? = null

    fun getDLNACallback(): DLNACallback? {
        return mDLNACallback
    }

    init {
        mDLNACallback = DLNACallback { type: Int, param1: String, param2: String, param3: String ->
            // String类型的函数(方法)trimIndent()用于切割每一行开头相同数量的空格, 三个双引号 可以包含换行，原始字符串中的\将不被识别为转义
            Log.e(
                TAG, """
                     type: $type
                     param1: $param1
                     param2: $param2
                     param3: $param3
                     """.trimIndent()
            )

            when (type) {
                CallbackTypes.CALLBACK_EVENT_ON_PLAY -> startPlayMedia(type, param1, param2)
                CallbackTypes.CALLBACK_EVENT_ON_PAUSE -> {
                }
                CallbackTypes.CALLBACK_EVENT_ON_SEEK -> {
                }
            }
        }
    }

    private fun startPlayMedia(type: Int, url: String, meta: String) {
        val mediaInfo: MediaInfo? = DLNAUtils.getMediaInfo(url, meta)
        if (mediaInfo?.mediaType === MediaType.TYPE_UNKNOWN) {
            Log.e(TAG, "Media Type Unknown!")
            return
        }

        Log.e(
            TAG, """
                 跳转视频播放页面 
                 ${mediaInfo.toString()}
                 """.trimIndent()
        )

        if (WorkApplication.currentActivity != null) {
            val intent = Intent()
            intent.setClass(WorkApplication.currentActivity!!, VideoActivity::class.java)
            intent.putExtra(JUMP_VIDEO_PARAMS_KEY, mediaInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            WorkApplication.currentActivity?.startActivity(intent)
            Log.e(TAG, "跳转完成")
        }
    }
}