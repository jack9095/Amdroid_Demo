package com.kuanquan.doyincover.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.gyf.immersionbar.ImmersionBar
//import com.gyf.barlibrary.ImmersionBar
import com.jakewharton.rxbinding2.view.RxView
import com.kuanquan.doyincover.R
import com.kuanquan.doyincover.publisher.view.LongClickProgressBar
//import com.luck.picture.lib.previewloading.GlideImageLoader
//import com.luck.picture.lib.previewloading.OnProgressListener
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.*
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun debug(block: () -> Unit) {
//    if (BuildConfig.DEBUG)
        block()
}

fun release(block: () -> Unit) {
//    if (!BuildConfig.DEBUG)
        block()
}


fun formatPrice(price: String): String {
    if (price.matches(Regex("\\d+\\.0+"))) {
        return price.substring(0, price.lastIndexOf('.'))
    } else if (price.matches(Regex("\\d+\\.\\d0"))) {
        return price.substring(0, price.length - 1)
    }
    return price
}

fun formatPrice(price: Int): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(price / 100f)
}

fun hash(data: String): String {
    val bytes = data.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun createBitmapByScale(
        bitmap: Bitmap?,
        scale: Float
): Bitmap? {
    if (bitmap == null) {
        return null
    }
    val w: Int
    val h: Int
    w = bitmap.width
    h = bitmap.height
//  if (w < 300 && h < 300) {
//    return bitmap
//  }
    val matrix = Matrix()
//  matrix.postScale(scale / w, scale / h)
    return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
}

fun formatTime(timeInMillis: Long): String {
    val simpleDateFromat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return simpleDateFromat.format(timeInMillis)
}

fun isNetWorkConntectd(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    if (networkInfo != null && networkInfo.isConnected) {
        if (networkInfo.state == NetworkInfo.State.CONNECTED) {
            return true
        }
    }
    return false
}


fun isWifiConnected(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
    return wifiNetworkInfo?.isConnected == true
}

fun getBitmap(position: Int, context: Context, url: String, width: Int, height: Int, block: (Bitmap?, Int) -> Unit) {
    val buildUrl = StringBuilder(url).apply {
        if (!url.contains("?imageMogr2") && isWebPSupportableHost(url)) {
//            Timber.e("==111==")
            append("?imageMogr2")
            append("/strip")
            append("/gravity/center")
            append("/thumbnail")
            append("/${width}x$height")
            append("/format/webp")
        }
    }
    val imageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(buildUrl.toString()))
            .build()
    val imagePipeline = Fresco.getImagePipeline()
    imagePipeline.fetchDecodedImage(imageRequest, context)
            .subscribe(object : BaseBitmapDataSubscriber() {
                override fun onNewResultImpl(bitmap: Bitmap?) {
                    block.invoke(bitmap, position)
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {}
            }, UiThreadImmediateExecutorService.getInstance())
}

fun SimpleDraweeView.setImageUrl2Webp(url: String, width: Int, height: Int) {
    if (!isWebPSupportableHost(url)) {
        val urlTemp = StringBuilder(url).apply {
            if (!url.contains("?imageMogr2")) {
                append("?imageMogr2")
                append("/format/webp")
                append("/thumbnail")
                append("/${width}x$height")
            }
        }.toString()
        setImageURI(urlTemp)
//    Timber.e("setImageUrl2Webp-urlTemp：$urlTemp")
    } else {
        setImageURI(url)
//    Timber.e("setImageUrl2Webp-url：$url")
    }
}

fun saveImageFromDataSource(context: Context, url: String, localSavePath: String) {
    val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
            .setProgressiveRenderingEnabled(true).build()

    val dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, context)

    dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<CloseableImage>>() {
        override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
            context.showLongToast(context.getString(R.string.string_save_pic_error))
        }

        override fun onNewResultImpl(source: DataSource<CloseableReference<CloseableImage>>) {
            val reference = source.result
            if (reference != null) {
                val image = reference.get()
                if (image is CloseableBitmap) {
                    val picBitmap = image.underlyingBitmap
                    if (saveBitmap(picBitmap, localSavePath)) {
                        context.showLongToast(String.format(context.getString(R.string.string_save_pic_format), localSavePath))
                        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        val uri = Uri.fromFile(File(localSavePath))
                        intent.data = uri
                        context.sendBroadcast(intent)
                    } else {
                        context.showLongToast(context.getString(R.string.string_save_pic_error))
                    }
                } else {
                    context.showLongToast(context.getString(R.string.string_save_pic_error))
                }
            } else {
                context.showLongToast(context.getString(R.string.string_save_pic_error))
            }
        }
    }, UiThreadImmediateExecutorService.getInstance())
}

