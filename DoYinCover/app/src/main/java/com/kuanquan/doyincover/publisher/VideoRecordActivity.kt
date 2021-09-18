package com.kuanquan.doyincover.publisher

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.media.AudioFormat
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuanquan.doyincover.R
import com.kuanquan.doyincover.base.FishActivity
//import com.luck.picture.lib.PictureSelector
//import com.luck.picture.lib.config.PictureConfig
//import com.luck.picture.lib.config.PictureMimeType
import com.qiniu.pili.droid.shortvideo.*
import com.qiniu.pili.droid.shortvideo.PLErrorCode.*
//import com.shuashuakan.android.R
//import com.shuashuakan.android.data.RxBus
//import com.shuashuakan.android.event.VideoRecordFinishEvent
import com.kuanquan.doyincover.publisher.utils.StringUtil
import com.kuanquan.doyincover.publisher.view.FocusIndicator
import com.kuanquan.doyincover.publisher.view.RecordButton
import com.kuanquan.doyincover.publisher.view.RoundedCornersTransformation
import com.kuanquan.doyincover.publisher.view.SectionProgressBar
import com.kuanquan.doyincover.utils.ScreenUtils
//import com.shuashuakan.android.ui.ProgressDialog
//import com.shuashuakan.android.ui.base.FishActivity
//import com.kuanquan.doyincover.publisher.view.RecordButton
//import transform.RoundedCornersTransformation
//import com.shuashuakan.android.utils.bindView
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.util.*

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/10
 * Description:
 */
class VideoRecordActivity : FishActivity() {

    companion object {
        const val DEFAULT_MAX_RECORD_DURATION: Long = 30 * 1000
        const val DEFAULT_MIN_RECORD_DURATION: Long = 3 * 1000

        fun start(context: Context, type: Int, masterId: String? = null, channelId: String? = "", channelName: String? = "") {
            val intent = Intent(context, VideoRecordActivity::class.java)
            intent.putExtra("model", RecordDataModel(null, null, type, masterId, channelId, channelName))
            context.startActivity(intent)
        }

        fun getRecordCachePath(context: Context): String? {
            val cacheDir = File(context.externalCacheDir, "ugc")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            return cacheDir.absolutePath
        }
    }

    private val preview by lazy { findViewById<GLSurfaceView>(R.id.preview) }
    private val mFocusIndicator by lazy{ findViewById<FocusIndicator>(R.id.focus_indicator) }
    private val recordProgressbar by lazy { findViewById<SectionProgressBar>(R.id.record_progressbar) }
    private val record by lazy { findViewById<RecordButton>(R.id.record) }
    private val recordOperation by lazy { findViewById<View>(R.id.recording_operation) }
    private val openGallery by lazy { findViewById<View>(R.id.open_gallery) }
    private val openGalleryImage by lazy { findViewById<ImageView>(R.id.open_gallery_image) }
    private val switchFaceBeauty by lazy { findViewById<TextView>(R.id.switch_face_beauty) }
    private val recordBottomOperation by lazy { findViewById<View>(R.id.record_bottom_operation) }

    private val filterScrollHint by lazy { findViewById<TextView>(R.id.filter_scroll_hint) }
    private val filterNameText by lazy { findViewById<TextView>(R.id.filter_name_text) }
    private val recordNextStepBtn by lazy { findViewById<ImageView>(R.id.record_next_step) }
    private val recordOperationLayout by lazy { findViewById<View>(R.id.record_operation_layout) }

    private lateinit var mShortVideoRecorder: PLShortVideoRecorder
    private lateinit var mCameraSetting: PLCameraSetting
    private lateinit var mMicrophoneSetting: PLMicrophoneSetting
    private lateinit var mRecordSetting: PLRecordSetting
    private lateinit var mVideoEncodeSetting: PLVideoEncodeSetting
    private lateinit var mAudioEncodeSetting: PLAudioEncodeSetting
    private lateinit var mFaceBeautySetting: PLFaceBeautySetting

    private var mFocusIndicatorX: Int = 0
    private var mFocusIndicatorY: Int = 0

    private lateinit var mGestureDetector: GestureDetector

    private var filterList = mutableListOf<PLBuiltinFilter>()

