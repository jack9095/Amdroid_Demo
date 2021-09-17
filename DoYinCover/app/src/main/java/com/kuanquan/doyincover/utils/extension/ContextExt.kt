package com.kuanquan.doyincover.utils.extension

import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.kuanquan.doyincover.R
import com.kuanquan.doyincover.utils.dip

fun Context.showLongToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Context.showShortToast(@StringRes resId: Int) {

    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.showTopToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val contentView = LayoutInflater.from(this).inflate(R.layout.view_custom_toast, null, false)
    val tvMsg = contentView.findViewById<TextView>(R.id.tv_msg)
    tvMsg.setText(resId)
    val toast = Toast(this)
    toast.setGravity(Gravity.TOP, 0, dip(80))
    toast.view = contentView
    toast.duration = duration
    toast.show()
}

fun Context.screenWidth() = resources.displayMetrics.widthPixels

fun Context.screenHeight() = resources.displayMetrics.heightPixels

fun Context.sharedPreferences(name: String = packageName): SharedPreferences {
    return getSharedPreferences(name, Context.MODE_PRIVATE)
}

fun Context.showCancelSubscribeDialog(topicName: String?,
                                      onPositiveClick: () -> Any?,
                                      onNegativeClick: (() -> Any?)? = null) {

    val msg = String.format(getString(R.string.string_dialog_cancel_subscribe_desc), topicName
            ?: "")
    AlertDialog.Builder(this)
            .setTitle(R.string.string_dialog_cancel_subscribe_title)
            .setMessage(msg)
            .setPositiveButton(R.string.string_dialog_cancel_subscribe_positive) { dialog, _ ->
                dialog.dismiss()
                onPositiveClick()
            }
            .setNegativeButton(R.string.string_dialog_cancel_subscribe_negative) { dialog, _ ->
                dialog.dismiss()
                onNegativeClick?.invoke()
            }
            .show()
}

fun Context.showCancelFollowDialog(userName: String?,
                                   onPositiveClick: () -> Any?,
                                   onNegativeClick: (() -> Any?)? = null) {

    val msg = String.format(getString(R.string.string_dialog_cancel_follow_desc), userName ?: "")
    AlertDialog.Builder(this)
            .setTitle(R.string.string_dialog_cancel_follow_title)
            .setMessage(msg)
            .setPositiveButton(R.string.string_dialog_cancel_follow_positive) { dialog, _ ->
                dialog.dismiss()
                onPositiveClick()
            }
            .setNegativeButton(R.string.string_dialog_cancel_follow_negative) { dialog, _ ->
                dialog.dismiss()
                onNegativeClick?.invoke()
            }
            .show()
}
