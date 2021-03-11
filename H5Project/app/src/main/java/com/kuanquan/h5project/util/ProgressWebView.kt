package com.kuanquan.h5project.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.kuanquan.h5project.R

class ProgressWebView: WebView {

    private var progressbar: ProgressBar? = null

    @SuppressLint("SetJavaScriptEnabled")
    constructor(context: Context, attrs:AttributeSet): super(context,attrs){
        progressbar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressbar?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 6, 0, 0)
        progressbar?.progressDrawable = context.resources.getDrawable(R.drawable.progress_bar)

        addView(progressbar)
        webChromeClient = WebChromeClient()
        webViewClient = MyWebViewClient()


//        val settings = settings // 获取WebSettings
//        settings.builtInZoomControls = false // 进行控制缩放
//        settings.setSupportZoom(false) // 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
//        settings.javaScriptEnabled = true // 如果访问的页面中有JavaScript，则WebView必须设置支持JavaScript，否则显示空白页面
//        settings.cacheMode = WebSettings.LOAD_DEFAULT
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getSettings().mixedContentMode =
//                WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // 设置当一个安全站点企图加载来自一个不安全站点资源时WebView的行为
//        }
    }

    //浏览器
    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (progressbar?.visibility == View.GONE) {
                progressbar?.visibility = View.VISIBLE
            }
            progressbar?.progress = newProgress
            if (newProgress >= 100) {
                progressbar?.visibility = View.GONE
            }
            super.onProgressChanged(view, newProgress)
        }
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            LogUtil.e("ProgressWebView", "onReceivedError")
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = progressbar?.layoutParams as? LayoutParams
        lp?.x = l
        lp?.y = t
        progressbar?.layoutParams = lp
        super.onScrollChanged(l, t, oldl, oldt)
    }
}