    private var model: RecordDataModel? = null

//    private val progressDialog by lazy {
//        return@lazy ProgressDialog.progressDialog(this, getString(R.string.string_video_process))
//    }
//    private val eventCompositeDisposable = CompositeDisposable()
//
//    private val filterDialog: FilterListBottomDialog by lazy {
//        FilterListBottomDialog(this)
//    }

    private var cacheFilePath: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video_record)
        model = intent.getParcelableExtra("model")

        cacheFilePath = File(getRecordCachePath(this), "record.mp4")

        initRecorder()
        setOther()
        initFilterList()
        getFirstVideo()
//        RxBus.get().toFlowable().subscribe {
//            if (it is VideoRecordFinishEvent) {
//                finish()
//            }
//        }.addTo(eventCompositeDisposable)
    }

    private fun initRecorder() {
        mShortVideoRecorder = PLShortVideoRecorder()
        mShortVideoRecorder.setRecordStateListener(recordStateListener)
        mShortVideoRecorder.setFocusListener(focusListener)

        mCameraSetting = PLCameraSetting()
        mCameraSetting.cameraId = PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK
        mCameraSetting.cameraPreviewSizeRatio = PLCameraSetting.CAMERA_PREVIEW_SIZE_RATIO.RATIO_16_9
        mCameraSetting.cameraPreviewSizeLevel = PLCameraSetting.CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_720P

        mMicrophoneSetting = PLMicrophoneSetting()
        mMicrophoneSetting.channelConfig = AudioFormat.CHANNEL_IN_STEREO

        mVideoEncodeSetting = PLVideoEncodeSetting(this)
        mVideoEncodeSetting.setEncodingSizeLevel(PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_720P_3)
        mVideoEncodeSetting.encodingBitrate = 2000 * 1000
        mVideoEncodeSetting.isHWCodecEnabled = true
        mVideoEncodeSetting.setConstFrameRateEnabled(true)

        mAudioEncodeSetting = PLAudioEncodeSetting()
        mAudioEncodeSetting.isHWCodecEnabled = true
        mAudioEncodeSetting.channels = 2

        mRecordSetting = PLRecordSetting()
        mRecordSetting.maxRecordDuration = DEFAULT_MAX_RECORD_DURATION
        mRecordSetting.setRecordSpeedVariable(true)
        mRecordSetting.setVideoCacheDir(cacheFilePath?.parent)
        mRecordSetting.videoFilepath = cacheFilePath?.absolutePath

        mFaceBeautySetting = PLFaceBeautySetting(0.6f, 0.5f, 0.3f)

        mShortVideoRecorder.prepare(preview, mCameraSetting, mMicrophoneSetting, mVideoEncodeSetting,
                mAudioEncodeSetting, mFaceBeautySetting, mRecordSetting)

        recordProgressbar.setFirstPointTime(DEFAULT_MIN_RECORD_DURATION)
        onSectionCountChanged(0, 0)
        recordProgressbar.setTotalTime(this, mRecordSetting.maxRecordDuration)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOther() {
        mGestureDetector = GestureDetector(this, mGestureDetectorListener)
        record.setOnRecordStateChangedListener(recordButtonListener)
        preview.setOnTouchListener { _, motionEvent ->
            mGestureDetector.onTouchEvent(motionEvent)
            true
        }
//        filterDialog.listener = object : FilterListBottomDialog.OnFilterDialogListener {
//            override fun onSelectPosition(position: Int) {
//                selectedPosition = position
//                selectFilter(filterList[selectedPosition])
//            }
//        }
//        filterDialog.setOnShowListener {
//            record.visibility = View.GONE
//            recordBottomOperation.visibility = View.GONE
//
//            if (filterDialog.getSelectPosition() != selectedPosition) {
//                filterDialog.setFilterPosition(selectedPosition)
//            }
//            filterDialog.scrollFilter(selectedPosition)
//        }
//        filterDialog.setOnDismissListener {
//            record.visibility = View.VISIBLE
//            recordBottomOperation.visibility = View.VISIBLE
//        }
        filterNameText.postDelayed(runnable, 3000)
    }

    private fun initFilterList() {
        val filters = mShortVideoRecorder.builtinFilterList
        if (filters != null) {
            filterList.addAll(Arrays.asList(*filters))
            val builtinFilter = PLBuiltinFilter()
            builtinFilter.name = "none"
            filterList.add(0, builtinFilter)
        } else {
            filterList = mutableListOf()
        }
//        filterDialog.initData(filterList)
    }

    private var selectedPosition = 0

    private fun prevFilter() {
        if (!filterList.isEmpty()) {
            if (selectedPosition <= 0) {
                selectedPosition = filterList.size - 1
            } else {
                selectedPosition--
            }
            selectFilter(filterList[selectedPosition])
        }
    }

    private fun nextFilter() {
        if (!filterList.isEmpty()) {
            if (selectedPosition >= filterList.size - 1) {
                selectedPosition = 0
            } else {
                selectedPosition++
            }
            selectFilter(filterList[selectedPosition])
        }
    }

    private fun selectFilter(plBuiltinFilter: PLBuiltinFilter) {
        if (plBuiltinFilter.name != "none") {
            mShortVideoRecorder.setBuiltinFilter(plBuiltinFilter.name)
        } else {
            mShortVideoRecorder.setBuiltinFilter(null)
        }
        if (filterScrollHint.visibility == View.VISIBLE) {
            filterScrollHint.visibility = View.INVISIBLE
        }
        filterNameText.text = StringUtil.getChineseName(plBuiltinFilter.name, this)
        filterNameText.visibility = View.VISIBLE
        filterNameText.removeCallbacks(runnable)
        filterNameText.postDelayed(runnable, 1000)

//        filterDialog.setFilterPosition(selectedPosition)
    }

    private val runnable = Runnable {
        if (filterNameText.visibility == View.VISIBLE)
            filterNameText.visibility = View.INVISIBLE
        if (filterScrollHint.visibility == View.VISIBLE) {
            filterScrollHint.visibility = View.INVISIBLE
        }
    }

    private var isRecording = false

    private fun updateRecordingBtns(isRecording: Boolean) {
        this.isRecording = isRecording
        if (isRecording) {
            recordOperationLayout.visibility = View.GONE
        } else {
            recordOperationLayout.visibility = View.VISIBLE
            record.reset()
        }
    }

    override fun onResume() {
        super.onResume()
        mShortVideoRecorder.resume()
        if (selectedPosition > 0) {
            selectFilter(filterList[selectedPosition])
        }
    }

    override fun onPause() {
        super.onPause()
        mShortVideoRecorder.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mShortVideoRecorder.setFocusListener(null)
//        eventCompositeDisposable.clear()
        mShortVideoRecorder.destroy()
    }

    fun onClickSwitchCamera(v: View) {
        mShortVideoRecorder.switchCamera()
        mFocusIndicator.focusCancel()
    }

    fun onClickSwitchFaceBeauty(v: View) {
        mFaceBeautySetting.setEnable(!mFaceBeautySetting.isEnabled)
        val top = if (mFaceBeautySetting.isEnabled) {
            ContextCompat.getDrawable(this, R.drawable.ic_record_face_beauty_open)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_record_face_beauty_close)
        }
        switchFaceBeauty.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null)
    }

    fun onClickFilter(v: View) {
//        filterDialog.show()
    }

    fun onClickClose(v: View) {
        comeBack()
    }

    override fun onBackPressed() {
        comeBack()
    }

    private fun comeBack() {
        if (recordProgressbar.count > 0) {
//            createDialog(getString(R.string.string_can_give_up_video), DialogInterface.OnClickListener { _, _ ->
//                finish()
//            }).show()
        } else {
            finish()
        }
    }

    fun onClickOpenGallery(v: View) {
//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofVideo())
//                .theme(R.style.customPictureStyle)
//                .selectionMode(PictureConfig.SINGLE)
//                .previewVideo(true)
//                .videoMinSecond(3)
//                .videoMaxSecond(30)
//                .isCamera(false)
//                .isUploadVideo(true)
//                .forResult(PictureMimeType.ofVideo())
    }

    fun onClickBackDelete(v: View) {
//        createDialog(getString(R.string.string_can_delete_video), DialogInterface.OnClickListener { _, _ ->
//            if (!mShortVideoRecorder.deleteLastSection()) {
//                showShortToast(getString(R.string.string_delete_part_video_error))
//            }
//        }).show()
    }

    fun onClickNextStep(v: View) {
        if (recordNextStepBtn.isSelected) {
//            progressDialog.show()
            mShortVideoRecorder.concatSections(videoSaveListener)
        } else {
//            showShortToast(getString(R.string.string_video_time_min))
        }
    }

    private fun onSectionCountChanged(count: Int, totalTime: Long) {
        runOnUiThread {
            if (totalTime > 0) {
                openGallery.visibility = View.GONE
                recordOperation.visibility = View.VISIBLE
            } else {
                openGallery.visibility = View.VISIBLE
                recordOperation.visibility = View.GONE
            }
            record.isEnabled = count > 0
            recordNextStepBtn.isSelected = totalTime >= DEFAULT_MIN_RECORD_DURATION
        }
    }

    private fun chooseCameraFacingId(): PLCameraSetting.CAMERA_FACING_ID {
        return when {
            PLCameraSetting.hasCameraFacing(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD) -> PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD
            PLCameraSetting.hasCameraFacing(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT) -> PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT
            else -> PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK
        }
    }

    private val recordStateListener = object : PLRecordStateListener {
        override fun onDurationTooShort() {
            runOnUiThread {
//                showShortToast(getString(R.string.string_delete_part_video_error))
            }
        }

        override fun onReady() {
            runOnUiThread {
                record.isEnabled = true
            }
        }

        override fun onRecordStarted() {
            runOnUiThread {
                recordProgressbar.setCurrentState(SectionProgressBar.State.START)
                updateRecordingBtns(true)
            }
        }

        override fun onRecordCompleted() {
            runOnUiThread {
                mShortVideoRecorder.endSection()
                updateRecordingBtns(false)
                recordNextStepBtn.isSelected = true
                onClickNextStep(preview)
            }
        }

        override fun onError(p0: Int) {
            runOnUiThread { 
//                showShortToast(toastErrorCode(p0)) 
            }
        }

        override fun onSectionRecording(p0: Long, p1: Long, p2: Int) {
        }

        override fun onRecordStopped() {
            runOnUiThread {
                updateRecordingBtns(false)
            }
        }

        override fun onSectionIncreased(incDuration: Long, totalDuration: Long, sectionCount: Int) {
            mTotalDuration = totalDuration
            recordProgressbar.addBreakPointTime(totalDuration)
            recordProgressbar.setCurrentState(SectionProgressBar.State.PAUSE)
            onSectionCountChanged(sectionCount, totalDuration)
        }

        override fun onSectionDecreased(decDuration: Long, totalDuration: Long, sectionCount: Int) {
            mTotalDuration = totalDuration
            recordProgressbar.removeLastBreakPoint()
            onSectionCountChanged(sectionCount, totalDuration)
        }
    }

    private var mTotalDuration = 0L

    private val focusListener = object : PLFocusListener {
        override fun onAutoFocusStop() {
        }

        override fun onManualFocusStop(p0: Boolean) {
            if (p0) {
                mFocusIndicator.focusSuccess()
            } else {
                mFocusIndicator.focusFail()
            }
        }

        override fun onAutoFocusStart() {
        }

        override fun onManualFocusStart(p0: Boolean) {
            if (p0) {
                val lp = mFocusIndicator.layoutParams as FrameLayout.LayoutParams
                lp.leftMargin = mFocusIndicatorX
                lp.topMargin = mFocusIndicatorY
                mFocusIndicator.layoutParams = lp
                mFocusIndicator.focus()
            } else {
                mFocusIndicator.focusCancel()
            }
        }

        override fun onManualFocusCancel() {
            mFocusIndicator.focusCancel()
        }
    }

    private val videoSaveListener = object : PLVideoSaveListener {
        override fun onSaveVideoSuccess(filePath: String) {
            runOnUiThread {
//                progressDialog.dismiss()
                VideoEditActivity.start(this@VideoRecordActivity, filePath, model)
            }
        }

        override fun onSaveVideoFailed(p0: Int) {
            runOnUiThread {
//                progressDialog.dismiss()
//                showShortToast("拼接视频段失败: $p0")
            }
        }

        override fun onProgressUpdate(p0: Float) {
//      runOnUiThread { progressDialog.setProgress((100 * percentage).toInt()) }
        }

        override fun onSaveVideoCanceled() {
//            progressDialog.dismiss()
        }
    }

    private val recordButtonListener = object : RecordButton.OnRecordStateChangedListener {

        override fun onRecordStart() {
            if (mTotalDuration < DEFAULT_MAX_RECORD_DURATION) {
                if (!mShortVideoRecorder.beginSection()) {
                    record.reset()
                    mShortVideoRecorder.endSection()
//          showShortToast("无法开始视频段录制")
                }
            } else {
                record.reset()
//                showShortToast(getString(R.string.string_video_record_max))
            }
        }

        override fun onRecordStop() {
            mShortVideoRecorder.endSection()
        }

        override fun onZoom(percentage: Float) {

        }
    }

    private val mGestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            mFocusIndicatorX = e.x.toInt() - mFocusIndicator.width / 2
            mFocusIndicatorY = e.y.toInt() - mFocusIndicator.height / 2
            mShortVideoRecorder.manualFocus(mFocusIndicator.width, mFocusIndicator.height, e.x.toInt(), e.y.toInt())
            return false
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (isRecording) return false
            if (e1 == null || e2 == null) return false
            if (Math.abs(e1.x - e2.x) < Math.abs(e1.y - e2.y)) {
                return false
            }
            if (e1.x - e2.x > 80) {
                //向左滑动
                nextFilter()
                return true
            } else if (e1.x - e2.x < -80) {
                //向右滑动
                prevFilter()
                return true
            }
            return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == PictureMimeType.ofVideo()) {
//            var result = PictureSelector.obtainMultipleResult(data)
//            if (result.size != 0) {
//                val localMedia = result[0]
//                VideoEditActivity.start(this@VideoRecordActivity, localMedia.compressPath, model)
//            }
//        }
    }

    fun toastErrorCode(errorCode: Int): String {
        return when (errorCode) {
            ERROR_SETUP_CAMERA_FAILED -> getString(R.string.string_camera_configuration_error)
            ERROR_SETUP_MICROPHONE_FAILED -> getString(R.string.string_mic_configuration_error)
            ERROR_NO_VIDEO_TRACK -> getString(R.string.string_file_info_error)
            ERROR_SRC_DST_SAME_FILE_PATH -> getString(R.string.string_path_same_error)
            ERROR_MULTI_CODEC_WRONG -> getString(R.string.string_device_not_support_error)
            ERROR_SETUP_VIDEO_ENCODER_FAILED -> getString(R.string.string_encode_start_error)
            ERROR_SETUP_VIDEO_DECODER_FAILED -> getString(R.string.string_decode_start_error)
            ERROR_SETUP_AUDIO_ENCODER_FAILED -> getString(R.string.string_audio_encode_start_error)
            ERROR_LOW_MEMORY -> getString(R.string.string_storage_min_error)
            ERROR_MUXER_START_FAILED -> getString(R.string.string_muxer_start_error)
            else -> "错误码： $errorCode"
        }
    }

    private fun getFirstVideo() {
        val proj = arrayOf(MediaStore.Video.Thumbnails._ID,
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_MODIFIED)

        supportLoaderManager.initLoader(0, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> { //创建了一个CursorLoader，查询的数据有文件地址，名次，添加时间，id并且按照添加时间排序
                return CursorLoader(this@VideoRecordActivity, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        proj, MediaStore.Video.Media.MIME_TYPE + "=?", arrayOf("video/mp4"), MediaStore.Video.Media.DATE_MODIFIED + " desc")
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                if (data != null) {
                    val count = data.count
                    if (count <= 0) {
                        return
                    }
                    data.moveToFirst()
                    val videoId = data.getInt(data.getColumnIndex(MediaStore.Video.Media._ID))

                    Glide.with(openGalleryImage.context).load(MediaStore.Video.Thumbnails.getThumbnail(contentResolver,
                            videoId.toLong(), MediaStore.Video.Thumbnails.MICRO_KIND, null))
                            .apply(
                                    RequestOptions().transform(RoundedCornersTransformation(ScreenUtils.dip2px(openGalleryImage.context, 5f), 0, RoundedCornersTransformation.CornerType.ALL)))
                            .into(openGalleryImage)
                }
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        })
    }
}
