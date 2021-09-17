package com.shuashuakan.android.modules.publisher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.view.children
import com.kuanquan.doyincover.utils.getColor1
import com.kuanquan.doyincover.utils.getVideoCacheDir
import com.pili.pldroid.player.*
import com.pili.pldroid.player.widget.PLVideoTextureView
import com.pili.pldroid.player.widget.PLVideoView
import com.qiniu.pili.droid.shortvideo.*
import com.shuashuakan.android.R
import com.shuashuakan.android.data.RxBus
import com.shuashuakan.android.data.api.model.chain.GifModel
import com.shuashuakan.android.modules.account.AccountManager
import com.shuashuakan.android.event.GifToCommentEvent
import com.shuashuakan.android.modules.account.activity.LoginActivity
import com.shuashuakan.android.spider.SpiderEventNames
import com.shuashuakan.android.ui.ProgressDialog
import com.shuashuakan.android.ui.base.FishActivity
import com.shuashuakan.android.modules.publisher.view.CutView
import com.shuashuakan.android.modules.publisher.view.OnVideoEditorListener
import com.shuashuakan.android.modules.publisher.view.VideoEditorView
import com.shuashuakan.android.modules.publisher.view.ViewType
import com.shuashuakan.android.modules.share.ShareHelper
import com.shuashuakan.android.utils.*
import java.io.File
import javax.inject.Inject

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/21
 * Description:
 */
class CreateGifActivity : FishActivity() {
    private val previewLayout by bindView<FrameLayout>(R.id.preview_layout)
    private val mTextureView by bindView<PLVideoView>(R.id.video_player)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val cutView by bindView<CutView>(R.id.cut_view)

    private val operationDeleteSwitch by bindView<ViewSwitcher>(R.id.operation_delete_switch)

    private val videoEditorView by bindView<VideoEditorView>(R.id.video_editor)
    private val deleteView by bindView<ImageView>(R.id.delete_view)
    private val operationLayout by bindView<LinearLayout>(R.id.operation_layout)
    private val makeDonePage by bindView<FrameLayout>(R.id.make_done_page)

    val loadingDialog by lazy {
        return@lazy ProgressDialog.progressDialog(this@CreateGifActivity, "", false)
    }

    @Inject
    lateinit var accountManager: AccountManager
    @Inject
    lateinit var shareHelper: ShareHelper

    companion object {
        private const val REQUEST_CODE_LOGIN = 1
        private const val PARAM_FEED_ID = "feedId"
        private const val PARAM_VIDEO_URL = "videoUrl"
        private const val PARAM_START_TIME = "startTime"
        private const val PARAM_END_TIME = "endTime"
        fun create(context: Context, feedId: String?, videoUrl: String?, startTime: Long?, endTime: Long?): Intent {
            val intent = Intent(context, CreateGifActivity::class.java)
            intent.putExtra(PARAM_VIDEO_URL, videoUrl)
            intent.putExtra(PARAM_FEED_ID, feedId)
            intent.putExtra(PARAM_START_TIME, startTime)
            intent.putExtra(PARAM_END_TIME, endTime)
            return intent
        }
    }

    private var feedId: String? = null
    private var videoUrl: String? = null
    private var startTime: Long = 0L
    private var endTime: Long = 0L

