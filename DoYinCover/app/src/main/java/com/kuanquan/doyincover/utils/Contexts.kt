@file:JvmName("Contexts")

package com.kuanquan.doyincover.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kuanquan.doyincover.R

//import arrow.core.Option

//fun Context.daggerComponent(): DuckComponent = DuckComponent.get(this)
//
//fun Context.getSpider(): Spider = daggerComponent().spider()
//
//fun Context.getColor1(color:Int):Int=ContextCompat.getColor(applicationContext,color)
//
//fun Context.resolve(link: String?): Option<Intent> {
//  if (link == null) return Option.empty()
//  return Option.fromNullable(daggerComponent().linkResolver().resolve(link).intent)
//}
//
//fun Context.startActivity(link: String?, block: Intent.() -> Unit = {}) {
//  Router.get(this).startActivity(this, link, block)
//}

//fun Context.startActivityForResultByLink(
//    link: String?, requestCode: Int, block: Intent.() -> Unit = {}) {
//  if (this is Activity)
//    Router.get(this).startActivityForResult(this, link, requestCode, block)
//}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

fun Context.sip(value: Float):Int =(value * (resources.displayMetrics.scaledDensity)+0.5f).toInt()

fun Context.sip(value: Int):Int =(value * (resources.displayMetrics.scaledDensity)+0.5f).toInt()

fun Context.showLongToast(msg: String?) {
  msg?.let { Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show() }
}

fun Context.showLongToast(msg: String?, gravity: Int) {
  msg?.let {
    val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_LONG)
    toast.setGravity(gravity, 0, 0)
    toast.show()
  }
}

fun Context.showShortToast(msg: String?) {
  msg?.let { Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show() }
}

fun Context.showKeyboard() {
  val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.toggleSoftInput(
      InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.hideKeyboard() {
  if (currentFocus == null) return
  val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  inputMethodManager.hideSoftInputFromWindow(
      currentFocus!!.windowToken, 0)
}

fun Context.getStatusBarHeight(): Int {
  val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
  if (resourceId > 0) {
    return resources.getDimensionPixelOffset(resourceId)
  }
  return 0
}

fun Context.getNavigationBarHeight(): Int {
  if (!isNavigationBarShow()) {
    return 0
  }
  val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
  if (resourceId > 0) {
    return resources.getDimensionPixelOffset(resourceId)
  }
  return 0
}

fun Context.isNavigationBarShow(): Boolean {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val size = Point()
    val realSize = Point()
    display.getSize(size)
    display.getRealSize(realSize)
    return realSize.y != size.y
  } else {
    val menu = ViewConfiguration.get(this).hasPermanentMenuKey()
    val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
      return !(menu || back)
  }
}

fun Context.getScreenSize(): Point {
  val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val display = windowManager.defaultDisplay
  val point = Point()
  display.getSize(point)
  return point
}

fun Context.getChangeSource(source: String?): String {
  var sour: String = "HomePage"
  when (source) {
    "home" -> sour = "HomePage"
    "channel_page" -> sour = "ChannelList"
    "explore" -> sour = "Explore"
    "fav" -> sour = "Fav"
    "like" -> sour = "Like"
    "upload" -> sour = "Publish"
    "push" -> sour = "Push"
    "h5" -> sour = "Web"
    "MessageCenter" -> sour = "MessageCenter"
    "VideoSlide" -> sour = "VideoSlide"
    "ChannelRecommend" -> sour = "ChannelRecommend"
    "ChannelTimeline" -> sour = "ChannelTimeline"
    "FollowTimeline" -> sour = "FollowTimeline"
    "PersonalPage" -> sour = "PersonalPage"
    "ExploreRankingList" -> sour = "ExploreRankingList"
    "Category" -> sour = "Category"
  }
  return sour
}

fun Context.goSettingDialog(context: Context) {
  val dialog: android.app.AlertDialog = android.app.AlertDialog.Builder(context)
      .setMessage(getString(R.string.string_not_has_camera_permission_error))
      .setPositiveButton(getString(R.string.string_string_go_to_setting)) { dialogInterface, i ->
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", packageName, null)))
        dialogInterface.cancel()
      }
      .setNegativeButton(getString(R.string.string_cancel)) { dialogInterface, i ->
        dialogInterface.cancel()
      }
      .create()
  dialog.show()
  val button = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
  if (button != null) {
    button.setTextColor(resources.getColor(R.color.black20))
  }
}

fun Context.createDialog(message: String, positiveButton: String, positiveListener: DialogInterface.OnClickListener,
                         negativeButton: String, negativeListener: DialogInterface.OnClickListener?, isCancelable: Boolean = true): AlertDialog {
  return AlertDialog.Builder(this).setMessage(message).setPositiveButton(positiveButton, positiveListener)
      .setNegativeButton(negativeButton, negativeListener).setCancelable(isCancelable).create()
}

fun Context.createDialog(message: String, positiveListener: DialogInterface.OnClickListener): AlertDialog {
  return this.createDialog(message, getString(R.string.string_confirm), positiveListener, getString(R.string.string_cancel), null)
}

/*
 * 震动效果
 */
fun Context.shockMethod(time: Long) {
  val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
  } else {
    vibrator.vibrate(time)
  }
}

//fun Context.getUserId(): Any {
//  return daggerComponent().accountManager().account()?.userId ?: ""
//}