//@file:JvmName("Channels")
//
//package com.kuanquan.doyincover.utils
//
//import android.content.Context
//import androidx.content.edit
//import arrow.core.Option
//import arrow.core.getOrElse
//import com.meituan.android.walle.WalleChannelReader
//import javax.inject.Inject
//import javax.inject.Singleton
//
///**
// * 获取渠道信息，默认使用 [channelName] 方法
// */
//fun Context.channelName(): String = daggerComponent().apkChannel().channelName
//
//fun Context.latestChannelName(): String = daggerComponent().apkChannel().latestChannelName
//
//
//@Singleton
//class ApkChannel @Inject constructor(@AppContext context: Context, storage: Storage) {
//
//  companion object {
//    private const val APK_CHANNEL_NAME = "apk_channel_name"
//    private const val NULL = "N/A"
//  }
//
//  /**
//   * 从 APK 文件中获取渠道信息，如果覆盖安装的话会取最后安装的 apk 的渠道信息
//   */
//  val latestChannelName by lazy {
//    Option.fromNullable(WalleChannelReader.getChannel(context))
//        .getOrElse { NULL }
//  }
//
//  /**
//   * 第一次会从 APk 中获取渠道信息，并保存到 SharedPreferences 中。
//   * 覆盖安装的话会取第一次安装的渠道信息
//   */
//  val channelName by lazy {
//    var channel = storage.appPreference.getString(APK_CHANNEL_NAME, NULL)
//    if (channel == null || NULL == channel) {
//      channel = latestChannelName
//      storage.appPreference.edit(commit = true) {
//        putString(APK_CHANNEL_NAME, channel)
//      }
//    }
//    channel.valueOrDefault(NULL)
//  }
//}
