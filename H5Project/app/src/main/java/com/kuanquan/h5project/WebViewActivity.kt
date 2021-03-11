package com.kuanquan.h5project

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.View
import com.kuanquan.h5project.base.BaseActivity
import com.kuanquan.h5project.h5.JSInterface
import com.kuanquan.h5project.h5.JSInterface.OnClickH5Listener
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: BaseActivity(), OnClickH5Listener {
    private val TAG = this.javaClass.simpleName
    private var mJSInterface: JSInterface? = null

    override fun getLayoutId(): Int = R.layout.activity_web_view

    @SuppressLint("JavascriptInterface","Registered")
    override fun initView() {
        top_navigation.setClick(this)
        setWebSetting(web_view)
        setHttpAndHttps()
        mJSInterface = JSInterface(this)
        web_view.addJavascriptInterface(mJSInterface!!, "app")
    }

    override fun initData() {
        web_view.loadUrl("https://sj.qq.com/myapp/detail.htm?apkName=com.tencent.qqappmarket.hd");
    }

    override fun onClick(view: View?) {}

    override fun voiceAnnouncements(type: String) {
        val hasPermissions = hasPermissions(Manifest.permission.CAMERA)
        if (!hasPermissions) {
            requestPermissions(0, Manifest.permission.CAMERA)
        } else {

        }
    }

    override fun printingData(json: String) {

    }

    override fun cameraPhone() {
        super.cameraPhone()
    }

    val version = Build.VERSION.SDK_INT
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 调用js的方法
//        if (version < 18) {
//            myWebView.loadUrl("javascript:javaCallJs(" + "'" + "contents" + "'" + ")");
//        } else {
//            myWebView.evaluateJavascript("javascript:javaCallJs(" + "'" + "contents" + "'" + ")", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    //此处为 js 返回的结果
//                }
//            });
//        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}