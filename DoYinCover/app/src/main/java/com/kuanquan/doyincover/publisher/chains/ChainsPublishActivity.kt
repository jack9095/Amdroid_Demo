package com.kuanquan.doyincover.publisher.chains

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.qiniu.pili.droid.shortvideo.PLMediaFile
import com.shuashuakan.android.R
import com.shuashuakan.android.data.RxBus
import com.shuashuakan.android.data.api.model.FeedChannel
import com.shuashuakan.android.data.api.services.ApiService
import com.shuashuakan.android.event.EditVideoSuccessEvent
import com.shuashuakan.android.event.PublishGoHomeEvent
import com.shuashuakan.android.exts.applySchedulers
import com.shuashuakan.android.exts.subscribeApi
import com.shuashuakan.android.modules.publisher.RecordDataModel
import com.shuashuakan.android.modules.publisher.VideoEditActivity
import com.shuashuakan.android.modules.topic.TopicCategoryActivity
import com.shuashuakan.android.modules.widget.dialogs.DownloadProgressDialog
import com.shuashuakan.android.modules.widget.transform.RoundedCornersTransformation
import com.shuashuakan.android.service.PullService
import com.shuashuakan.android.ui.ProgressDialog
import com.shuashuakan.android.ui.base.FishActivity
import com.shuashuakan.android.utils.*
import com.kuanquan.doyincover.utils.download.DownloadListener
import com.kuanquan.doyincover.utils.download.DownloadManager
import com.kuanquan.doyincover.utils.toastCustomLayout
import com.kuanquan.doyincover.utils.toastCustomText
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
//接龙视频发布
class ChainsPublishActivity : FishActivity(), DownloadListener {

  private val maxLen = 60
  private val content by bindView<EditText>(R.id.home_container)
  private val imageView by bindView<ImageView>(R.id.image_view)
  private val changeCoverRL by bindView<RelativeLayout>(R.id.chains_publish_change_cover_rl)
  private val publish by bindView<TextView>(R.id.publish)
  private val back by bindView<ImageView>(R.id.back)
  private val backTextView by bindView<TextView>(R.id.back_tv)
  private val selectTopicBtn by bindView<TextView>(R.id.select_topic_btn)
  private val emojiLayout by bindView<LinearLayout>(R.id.publish_emoji_layout)
  private val emojiList = intArrayOf(0x1F602, 0x1F61A, 0x1F64C, 0x1F525, 0x26FD, 0x1F60D, 0x1F630, 0x1F621)
  private var channelModel: FeedChannel? = null
  private lateinit var recordDataModel: RecordDataModel
  private lateinit var videoId: String
  private lateinit var videoUrl: String
  private lateinit var title: String
  private var canEdit: Boolean = false
  private var editCount: Int = -1
  private lateinit var downloadManager: DownloadManager

  private var changeedCorver: Int = -2
  private var mPath = ""

  private val progressDialog by lazy {
    return@lazy ProgressDialog.progressDialog(this, getString(com.shuashuakan.android.base.ui.R.string.string_loading), false)
  }

  @Inject
  lateinit var apiService: ApiService

  companion object {
    const val CHANNEL_RESULT_CODE = 100
    private const val CHANNEL_RESULT_CODE_VIDEOEDITED = 101
    const val RECORD_MODEL = "record_model"
    const val RECORD_VIDEO_ID = "record_video_id"
    const val RECORD_VIDEO_URL = "record_video_url"
    const val RECORD_VIDEO_TITLE = "record_video_title"
    const val CAN_EDIT = "can_edit"
    const val EDIT_COUNT = "edit_count"

    fun create(context: Context, model: RecordDataModel,
               videoId: String = "", videoUrl: String = "", title: String = "",
               canEdit: Boolean = false, editCount: Int = -1): Intent {
      return Intent(context, ChainsPublishActivity::class.java)
          .putExtra(RECORD_MODEL, model)
          .putExtra(RECORD_VIDEO_ID, videoId)
          .putExtra(RECORD_VIDEO_URL, videoUrl)
          .putExtra(RECORD_VIDEO_TITLE, title)
          .putExtra(CAN_EDIT, canEdit)
          .putExtra(EDIT_COUNT, editCount)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chains_publish)
    getExtra()
    initListener()
    initEmojiLayout()
    emojiLayout.visibility = View.GONE
  }