fun saveBitmap(bitmap: Bitmap, localSavePath: String): Boolean {
    if (TextUtils.isEmpty(localSavePath)) {
        throw NullPointerException("保存的路径不能为空")
    }
    val f = File(localSavePath)
    if (f.exists()) {// 如果本来存在的话，删除
        f.delete()
    }
    try {
        f.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    try {
        val out = FileOutputStream(f)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()

    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return false
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
    return true
}


fun isWebPSupportableHost(url: String): Boolean {
    val hostList = arrayOf(
            "iqn.shuashuakan.net",
            "testiqn.shuashuakan.net",
            "iqntest.shuashuakan.net",
            "dn-img-seriousapps.qbox.me",
            "dn-test-img-seriousapps.qbox.me",
            "test.image.ricebook.com",
            "image.ricebook.com",
            "v2.ricebook.com",
            "v3.ricebook.com",
            "v4.ricebook.com"

    )
    return hostList.contains(url.toLowerCase())
}


//绘制一圆角位图
fun getCircledBitmap(bitmap: Bitmap): Bitmap {
    val circleSize = Math.min(bitmap.width, bitmap.height)
    val output = Bitmap.createBitmap(circleSize, circleSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint()
    val rect = Rect(0, 0, circleSize, circleSize)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    val size = Math.min(bitmap.width / 2f, bitmap.height / 2f)
    canvas.drawCircle(size, size, size, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
}

fun parseSsrUrl(url: String): Map<String, String> {
    return url.substring(url.indexOf("?") + 1).split("&").mapNotNull { parseNameAndValue(it) }.toMap()
}

private fun parseNameAndValue(s: String): Pair<String, String>? {
    val array = s.split('=')
    if (array.size == 2) {
        return array[0] to array[1].urlDecode()
    }
    return null
}

//高斯模糊
fun showUrlBlur(draweeView: SimpleDraweeView, url: String, iterations: Int, blurRadius: Int) {
    val uri = Uri.parse(url)
    showUrlBlur(draweeView, uri, iterations, blurRadius)
}

fun showUrlBlur(draweeView: SimpleDraweeView, uri: Uri, iterations: Int, blurRadius: Int) {
    val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setPostprocessor(IterativeBoxBlurPostProcessor(iterations, blurRadius))
            .build()
    val controller = Fresco.newDraweeControllerBuilder()
            .setOldController(draweeView.controller).setImageRequest(request).build()
    draweeView.controller = controller
}

//设置带缓存的图片
fun showCacheImage(imageView: ImageView, url: String, mContext: Context) {
    val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    Glide.with(mContext)
            .load(url)
            .apply(options)
            .into(imageView)
}


fun SimpleDraweeView.setGifImage(url: String?) {
    val uri = if (url != null) Uri.parse(url) else null

    //减小图片大小 size ?
    val size = 10
    val request = ImageRequestBuilder.newBuilderWithSource(uri)
            .setResizeOptions(ResizeOptions(size, size)).build()

    controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(request)
            .setOldController(controller)
            .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
            .build()
}

fun SimpleDraweeView.setImagePath(path: String?) {
    var mediaPath = path
    if (path != null && path.length > 5) {
        if (path.substring(0, 4) != "http") {
            mediaPath = "file://$path"
        }
    }
    setImageURI(mediaPath)
}

fun SimpleDraweeView.setImagePathWithRoundBorder(context: Context, path: String?, roundNum: Int, @ColorInt color: Int, borderWith: Int) {
    var mediaPath = path
    if (path != null && path.length > 5) {
        if (path.substring(0, 4) != "http") {
            mediaPath = "file://$path"
        }
    }
    val roundingParams = RoundingParams.fromCornersRadius(context.dip(roundNum).toFloat())
    roundingParams.borderColor = color
    roundingParams.borderWidth = context.dip(borderWith).toFloat()
    hierarchy.roundingParams = roundingParams
    setImageURI(mediaPath)
}

fun SimpleDraweeView.setImagePath(context: Context, path: String?, num: Int) {
    var mediaPath = path
    if (path != null && path.length > 5) {
        if (path.substring(0, 4) != "http") {
            mediaPath = "file://$path"
        }
    }
    val roundingParams = RoundingParams.fromCornersRadius(context.dip(num).toFloat())
    hierarchy.roundingParams = roundingParams
    setImageURI(mediaPath)
}

fun setImageForGlide(context: Context, path: String?, imageView: ImageView?, asBitmap: Boolean) {
    val options = RequestOptions()
            .placeholder(R.drawable.ic_block_comment)
            .error(R.drawable.ic_block_comment)

    if (imageView != null) {
        if (asBitmap)
            Glide.with(context).load(path).apply(options).into(imageView)
        else
            Glide.with(context).asGif().load(path).apply(options).into(imageView)
    }
}

/**
 * 加载图片时显示加载中动画
 */
fun setImageLoadingForGlide(context: Context, path: String?, imageView: ImageView?, progressView: ProgressBar?) {

//    if (imageView != null && progressView != null) {
//        GlideImageLoader.create(imageView).loadImage2(context, path, 0, object :
//            LongClickProgressBar.OnProgressListener {
//            override fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) {
//                if (isComplete) {
//                    progressView.visibility = View.GONE
//                } else {
//                    progressView.visibility = View.VISIBLE
//                    progressView.progress = percentage
//                }
//            }
//        })
//    }
}

fun imageUrl2WebP(url: String, width: Int, height: Int): String {
    if (!isWebPSupportableHost(url)) {
        return StringBuilder(url).apply {
            if (!url.contains("?imageMogr2")) {
                append("?imageMogr2")
                append("/strip")
                append("/gravity/center")
                append("/thumbnail")
                append("/${width}x$height")
                append("/format/webp")
            }
        }.toString()
    } else {
        return url
    }
}

//http://iqn.shuashuakan.net/video/cover/1543285410340001?imageMogr2/format/webp/thumbnail/1125x
fun imageUrl2WebP2(url: String, width: Int, height: Int): String {
    if (!isWebPSupportableHost(url)) {
        return StringBuilder(url).apply {
            if (!url.contains("?imageMogr2")) {
                append("?imageMogr2")
                append("/format/webp")
                append("/thumbnail")
                append("/${width}x$height")
            }
        }.toString()
    } else {
        return url
    }
}


fun Context.copyString(text: String, toast: String? = null) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    cm.primaryClip = ClipData.newPlainText("", text)
    if (toast != null) {
        showLongToast(toast)
    }
}

fun numFormat(num: Int?): String {
    return if (num != null) if (num < 10000) {
        num.toString()
    } else {
        val formatNum = num * 1.0 / 10000
        val result = StringFromat.getDecimalString(formatNum, "#0.0")
        "${result}w"
    } else ""
}

fun numFormat4Profile(num: Int?): String {
    num?.let {
        if (num <= 0) {
            return ""
        }
        if (num < 10000) {
            return "$num UP"
        } else {
            val formatNum = num * 1.0 / 10000
            val result = StringFromat.getDecimalString(formatNum, "#0.0")
            return "${result}w UP"
        }
    } ?: return ""
}

fun urlGetValue(url: String): Map<String, String> {
    val index = url.indexOf("?")
    val temp = url.substring(index + 1)
    val keyValue = temp.split("&")

    val valueMap: MutableMap<String, String> = mutableMapOf()

    keyValue.forEach {
        valueMap[it] = it.replace("$it=", "")
    }
    return valueMap
}

@SuppressLint("CheckResult")
fun View.noDoubleClick(callback: () -> Unit) {
    RxView.clicks(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                callback()
            }
}

fun createUUID(): String {
    return UUID.randomUUID().toString()
}

fun showAlertDialog(context: Context) {
    val alertDialogBuilder = android.app.AlertDialog.Builder(context)
    alertDialogBuilder.setTitle(context.getString(R.string.string_leave_page))
    alertDialogBuilder.setCancelable(false)
    alertDialogBuilder.setPositiveButton(context.getString(R.string.string_confirm)) { dialog, which ->
        dialog.dismiss()
        (context as Activity).finish()
    }
    alertDialogBuilder.setNegativeButton(context.getString(R.string.string_cancel)) { dialog, which ->
        dialog.dismiss()
    }
    alertDialogBuilder.create().show()
}

fun checkPhoneRom(name: String): Boolean {
    // ro.smartisan.version    ro.miui.ui.version.name
    return !TextUtils.isEmpty(getPhoneProperty(name))
}

private fun getPhoneProperty(name: String): String {
    val line: String
    val input: BufferedReader
    try {
        val p = Runtime.getRuntime().exec("getprop $name")
        input = BufferedReader(InputStreamReader(p.inputStream), 1024)
        line = input.readLine()
        input.close()
    } catch (ex: IOException) {
        throw IOException(ex.toString())
    }
    return line
}

fun setViewMarginTop(activity: Activity, view: View, height: Int? = -1) {
    val statusBarHeight = ImmersionBar.getStatusBarHeight(activity)
    val lp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    var heightDefault = 0
    if (height == -1) {
        heightDefault = ScreenUtils.dip2px(activity, 50f)
    } else {
        heightDefault = height!!
    }
    lp.setMargins(0, statusBarHeight + heightDefault, 0, 0)
    view.layoutParams = lp
}


fun parseColor(color: String): Int {
    var parseColor = 0
    try {
        parseColor = Color.parseColor(color)
    } catch (e: Exception) {
        Log.e(TAG,"颜色转化错误 转化color = $color")
    }
    return parseColor
}

fun getColor1(color: Int,context: Context): Int {
    return context.resources.getColor(color)
}

const val TAG = "Utils"
