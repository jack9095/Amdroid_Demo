package com.kuanquan.doyincover.publisher

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.flyco.roundview.RoundTextView
import com.kuanquan.doyincover.R
import com.qiniu.pili.droid.shortvideo.*
import com.kuanquan.doyincover.publisher.utils.StringUtil
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.util.*
import com.kuanquan.doyincover.base.FishActivity
import com.kuanquan.doyincover.dialog.FilterListBottomDialog
import com.kuanquan.doyincover.utils.TimeUtil

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/10
 * Description:
 */
class VideoEditActivity : FishActivity() {
  companion object {
    private const val MP4_PATH = "MP4_PATH"
    private const val IMAGE_DISK_CACHE_MAX_SIZE = 500 * 1024 * 1024
    private const val GLIDE_CACHE_DIR = "/doyin/video_cache"
    private const val NO_MEDIA = "/.nomedia"
    private const val EMPTY = ""

    fun start(context: Context, mp4Path: String, model: RecordDataModel?) {
      val intent = Intent(context, VideoEditActivity::class.java)
      intent.putExtra(MP4_PATH, mp4Path)
      intent.putExtra("model", model)
      context.startActivity(intent)
    }

    fun startForResult(context: Activity, mp4Path: String, model: RecordDataModel?, requestCode: Int){
      val intent = Intent(context, VideoEditActivity::class.java)
      intent.putExtra(MP4_PATH, mp4Path)
      intent.putExtra("model", model)
      context.startActivityForResult(intent, requestCode)
    }
  }

  private val preview by lazy { findViewById<GLSurfaceView>(R.id.preview) }
  private val editLayout by lazy { findViewById<View>(R.id.edit_layout) }
  private val choseCoverLayout by lazy { findViewById<View>(R.id.chose_cover_layout) }
  private val backBtn by lazy { findViewById<ImageView>(R.id.ic_back) }
  private val doneBtn by lazy { findViewById<RoundTextView>(R.id.done_btn) }
  private val filterBtn by lazy { findViewById<TextView>(R.id.filter_btn) }
  private val choseCoverBtn by lazy { findViewById<TextView>(R.id.chose_cover_btn) }
  private val filterNameText by lazy { findViewById<TextView>(R.id.filter_name_text) }
  private val choiceCover by lazy { findViewById<ChoiceCover>(R.id.choice_cover) }

  private var mMp4path: String? = null

  private var coverDuration: Int = 0
  private var coverDurationVideoEdit: Int = 0

  private lateinit var model: RecordDataModel

  private var mShortVideoEditorStatus = PLShortVideoEditorStatus.Idle

  private val eventCompositeDisposable = CompositeDisposable()

  // 视频编辑器预览状态
  private enum class PLShortVideoEditorStatus {
    Idle,
    Playing,
    Paused
  }