  @SuppressLint("CheckResult")
  private fun getExtra() {
    recordDataModel = intent.extras.getParcelable(RECORD_MODEL)
    videoId = intent.extras.getString(RECORD_VIDEO_ID)
    videoUrl = intent.extras.getString(RECORD_VIDEO_URL)
    title = intent.extras.getString(RECORD_VIDEO_TITLE)
    canEdit = intent.extras.getBoolean(CAN_EDIT)
    editCount = intent.extras.getInt(EDIT_COUNT)

    if (!recordDataModel.channelName.isNullOrEmpty()) {
      selectTopicBtn.text = recordDataModel.channelName
      selectTopicBtn.isEnabled = recordDataModel.channelName == null
    }

    if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_SOLITAIRE) {
      selectTopicBtn.visibility = View.GONE
    }

    if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
      content.setText(title)
      content.setSelection(title.length)
      selectTopicBtn.isEnabled = true
      publish.text = getString(R.string.string_confirm_change)
      changeCoverRL.visibility = View.VISIBLE
      imageView.isEnabled = true

      val drawable = resources.getDrawable(R.drawable.ic_channel_disable)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        selectTopicBtn.setCompoundDrawables(drawable, null, null, null)
    } else {
      showShortToast(getString(R.string.string_save_photo_success))
      imageView.isEnabled = false
    }

    if (recordDataModel.coverDuration == null
        || recordDataModel.filePath == null
        || recordDataModel.coverDuration == 0) {
      Glide.with(imageView.context).load(recordDataModel.filePath).apply(getRequestOptions()).into(imageView)
    } else {
      showCoverImageView(recordDataModel.filePath!!, recordDataModel.coverDuration!!, imageView)
    }
  }

  private fun initListener() {
    publish.setOnClickListener {
      doPublish()
    }
    back.setOnClickListener {
      onBackPressed()
    }
    backTextView.setOnClickListener {
      onBackPressed()
    }
    selectTopicBtn.noDoubleClick {
      if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
        toastCustomText(this, getString(com.shuashuakan.android.base.ui.R.string.string_not_change_topic_error))
      } else {
        TopicCategoryActivity.launchForResult(this, CHANNEL_RESULT_CODE,channelModel)
      }
    }
    imageView.noDoubleClick {
      downVideo()
    }
    content.viewTreeObserver.addOnGlobalLayoutListener {
      val r = Rect()
      this@ChainsPublishActivity.window.decorView.getWindowVisibleDisplayFrame(r)
      val screenHeight = this@ChainsPublishActivity.window.decorView.rootView.height
      val heightDifference = screenHeight - r.bottom
      if (heightDifference > dip(200)) {
        emojiLayout.visibility = View.VISIBLE
      } else {
        emojiLayout.visibility = View.GONE
      }
    }
    content.addTextChangedListener(object : TextWatcher {

      override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

      }

      override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        var editable = content.text
        val len = Strings.getTextLength(content.text.toString())
        if (len > maxLen) {
          Toast.makeText(this@ChainsPublishActivity, getString(R.string.string_beyound_length), Toast.LENGTH_SHORT).show()
          var selEndIndex = Selection.getSelectionEnd(editable)
          val str = editable.toString()
          //截取新字符串
          val newStr = str.substring(0, maxLen)
          content.setText(newStr)
          editable = content.text

          //新字符串的长度
          val newLen = editable.length
          //旧光标位置超过字符串长度
          if (selEndIndex > newLen) {
            selEndIndex = editable.length
          }
          //设置新光标所在的位置
          Selection.setSelection(editable, selEndIndex)
        }
        if (editable.toString().isEmpty()
            || (editable.toString() == title && (changeedCorver == -2 || changeedCorver == -1))) { // 封面没有修改 // 标题没有修改
          publish.alpha = 0.5f
        } else {
          publish.alpha = 1f
        }
      }

      override fun afterTextChanged(editable: Editable) {
      }
    })
  }

  override fun onBackPressed() {
    doBack()
  }

  private fun doBack() {
    AlertDialog.Builder(this)
        .setMessage(getString(R.string.is_back_chains_publish))
        .setPositiveButton(getString(com.shuashuakan.android.base.ui.R.string.string_confirm)) { dialog, _ ->
          dialog.dismiss()
          val deleteFile = FileUtil.deleteCatchFile(recordDataModel.filePath, this)
          if (deleteFile){
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(recordDataModel.filePath))))
          }
          if (!mPath.isEmpty()) {
            FileUtil.deleteFile(mPath)
          }
          hideKeyboard()
          finish()
        }
        .setNegativeButton(getString(R.string.string_cancel)) { dialog, _ ->
          dialog.dismiss()
          hideKeyboard()
        }
        .show()
  }

  private fun doPublish() {
    if (content.text.toString().isEmpty()) {
      showLongToast(getString(R.string.string_no_have_video_title_error))
      return
    }
    if (recordDataModel.recordType != PullService.UploadEntity.TYPE_ADD_SOLITAIRE) {
      if (selectTopicBtn.text == getString(R.string.string_choose_topic)) {
        showLongToast(getString(R.string.string_can_not_find_vieo_error))
        return
      }
    }
    if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_SOLITAIRE) {
      PullService.uploadSolitaire(this, recordDataModel.filePath!!, "video",
          content.text.toString(), recordDataModel.coverDuration, recordDataModel.masterFeedId)
      selectTopicBtn.visibility = View.GONE
      hideKeyboard()
      finish()
    } else if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_HOME_VIDEO) {
      PullService.uploadHomeVideo(this, recordDataModel.filePath!!, "video",
          content.text.toString(), "",
          if (!recordDataModel.channelId.isNullOrEmpty()) recordDataModel.channelId!! else channelModel?.id.toString(), recordDataModel.coverDuration,
          PullService.UploadEntity.TYPE_ADD_HOME_VIDEO)
      RxBus.get().post(PublishGoHomeEvent())
      hideKeyboard()
      finish()
    } else if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_CHANNEL_VIDEO) {
      PullService.uploadHomeVideo(this, recordDataModel.filePath!!, "video",
          content.text.toString(), "",
          if (!recordDataModel.channelId.isNullOrEmpty()) recordDataModel.channelId!! else channelModel?.id.toString(), recordDataModel.coverDuration,
          PullService.UploadEntity.TYPE_ADD_CHANNEL_VIDEO)
      hideKeyboard()
      finish()
    } else if (recordDataModel.recordType == PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO) {
      if ((changeedCorver == -2 || changeedCorver == -1) && content.text.toString() == title) {
        toastCustomText(this, getString(R.string.string_not_change_content_error))
        return
      }
      var videoCoverImageView: Double? = null
      if (changeedCorver != -2 && changeedCorver != -1) { // 封面已修改
        videoCoverImageView = changeedCorver / 1000.toDouble()
      }
      progressDialog.show()
      apiService.updateVideoInfo(videoId, content.text.toString(), videoCoverImageView)
          .applySchedulers()
          .subscribeApi(onNext = {
            Timber.e("subscribeApi")
            toastCustomLayout(this, R.layout.layout_dialog_extend)
            RxBus.get().post(EditVideoSuccessEvent(videoId, content.text.toString(),
                if(it.coverUrl != null) it.coverUrl else "", canEdit, editCount))
            if (!mPath.isEmpty()) {
              FileUtil.deleteFile(mPath)
            }
            progressDialog.dismiss()
            hideKeyboard()
            finish()
          }, onApiError = {
            toastCustomText(this, getString(R.string.string_change_error))
            progressDialog.dismiss()
            hideKeyboard()
          })
    }
  }

  private fun initEmojiLayout() {
    for (i in 0..7) {
      val textView = TextView(this)
      textView.text = String(Character.toChars(emojiList[i]))
      textView.setTextColor(resources.getColor(R.color.comment_content_color))
      textView.gravity = Gravity.CENTER
      textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
      textView.setOnClickListener { v ->
        content.append(textView.text.toString())
      }
      emojiLayout.addView(textView)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CHANNEL_RESULT_CODE) {
      if (resultCode == Activity.RESULT_OK) {
        channelModel = data?.getParcelableExtra(TopicCategoryActivity.resultDataFeedChannel)
        selectTopicBtn.text = channelModel?.name
      }
    } else if (requestCode == CHANNEL_RESULT_CODE_VIDEOEDITED) {
      if (resultCode == Activity.RESULT_OK) {
        // 更新视频封面
        val modelCorver = data?.getIntExtra("model", 0) ?: 0
        changeedCorver = modelCorver
        if ((changeedCorver == -2 || changeedCorver == -1)) { // 封面没有修改
          if (content.text.toString() == title) { // 且标题没有修改
            publish.alpha = 0.5f
          } else {
            publish.alpha = 1f
          }
        } else {
          showCoverImageView(mPath, changeedCorver, imageView)
          publish.alpha = 1f
        }
      }
    }
  }

  private fun showCoverImageView(path: String, corverTime: Int, imageView: ImageView) {
    if (path.isEmpty()) return
    val fileVideo = PLMediaFile(path)
    val videoFrameByTime = fileVideo.getVideoFrameByTime(corverTime.toLong(), false)
    videoFrameByTime?.let {
      Glide.with(imageView.context).load(it.toBitmap()).apply(getRequestOptions()).into(imageView)
    }

  }

  @SuppressLint("CheckResult")
  fun getRequestOptions(): RequestOptions {
    val requestOptions = RequestOptions()
    val transform = RoundedCornersTransformation(dip(8), 0, RoundedCornersTransformation.CornerType.ALL)
    requestOptions.placeholder(R.drawable.ic_block_video)
        .error(R.drawable.ic_block_video)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .transforms(CenterCrop(), transform)
    return requestOptions
  }


  private fun downVideo() {
    val urlSplit = videoUrl.split("/")
    val lastUrlSplit = urlSplit[urlSplit.size - 1]
    val cacheDir = cacheDir
    val path = Environment.getExternalStorageDirectory().path
    mPath = "$cacheDir/$lastUrlSplit"
    val file = File(mPath)
    if (!file.exists()) {
      downloadManager = DownloadManager(this, mPath, videoUrl,
          DownloadProgressDialog.progressDialog(this, false),null)
      downloadManager.setDownloadListener(this)
      downloadManager.startTask()
    } else {
      VideoEditActivity.startForResult(this, mPath, RecordDataModel(
              "", null, PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO,
              null, null, null), CHANNEL_RESULT_CODE_VIDEOEDITED
      )
    }
  }

  override fun downloadProgress(percent: Int) {
  }

  override fun downloadStart() {
    progressDialog.show()
  }

  override fun downloadSucceeded(path: String) {
    progressDialog.dismiss()
    if (!path.isEmpty()) {
      VideoEditActivity.startForResult(this, path, RecordDataModel(
              "", null, PullService.UploadEntity.TYPE_ADD_EDITED_VIDEO,
              null, null, null), CHANNEL_RESULT_CODE_VIDEOEDITED
      )
    }
  }

  override fun downloadCanceled() {
    progressDialog.dismiss()
    downloadManager.cancelTask()
  }

  override fun downloadFailed() {
    progressDialog.dismiss()
    downloadManager.cancelTask()
  }
}
