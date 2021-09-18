//package com.shuashuakan.android.modules.publisher
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Dialog
//import android.content.DialogInterface
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.Settings
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AlertDialog
//import android.support.v7.app.AppCompatDialogFragment
//import android.view.Gravity
//import android.view.View
//import android.view.Window
//import android.view.WindowManager
//import android.widget.ImageView
//import android.widget.TextView
//import com.kuanquan.doyincover.publisher.VideoRecordActivity
//import com.kuanquan.doyincover.utils.checkPhoneRom
//import com.luck.picture.lib.tools.ToastManage
//import com.shuashuakan.android.R
//import com.shuashuakan.android.commons.util.ACache
//import com.shuashuakan.android.config.AppConfig
//import com.shuashuakan.android.data.RxBus
//import com.shuashuakan.android.event.BindSuccessEvent
//import com.shuashuakan.android.event.EventPermissionRequestFragmentDismiss
//import com.shuashuakan.android.service.PullService
//import com.shuashuakan.android.modules.publisher.permission.CameraTest
//import com.shuashuakan.android.modules.publisher.permission.RecordAudioTest
//import com.shuashuakan.android.modules.publisher.permission.StorageReadTest
//import com.shuashuakan.android.modules.publisher.permission.StorageWriteTest
//import com.shuashuakan.android.modules.widget.dialogs.BindPhoneDialog
//import com.shuashuakan.android.utils.*
//import com.tbruyelle.rxpermissions2.RxPermissions
//import io.reactivex.disposables.CompositeDisposable
//import timber.log.Timber
//
//
//@Suppress("DEPRECATION")
///**
// * Author:  lijie
// * Date:   2018/12/7
// * Email:  2607401801@qq.com
// */
//class PermissionRequestFragment : AppCompatDialogFragment(), View.OnClickListener {
//  private val arrayOfPermissions = arrayOf(
//      Manifest.permission.CAMERA,
//      Manifest.permission.RECORD_AUDIO,
//      Manifest.permission.READ_EXTERNAL_STORAGE)
//
//  private val closeIv by bindView<ImageView>(R.id.per_close_iv)
//  private val requestBtn by bindView<TextView>(R.id.per_request_btn)
//
//  private val cameraPer by bindView<TextView>(R.id.per_camera)
//  private val voicePer by bindView<TextView>(R.id.per_voice)
//  private val albumPer by bindView<TextView>(R.id.per_album)
//
//  lateinit var arrayPers: Array<TextView>
//  var dialog: AlertDialog? = null
//  private var goOtherPage: Boolean = true
//  private var type: Int? = 0
//  private var masterId: String? = null
//  private var channelId: String? = null
//  private var channelName: String? = null
//  private val compositeDisposable = CompositeDisposable()
//  private var source: Int = 0
//
//  companion object {
//    const val CHANNEL_ID="channel_id"
//    const val CHANNEL_NAME="channel_name"
//    const val MASTER_ID="masterId"
//    const val TYPE="type"
//
//    fun create(type: Int, masterId: String? = null,channelId:String?="",channelName:String?=""): PermissionRequestFragment {
//      val fragment = PermissionRequestFragment()
//      val bundle = Bundle()
//      bundle.putInt(TYPE, type)
//      bundle.putString(MASTER_ID, masterId)
//      bundle.putString(CHANNEL_ID,channelId)
//      bundle.putString(CHANNEL_NAME,channelName)
//      fragment.arguments = bundle
//      return fragment
//    }
//  }
//
//  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//    val inflater = activity!!.layoutInflater
//    val view = inflater.inflate(R.layout.fragment_permission_request, null)
//    val dialog = Dialog(activity!!, R.style.CommentDialogTheme)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setContentView(view)
//    dialog.setCanceledOnTouchOutside(true)
//    // 设置宽度为屏宽、位置靠近屏幕底部
//    val window = dialog.window
//    window!!.setBackgroundDrawableResource(R.color.transparent)
//    val wlp = window.attributes
//    wlp.gravity = Gravity.BOTTOM
//    wlp.width = WindowManager.LayoutParams.MATCH_PARENT
//    wlp.height = WindowManager.LayoutParams.MATCH_PARENT
//    window.attributes = wlp
//    return dialog
//  }
//
//  override fun onActivityCreated(savedInstanceState: Bundle?) {
//    super.onActivityCreated(savedInstanceState)
//    arrayPers = arrayOf(
//        cameraPer, voicePer, albumPer
//    )
//    type = arguments?.getInt(TYPE)
//    masterId = arguments?.getString(MASTER_ID)
//    channelId=arguments?.getString(CHANNEL_ID)
//    channelName=arguments?.getString(CHANNEL_NAME)
//    registerEvent()
//    initView()
//    initListener()
//  }
//
//  private fun registerEvent() {
//    RxBus.get().toFlowable().subscribe {
//      if (it is BindSuccessEvent) {
//        if (it.type == BindPhoneDialog.HOME_CENTER || it.type == BindPhoneDialog.CHANNEL_DETAIL) {
//          VideoRecordActivity.start(requireContext(), type!!, masterId, channelId, channelName)
//        }
//      }
//    }.addTo(compositeDisposable)
//  }
//
//  override fun onDestroy() {
//    super.onDestroy()
//    compositeDisposable.clear()
//  }
//
//  private fun initView() {
//    for ((index, value) in arrayOfPermissions.withIndex()) {
//      val granted = RxPermissions(this).isGranted(value)
//      if (granted) {
//        setPermissionStatus(value)
//      }
//    }
//    checkIfAll()
//  }
//
//  private fun setPermissionStatus(value: String) {
//    for ((index, item) in arrayOfPermissions.withIndex()) {
//      if (item == value) {
//        arrayPers[index].setTextColor(context!!.getColor1(R.color.color_normal_d8d8d8))
//        val drawable = resources.getDrawable(R.drawable.pop_ic_finish)
//        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
//        arrayPers[index].setCompoundDrawables(null, null, drawable, null)
//        arrayPers[index].setPadding(requireActivity().dip(22), 0, 0, 0)
//      }
//    }
//  }
//
//  private fun initListener() {
//    closeIv.setOnClickListener(this)
//    requestBtn.setOnClickListener(this)
//  }
//
//  override fun onClick(v: View) {
//    when (v.id) {
//      R.id.per_close_iv -> dismiss()
//      R.id.per_request_btn -> goRequestPermission()
//    }
//  }
//
//  @SuppressLint("CheckResult")
//  private fun goRequestPermission() {
//    RxPermissions(this).requestEach(*arrayOfPermissions)
//        .subscribe { permission ->
//          when {
//            permission.granted -> {
//              //用户同意该权限
//              for (per in arrayOfPermissions) {
//                if (permission.name == per) {
//                  setPermissionStatus(per)
//                }
//              }
//              checkIfAll()
//            }
//            permission.shouldShowRequestPermissionRationale -> {
//              //用户拒绝，没有选中不再询问
//            }
//            else -> {
//              //用户拒绝，并选中不再询问
//              setGoSettingDialog()
//            }
//          }
//
//        }
//  }
//
//  private fun setGoSettingDialog() {
//    if (dialog == null) {
//      dialog = requireContext().createDialog(getString(R.string.string_not_has_camera_permission_error), getString(R.string.string_string_go_to_setting),
//          DialogInterface.OnClickListener { _, _ ->
//            startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                .setData(Uri.fromParts("package", requireActivity().packageName, null)), 100)
//          }, getString(R.string.string_cancel), null)
//      dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black20))
//    }
//    if (!dialog!!.isShowing) {
//      dialog?.show()
//    }
//  }
//
//  private fun checkIfAll() {
//    var tempFlag = true
//    for (item in arrayOfPermissions) {
//      if (!RxPermissions(this).isGranted(item)) {
//        tempFlag = false
//      }
//    }
//    if (tempFlag) {
//      if (goOtherPage) {
//        if (type != null) {
//
//          if (checkPhoneRom("ro.smartisan.version") // 锤子手机检查权限
//              || (Build.MANUFACTURER == "QIKU" || Build.MANUFACTURER == "360")) {
//            val permissionDenied = getSpecialPhoneDenifedPermission()
//            if (permissionDenied.isNotEmpty()) {
//              ToastManage.s(requireContext(), "请开启 $permissionDenied 权限")
//            }else {
//              goOther()
//              goOtherPage = false
//            }
//          } else {
//            goOther()
//            goOtherPage = false
//          }
//          dismiss()
//        }
//      }
//    }
//  }
//
//  override fun dismiss() {
//    super.dismiss()
//    RxBus.get().post(EventPermissionRequestFragmentDismiss())
//  }
//
//  private fun goOther() {
//    var bindType: String = BindPhoneDialog.HOME_CENTER
//    if (type == PullService.UploadEntity.TYPE_ADD_HOME_VIDEO) {
//      bindType = BindPhoneDialog.HOME_CENTER
//    } else if (type == PullService.UploadEntity.TYPE_ADD_CHANNEL_VIDEO) {
//      bindType = BindPhoneDialog.CHANNEL_DETAIL
//    }
//    if (SpUtil.find(AppConfig.LOGIN_TYPE) == "WeChat" && SpUtil.find(AppConfig.PHONE_NUM).isNullOrEmpty()
//        && ACache.get(requireActivity()).getAsString(AppConfig.SHOE_BIND_PHONE) != "show") {
//      BindPhoneDialog.create(bindType).show(requireActivity().supportFragmentManager, "all_chain")
//      ACache.get(requireActivity()).put(AppConfig.SHOE_BIND_PHONE,"show",AppConfig.BIND_DIALOG_SAVE_TIME)
//    }else {
//      Timber.e("====channel==$channelName=====$channelId")
//      VideoRecordActivity.start(requireContext(), type!!, masterId, channelId, channelName)
//      ACache.get(requireActivity()).put(AppConfig.SHOE_BIND_PHONE, "show", AppConfig.BIND_DIALOG_SAVE_TIME)
//    }
//
//  }
//
//  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    initView()
//  }
//
//  private fun getSpecialPhoneDenifedPermission(): String {
//    val cameraTest = CameraTest(requireContext())
//    val recordAudioTest = RecordAudioTest(requireContext())
//    val storageReadTest = StorageReadTest()
//    val storageWriteTest = StorageWriteTest()
//    val stringBuilder = StringBuilder()
//    stringBuilder.append("")
//    if (!cameraTest.test()) stringBuilder.append(getString(R.string.srting_camera_label))
//    if (!recordAudioTest.test()) stringBuilder.append(getString(R.string.string_audio_label))
//    if (!storageReadTest.test() || !storageWriteTest.test()) stringBuilder.append(getString(R.string.string_access_file_label))
//    return stringBuilder.toString()
//  }
//}
//
//fun isStoragePermission(): Boolean {
//  val storageReadTest = StorageReadTest()
//  val storageWriteTest = StorageWriteTest()
//  return (!storageReadTest.test() || !storageWriteTest.test())
//}