  private lateinit var mShortVideoEditor: PLShortVideoEditor
  private lateinit var mGestureDetector: GestureDetector
  private var filterList = mutableListOf<PLBuiltinFilter>()
  private val filterDialog: FilterListBottomDialog by lazy {
      FilterListBottomDialog(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setContentView(R.layout.activity_video_edit)
    getDefaultFilesDir(this)

    val url = "android.resource://" + packageName + "/" + R.raw.test
    val uri = Uri.parse(url)

    mMp4path = url

//    model = intent.getParcelableExtra("model")
    initPreviewView()
    initShortVideoEditor()
    setOther()
    initFilterList()
//    RxBus.get().toFlowable().subscribe {
//      if (it is VideoRecordFinishEvent) {
//        finish()
//      }
//    }.addTo(eventCompositeDisposable)
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun setOther() {
    mGestureDetector = GestureDetector(this, mGestureDetectorListener)
    preview.setOnTouchListener { _, motionEvent ->
      mGestureDetector.onTouchEvent(motionEvent)
      true
    }
    filterDialog.listener = object : FilterListBottomDialog.OnFilterDialogListener {
      override fun onSelectPosition(position: Int) {
        selectedPosition = position
        selectFilter(filterList[selectedPosition])
      }
    }
    filterDialog.setOnShowListener {
      doneBtn.visibility = View.GONE
      filterBtn.visibility = View.GONE
      choseCoverBtn.visibility = View.GONE

      if (filterDialog.getSelectPosition() != selectedPosition) {
        filterDialog.setFilterPosition(selectedPosition)
      }
      filterDialog.scrollFilter(selectedPosition)
    }
    filterDialog.setOnDismissListener {
      doneBtn.visibility = View.VISIBLE
      filterBtn.visibility = View.VISIBLE
      choseCoverBtn.visibility = View.VISIBLE
    }
    choiceCover.setOnScrollBorderListener(object : ChoiceCover.OnScrollBorderListener {
      override fun OnScrollBorder(start: Float, end: Float) {
        // 游标变更
        val startTIme = start.toInt()
        val result = startTIme / choiceCover.width.toFloat()
        val mCursor = (result * mShortVideoEditor.durationMs).toInt()
        mShortVideoEditor.seekTo(mCursor)
        pausePlayback()
      }

      override fun onScrollStateChange() {
        // 如果没滑动就回传给页面-1，代表没修改封面 TODO
//        if (model.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
//          coverDurationVideoEdit = -1
//        }
      }

    })
  }

  private fun initFilterList() {
//    val filters = mShortVideoEditor.builtinFilterList
//    if (filters != null) {
//      filterList.addAll(Arrays.asList(*filters))
//      val builtinFilter = PLBuiltinFilter()
//      builtinFilter.name = "none"
//      filterList.add(0, builtinFilter)
//    } else {
//      filterList = mutableListOf()
//    }
//    filterDialog.initData(filterList)
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
      mShortVideoEditor.setBuiltinFilter(plBuiltinFilter.name)
    } else {
      mShortVideoEditor.setBuiltinFilter(null)
    }
    filterNameText.text = StringUtil.getChineseName(plBuiltinFilter.name, this)
    filterNameText.visibility = View.VISIBLE
    filterNameText.removeCallbacks(runnable)
    filterNameText.postDelayed(runnable, 1000)

    filterDialog.setFilterPosition(selectedPosition)
  }

  private val runnable = Runnable {
    if (filterNameText.visibility == View.VISIBLE)
      filterNameText.visibility = View.INVISIBLE
  }

  fun onCloseChoseCover(v: View) { // TODO
//    if (model.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
//      comeBack()
//    } else {
      dismissChoseCover()
//    }
  }

  fun onSaveChoseCover(v: View) {
    coverDuration = mShortVideoEditor.currentPosition
//    if (model.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
//      // TODO 把 coverDuration 返回给ChansPublishActivity
//      model.coverDuration = coverDuration
//      model.filePath = mMp4path
//
//      if (coverDurationVideoEdit == 0) {
//        coverDurationVideoEdit = -1
//      } else {
//        coverDurationVideoEdit = model.coverDuration!!
//      }
//      setResult(Activity.RESULT_OK, Intent().putExtra("model", coverDurationVideoEdit))
//      finish()
//    } else {
      dismissChoseCover()
//    }
  }

  fun onBackClick(v: View) {
    comeBack()
  }

  fun onChoseCover(v: View) {
    showChoseCover()
  }

  override fun onBackPressed() {
//    if (model.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
//      comeBack()
//    } else {
//      if (choseCoverLayout.visibility == View.VISIBLE) {
//        dismissChoseCover()
//      } else {
//        comeBack()
//      }
//    }
    finish()
  }

  private fun comeBack() {
//    alertDialog.show()
  }

  private fun showChoseCover() {
    editLayout.visibility = View.GONE
    choseCoverLayout.visibility = View.VISIBLE

    val scalePreviewAnim = AnimatorSet()//组合动画

    val translationY = ObjectAnimator.ofFloat(preview, "translationY", 0.0f, -30.0f)

    val scaleX = ObjectAnimator.ofFloat(preview, "scaleX", 1f, 0.75f)
    val scaleY = ObjectAnimator.ofFloat(preview, "scaleY", 1f, 0.75f)

    scalePreviewAnim.duration = 500
    scalePreviewAnim.interpolator = DecelerateInterpolator()
    scalePreviewAnim.play(scaleX).with(scaleY).with(translationY)
    scalePreviewAnim.start()

    pausePlayback()
  }

  private fun dismissChoseCover() {
    choseCoverLayout.visibility = View.GONE
    editLayout.visibility = View.VISIBLE

    val translationY = ObjectAnimator.ofFloat(preview, "translationY", -30.0f, 0.0f)
    val scalePreviewAnim = AnimatorSet()//组合动画
    val scaleX = ObjectAnimator.ofFloat(preview, "scaleX", 0.75f, 1f)
    val scaleY = ObjectAnimator.ofFloat(preview, "scaleY", 0.75f, 1f)

    scalePreviewAnim.duration = 500
    scalePreviewAnim.interpolator = DecelerateInterpolator()
    scalePreviewAnim.play(scaleX).with(scaleY).with(translationY)
    scalePreviewAnim.start()

    startPlayback()
  }

  fun onFilterClick(v: View) {
    filterDialog.show()
  }

  fun onDoneClick(v: View) {
//    progressDialog.show()
    mShortVideoEditor.save()
  }

  override fun onResume() {
    super.onResume()
    if (choseCoverLayout.visibility == View.GONE) {
      startPlayback()
    }
    if (selectedPosition > 0) {
      selectFilter(filterList[selectedPosition])
    }
//    if (model.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
//      showChoseCover()
//    }
  }

  override fun onPause() {
    super.onPause()
    if (choseCoverLayout.visibility == View.GONE) {
      stopPlayback()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    eventCompositeDisposable.clear()
  }

  private fun startPlayback() {
    if (mShortVideoEditorStatus == PLShortVideoEditorStatus.Idle) {
      mShortVideoEditor.startPlayback(object : PLVideoFilterListener {
        override fun onSurfaceCreated() {

        }

        override fun onSurfaceChanged(width: Int, height: Int) {

        }

        override fun onSurfaceDestroy() {

        }

        override fun onDrawFrame(texId: Int, texWidth: Int, texHeight: Int, timestampNs: Long, transformMatrix: FloatArray): Int {
          return texId
        }
      })
      mShortVideoEditorStatus = PLShortVideoEditorStatus.Playing
    } else if (mShortVideoEditorStatus == PLShortVideoEditorStatus.Paused) {
      mShortVideoEditor.resumePlayback()
      mShortVideoEditorStatus = PLShortVideoEditorStatus.Playing
    }
  }

  private fun stopPlayback() {
    mShortVideoEditor.stopPlayback()
    mShortVideoEditorStatus = PLShortVideoEditorStatus.Idle
  }

  private fun pausePlayback() {
    mShortVideoEditor.pausePlayback()
    mShortVideoEditorStatus = PLShortVideoEditorStatus.Paused
  }

  private fun initPreviewView() {

  }

  private fun initShortVideoEditor() {
//    mMp4path = intent.getStringExtra(MP4_PATH)

    mMp4path = "https://200024424.vod.myqcloud.com/200024424_709ae516bdf811e6ad39991f76a4df69.f20.mp4"

//    choiceCover.setVideoPath(mMp4path)

    val setting = PLVideoEditSetting()
    setting.sourceFilepath = mMp4path
//    setting.destFilepath = getDefaultFilesDir(this) + GLIDE_CACHE_DIR
    setting.destFilepath = "${Environment.getExternalStorageDirectory()}/DCIM/" +
        "Camera/SSR${TimeUtil.getCurrentTime("yyyyMMddHHmmss")}.mp4"
    setting.isKeepOriginFile = false

//    mShortVideoEditor = PLShortVideoEditor(preview, setting)
//    mShortVideoEditor.setVideoSaveListener(onSaveListener)

  }

  private val onSaveListener = object : PLVideoSaveListener {
    override fun onSaveVideoCanceled() {
//      progressDialog.dismiss()
    }

    override fun onProgressUpdate(p0: Float) {
    }

    override fun onSaveVideoSuccess(p0: String) {
//      progressDialog.dismiss()
      Log.e("wangfei" ,p0)
//      RxBus.get().post(VideoRecordFinishEvent())
//      model.coverDuration = coverDuration
//      model.filePath = p0

      // 通知图库更新
      sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(p0))))
//      startActivity(ChainsPublishActivity.create(this@VideoEditActivity, model))
    }

    override fun onSaveVideoFailed(p0: Int) {
//      progressDialog.dismiss()
    }
  }

  private val mGestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
      if (choseCoverLayout.visibility == View.VISIBLE) return false
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


  /**
   * 获取应用私有目录
   */
  private fun getDefaultFilesDir(context: Context): String? {
    var fileDir = context.getExternalFilesDir(EMPTY)
    if (fileDir == null) {
      ContextCompat.getExternalFilesDirs(context, null)
      fileDir = context.getExternalFilesDir(EMPTY)
      if (fileDir == null) {
        fileDir = context.filesDir
      }
    }
    val file = File(fileDir?.absolutePath + NO_MEDIA)
    if (!file.exists()) {
      try {
        file.createNewFile()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    return fileDir?.absolutePath
  }
}