    private var isCroped = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_gif)
        exitCut()
        changeTranslucentStatusBar()
        videoUrl = intent.getStringExtra(PARAM_VIDEO_URL)
        feedId = intent.getStringExtra(PARAM_FEED_ID)
        startTime = intent.getLongExtra(PARAM_START_TIME, 0L)
        endTime = intent.getLongExtra(PARAM_END_TIME, 0L)
        videoEditorView.setDeleteView(deleteView)
        videoEditorView.setContentView(mTextureView)
        initPlayer()
        openPlayer()
        makeDonePage.setOnTouchListener { _, _ ->
            true
        }
        videoEditorView.setEditorListener(onVideoEditorListener)
    }

    private fun initPlayer() {
        val options = AVOptions()
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO)
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1)
        options.setInteger(AVOptions.KEY_PREFER_FORMAT, 2)
        options.setInteger(AVOptions.KEY_LOG_LEVEL, 2)
        options.setString(AVOptions.KEY_CACHE_DIR, getVideoCacheDir(this))
        options.setInteger(AVOptions.KEY_SEEK_MODE, 1)

        mTextureView.setAVOptions(options)
        mTextureView.setOnPreparedListener(mOnPreparedListener)
        mTextureView.setOnVideoSizeChangedListener(mOnVideoSizeListener)
        mTextureView.setOnInfoListener(mOnInfoListener)
        mTextureView.setOnCompletionListener(mOnCompletionListener)
        mTextureView.setVolume(0f, 0f)
    }

    private fun openPlayer() {
        mTextureView.setVideoPath(videoUrl)
    }

    fun onAddTextClick(v: View) {
        videoEditorView.addText()
    }

    private var editor: PLShortVideoEditor? = null
    private var surfaceView: GLSurfaceView? = null

    fun onCropVideoClick(v: View) {
        cutView.layoutParams.width = mTextureView.measuredWidth
        cutView.layoutParams.height = mTextureView.measuredHeight
        cutView.requestLayout()
        cutView.invalidate()

        if (!isCroped) {
            enterCut()
        }
    }

    private fun initEditor() {
        surfaceView = GLSurfaceView(this)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER)
        previewLayout.addView(surfaceView, 0, params)

        val setting = PLVideoEditSetting()
        setting.sourceFilepath = if (filePath != null) filePath else getCacheFilePath()
        setting.destFilepath = File(VideoRecordActivity.getRecordCachePath(this), "edit.mp4").absolutePath
        setting.isKeepOriginFile = true

        editor = PLShortVideoEditor(surfaceView, setting)
        editor?.setVideoSaveListener(object : PLVideoSaveListener {
            override fun onSaveVideoCanceled() {
                runOnUiThread {
                    loadingDialog.dismiss()
                }
            }

            override fun onProgressUpdate(p0: Float) {
            }

            override fun onSaveVideoSuccess(p0: String) {
                runOnUiThread {
                    endTime = 0L
                    videoToGif(p0)
                }
            }

            override fun onSaveVideoFailed(p0: Int) {
                runOnUiThread {
                    loadingDialog.dismiss()
                    showShortToast(String.format(getString(com.shuashuakan.android.base.ui.R.string.string_video_create_error_format), p0.toString()))
                }
            }
        })
    }

    private var filePath: String? = null

    private fun getCacheFilePath(): String? {
        return if (videoUrl != null) {
            val fileName = videoUrl!!.substring(videoUrl!!.indexOf("//") + 2, videoUrl!!.length).replace("/", "-")
            File(getVideoCacheDir(this), "$fileName.mp4").absolutePath
        } else {
            null
        }
    }

    private val mOnCompletionListener = PLOnCompletionListener {
        mTextureView.start()
        mTextureView.seekTo(startTime)
    }

    private val mOnPreparedListener = PLOnPreparedListener {
        mTextureView.displayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT
        mTextureView.start()
        mTextureView.seekTo(startTime)
    }

    private val mOnInfoListener = PLOnInfoListener { what, extra ->
        if (PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING == what) {
            if (endTime > 0L) {
                if (extra >= endTime) {
                    mTextureView.seekTo(startTime)
                }
            }
        }
    }

    private var mVideoHeight = 0
    private var mVideoWidth = 0

    private val mOnVideoSizeListener = PLOnVideoSizeChangedListener { width, height ->
        mVideoHeight = height
        mVideoWidth = width
    }

    private var isPause: Boolean = false

    override fun onPause() {
        super.onPause()
        isPause = true
        mTextureView.pause()
    }

    override fun onResume() {
        super.onResume()
        if (isPause) {
            isPause = false
            mTextureView.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTextureView.stopPlayback()
        previewLayout.removeView(mTextureView)
    }

    private fun adjustAspectRatio(videoWidth: Int, videoHeight: Int) {
        val viewWidth = previewLayout.measuredWidth
        val viewHeight = previewLayout.measuredHeight
        val aspectRatio = videoHeight.toDouble() / videoWidth
        var newWidth: Int
        var newHeight: Int

        newWidth = viewWidth
        newHeight = (newWidth * aspectRatio).toInt()
        val viewInScreenRatio = newHeight.toFloat() / viewHeight
        if (viewInScreenRatio > 0.82 && viewInScreenRatio <= 1) {
            newHeight = viewHeight
            newWidth = (viewHeight / aspectRatio).toInt()
        }
        mTextureView.layoutParams.width = newWidth
        mTextureView.layoutParams.height = newHeight
        mTextureView.requestLayout()
    }

    private fun save() {
        if (videoEditorView.childCount > 0) {
            if (editor == null || surfaceView == null) {
                initEditor()
            }
            if (endTime > 0) {
                editor?.setVideoRange(startTime, endTime)
            }
            val viewList = mutableListOf<PLTextView>()
            videoEditorView.children.forEach {
                if (it is PLTextView) {
                    viewList.add(it)
                }
            }
            videoEditorView.removeAllViews()
            viewList.forEach {
                editor?.addTextView(it)
            }
            editor?.save()
        } else {
            cutVideo {
                videoToGif(it)
            }
        }
        loadingDialog.show()
        setDialogMessage(getString(R.string.string_craete_gif_ing))
    }

    private fun setBaseToolbar() {
        toolbar.menu.clear()
        toolbar.setBackgroundColor(getColor1(R.color.color_normal_2222222))
        toolbar.title = getString(R.string.string_create_face_package)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.inflateMenu(R.menu.menu_done)
        toolbar.setOnMenuItemClickListener {
            save()
            true
        }
    }

    private fun setCutToolbar() {
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            exitCut()
        }
        toolbar.setNavigationIcon(R.drawable.ic_record_close)
        toolbar.inflateMenu(R.menu.menu_image_confirm)
        toolbar.setOnMenuItemClickListener { _ ->
            cutVideo {
                cropVideo(it)
            }
            true
        }
    }

    private fun cropVideo(path: String) {
        //得到裁剪后的margin值
        val cutArr = cutView.cutArr
        val left = cutArr[0]
        val top = cutArr[1]
        val right = cutArr[2]
        val bottom = cutArr[3]
        val cutWidth = cutView.rectWidth
        val cutHeight = cutView.rectHeight

        //计算宽高缩放比
        val leftPro = left / cutWidth
        val topPro = top / cutHeight
        val rightPro = right / cutWidth
        val bottomPro = bottom / cutHeight

        //得到裁剪位置
        val cropWidth = (mVideoWidth * (rightPro - leftPro)).toInt()
        val cropHeight = (mVideoHeight * (bottomPro - topPro)).toInt()
        val x = (leftPro * mVideoWidth).toInt()
        val y = (topPro * mVideoHeight).toInt()

        val media = PLMediaFile(path)

        val cacheFilePath = File(VideoRecordActivity.getRecordCachePath(this), "record${System.currentTimeMillis()}.mp4").absolutePath
        val transCoder = PLShortVideoTranscoder(this, path, cacheFilePath)
        transCoder.setClipArea(x, y, cropWidth, cropHeight)
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
            setDialogMessage(getString(R.string.string_video_crop_ing))
        }
        transCoder.transcode(cropWidth, cropHeight, media.videoBitrate, media.videoRotation, false, object : PLVideoSaveListener {
            override fun onSaveVideoFailed(p0: Int) {
                runOnUiThread {
                    loadingDialog.dismiss()
                    exitCut()
                    showShortToast(String.format(getString(com.shuashuakan.android.base.ui.R.string.string_video_crop_error_format), p0.toString()))
                }
            }

            override fun onSaveVideoCanceled() {
                runOnUiThread {
                    loadingDialog.dismiss()
                }
            }

            override fun onProgressUpdate(p0: Float) {
            }

            override fun onSaveVideoSuccess(p0: String?) {
                if (path.contains("ugc/")) {
                    File(path).delete()
                }
                filePath = p0
                mVideoWidth = cropWidth
                mVideoHeight = cropHeight
                runOnUiThread {
                    isCroped = true
                    findViewById<ImageView>(R.id.crop_image).setImageResource(R.drawable.ic_video_edit_crop_disable)
                    loadingDialog.dismiss()
                    exitCut()
                    mTextureView.stopPlayback()
                    initPlayer()
                    mTextureView.setVideoPath(filePath)
//          adjustAspectRatio(mVideoWidth, mVideoHeight)
                }
            }
        })
    }

    private fun cutVideo(block: (s: String) -> Unit) {
        if (endTime > 0L) {
            val cacheFilePath = File(VideoRecordActivity.getRecordCachePath(this), "trimmer.mp4").absolutePath
            val trimmer = PLShortVideoTrimmer(this, getCacheFilePath(), cacheFilePath)
            loadingDialog.show()
            setDialogMessage(getString(R.string.string_video_crop_ing))
            trimmer.trim(startTime, endTime, PLShortVideoTrimmer.TRIM_MODE.ACCURATE, object : PLVideoSaveListener {
                override fun onSaveVideoCanceled() {
                    runOnUiThread {
                        loadingDialog.dismiss()
                    }
                }

                override fun onProgressUpdate(p0: Float) {
                }

                override fun onSaveVideoSuccess(p0: String) {
                    endTime = 0L
                    filePath = p0
                    runOnUiThread {
                        block(p0)
//              isCroped = true
//              findViewById<ImageView>(R.id.crop_image).setImageResource(R.drawable.ic_video_edit_crop_disable)
//              loadingDialog.dismiss()
//              exitCut()
//              mTextureView.stopPlayback()
//              initPlayer()
//              mTextureView.setVideoPath(filePath)
                    }
                }

                override fun onSaveVideoFailed(p0: Int) {
                    runOnUiThread {
                        loadingDialog.dismiss()
                        exitCut()
                        showShortToast(String.format(getString(com.shuashuakan.android.base.ui.R.string.string_video_crop_error_format), p0.toString()))
                    }
                }
            })
        } else {
            block(filePath!!)
        }
    }

    private fun videoToGif(s: String?) {
        val file = PLMediaFile(s)
        val frameTotal = file.durationMs / 1000 * 5
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
        val composer = PLShortVideoComposer(this)
        composer.extractVideoToGIF(s, 0, file.durationMs, frameTotal.toInt(), mVideoWidth, mVideoHeight, 5, true,
                "${Environment.getExternalStorageDirectory()}/DCIM/Camera/GIF${TimeUtil.getCurrentTime("yyyyMMddHHmmss")}.gif",
                object : PLVideoSaveListener {
                    override fun onSaveVideoCanceled() {
                        runOnUiThread {
                            loadingDialog.dismiss()
                        }
                    }

                    override fun onProgressUpdate(p0: Float) {

                    }

                    override fun onSaveVideoSuccess(p0: String?) {
                        if (s!!.contains("/ugc")) {
                            File(s).delete()
                        }
                        if (filePath != null) {
                            File(filePath).delete()
                        }
                        filePath = p0
                        createSpider()
                        runOnUiThread {
                            loadingDialog.dismiss()
                            openMakeDonePage(filePath)
                        }
                    }

                    override fun onSaveVideoFailed(p0: Int) {
                        runOnUiThread {
                            loadingDialog.dismiss()
                            showShortToast(String.format(getString(com.shuashuakan.android.base.ui.R.string.string_gif_error_format), p0.toString()))
                        }
                    }
                })
    }

    private fun enterCut() {
        operationDeleteSwitch.visibility = View.GONE
        videoEditorView.visibility = View.GONE
        cutView.visibility = View.VISIBLE
        setCutToolbar()
    }

    private fun exitCut() {
        operationDeleteSwitch.visibility = View.VISIBLE
        videoEditorView.visibility = View.VISIBLE
        cutView.visibility = View.GONE
        setBaseToolbar()
    }

    private fun openMakeDonePage(filePath: String?) {
        // 通知图库更新
        if (filePath != null)
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(filePath))))

        val ctrlAnimation = TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f)
        makeDonePage.visibility = View.VISIBLE
        makeDonePage.startAnimation(ctrlAnimation)
    }

    fun onShareCommentClick(v: View) {
        if (!accountManager.hasAccount()) {
            LoginActivity.launchForResult(this, REQUEST_CODE_LOGIN)
        } else {
            getSpider().manuallyEvent(SpiderEventNames.EXPRESSION_COMMENT)
                    .put("feedID", feedId ?: "")
                    .put("userID", getUserId()).track()
            finish()
            RxBus.get().post(GifToCommentEvent(GifModel(filePath!!, mVideoWidth, mVideoHeight)))
        }
    }

    fun onShareWechatClick(v: View) {
        shareHelper.doShare(this@CreateGifActivity, filePath!!, "wechat_session", null, true) {
            shareSpider("wechat_session")
        }
    }

    fun onShareQqClick(v: View) {
        shareHelper.doShare(this@CreateGifActivity, filePath!!, "QQ", null, true) {
            shareSpider("QQ")
        }
    }

    private fun shareSpider(shareWay: String) {
        getSpider().manuallyEvent(SpiderEventNames.EXPRESSION_SHARE)
                .put("feedID", feedId ?: "")
                .put("userID", getUserId())
                .put("isSuccess", true)
                .put("shareWay", shareWay)
                .track()
    }

    private fun createSpider() {
        getSpider().manuallyEvent(SpiderEventNames.EXPRESSION_CREATE)
                .put("feedID", feedId ?: "")
                .put("userID", accountManager.account()?.userId ?: "")
                .put("startPosition", intent.getLongExtra(PARAM_START_TIME, 0L))
                .put("endPosition", intent.getLongExtra(PARAM_END_TIME, 0L))
                .track()
    }

    fun onBackFeedClick(v: View) {
        finish()
    }

    private val onVideoEditorListener = object : OnVideoEditorListener {
        override fun onEditTextChangeListener(textView: TextView?) {
        }

        override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        }

        override fun onRemoveViewListener(numberOfAddedViews: Int) {
        }

        override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        }

        override fun onStartViewChangeListener(viewType: ViewType?) {
            operationDeleteSwitch.displayedChild = 1
        }

        override fun onStopViewChangeListener(viewType: ViewType?) {
            operationDeleteSwitch.displayedChild = 0
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
            RxBus.get().post(GifToCommentEvent(GifModel(filePath!!, mVideoWidth, mVideoHeight)))
            finish()
        }
    }

    fun setDialogMessage(message: String) {
        loadingDialog.findViewById<TextView>(com.shuashuakan.android.base.ui.R.id.loadingTextView).text = message
    }
}