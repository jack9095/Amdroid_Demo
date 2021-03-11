package com.kuanquan.h5project.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.kuanquan.h5project.util.LoadDialog
import kotlin.system.exitProcess


abstract class BaseActivity: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setStatusBar(Color.WHITE)
        initView()
        initData()
    }

    var progressDialog: LoadDialog? = null

    open fun showProgressDialog(): LoadDialog? {
        progressDialog = LoadDialog(this)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.setCancelable(true)
        try {
            if (progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
            progressDialog?.show()
        } catch (e: Exception) {
        }
        return progressDialog
    }


    open fun dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    // TODO 型参数组 修饰符 vararg
    protected fun addOnClickListeners(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (@IdRes id in ids) {
            findViewById<View>(id).setOnClickListener(listener)
        }
    }

    protected fun showToast(str: String?) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }


    private fun setStatusBar(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
            if (isLightColor(color)) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    private fun isLightColor(@ColorInt color: Int): Boolean = ColorUtils.calculateLuminance(color) >= 0.5

    @ColorInt
    protected fun getStatusBarColor(): Int = Color.WHITE

    private var mExitTime: Long = 0
    open fun oDoubleCheck() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
            exitProcess(0)
        }
    }

    open fun hasPermissions(vararg permissions: String?): Boolean {
        for (permission in permissions) {
            if (permission?.let { ContextCompat.checkSelfPermission(this, it) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    open fun requestPermissions(requestCode: Int, vararg permissions: String?) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPhone()
            } else {
                Toast.makeText(this, "你拒绝了摄像头权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun cameraPhone() {}

    // webView 配置
    private var myWebView: WebView? = null
    protected var uploadMessage: ValueCallback<Uri>? = null
    protected var uploadMessageAboveL: ValueCallback<Array<Uri?>>? = null
    protected val FILE_CHOOSER_RESULT_CODE = 10000
    protected var settings: WebSettings? = null

    // 可以让 js 调用本地相机的方法
    @SuppressLint("SetJavaScriptEnabled")
    protected open fun setWebSetting(webView: WebView?) {
        myWebView = webView
        val settings = webView?.settings

        settings?.useWideViewPort = true
        settings?.loadWithOverviewMode = true
        settings?.javaScriptEnabled = true
        myWebView?.setWebChromeClient(object : WebChromeClient() {
            //  android 3.0以下：用的这个方法
            fun openFileChooser(valueCallback: ValueCallback<Uri>?) {
                uploadMessage = valueCallback
                openImageChooserActivity()
            }

            // android 3.0以上，android4.0以下：用的这个方法
            fun openFileChooser(valueCallback: ValueCallback<*>?, acceptType: String?) {
                uploadMessage = valueCallback as? ValueCallback<Uri>?
                openImageChooserActivity()
            }

            //android 4.0 - android 4.3  安卓4.4.4也用的这个方法
            fun openFileChooser(
                valueCallback: ValueCallback<Uri>?, acceptType: String?,
                capture: String?
            ) {
                uploadMessage = valueCallback
                openImageChooserActivity()
            }

            //android4.4 无方法。。。
            // Android 5.0及以上用的这个方法
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                uploadMessageAboveL = filePathCallback
                openImageChooserActivity()
                return true
            }
        })

        settings?.useWideViewPort = true
        settings?.loadWithOverviewMode = true
        settings?.javaScriptEnabled = true
        settings?.builtInZoomControls = false
        settings?.setSupportZoom(false)
        settings?.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
//        myWebView?.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(
//                view: WebView,
//                url: String
//            ): Boolean {
//                view.loadUrl(url)
//                return true
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                super.onPageFinished(view, url)
//                LogUtil.e("BaseCommonH5Activity  url = ", url)
//            }
//
//            override fun onReceivedError(
//                view: WebView,
//                request: WebResourceRequest,
//                error: WebResourceError
//            ) {
//                super.onReceivedError(view, request, error)
//                LogUtil.e("BaseCommonH5Activity onReceivedError error = ", error)
//            }
//
//            override fun onReceivedSslError(
//                view: WebView,
//                handler: SslErrorHandler,
//                error: SslError
//            ) {
//                super.onReceivedSslError(view, handler, error)
//                LogUtil.e("BaseCommonH5Activity onReceivedSslError error = ", error)
//            }
//
//            override fun onReceivedHttpError(
//                view: WebView,
//                request: WebResourceRequest,
//                errorResponse: WebResourceResponse
//            ) {
//                super.onReceivedHttpError(view, request, errorResponse)
//                LogUtil.e("BaseCommonH5Activity onReceivedSslError error = ", errorResponse)
//            }
//        }
    }

    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(
            Intent.createChooser(i, "Image Chooser"),
            FILE_CHOOSER_RESULT_CODE
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return
            val result =
                if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (uploadMessage != null) {
                uploadMessage!!.onReceiveValue(result)
                uploadMessage = null
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) return
        var results: Array<Uri?>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    results = arrayOfNulls(clipData.itemCount)
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null) results = arrayOf(Uri.parse(dataString))
            }
        }
        uploadMessageAboveL?.onReceiveValue(results)
        uploadMessageAboveL = null
    }

    // 5.0 以后的WebView加载的链接为Https开头，但是链接里面的内容，比如图片为Http链接，这时候，图片就会加载不出来，怎么解决
    protected open fun setHttpAndHttps() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //两者都可以
            myWebView?.settings?.mixedContentMode = myWebView?.settings?.mixedContentMode!!
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            dismissProgressDialog()
        }
        if (myWebView != null) {
            val parent = myWebView?.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(myWebView)
            }
            myWebView?.stopLoading()
            myWebView?.settings?.javaScriptEnabled = false
            myWebView?.clearHistory()
            myWebView?.clearView()
            myWebView?.removeAllViews()
            myWebView?.destroy()
            myWebView = null
        }
    }
}