package com.kuanquan.webviewapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kuanquan.mylibrary.WebConstants
import com.kuanquan.mylibrary.view.DWebView

/**
 * https://juejin.cn/post/6844903684065722382
 * https://github.com/xudjx/webprogress
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.openWeb1).setOnClickListener(View.OnClickListener {
            WebActivity.start(
                this@MainActivity,
                "腾讯网",
                "https://xw.qq.com/?f=qqcom",
                WebConstants.LEVEL_BASE
            )
        })

        findViewById<TextView>(R.id.openWeb2).setOnClickListener(View.OnClickListener { // for baselevel
            //RemoteCommonWebActivity.start(MainActivity.this, "AIDL测试", DWebView.CONTENT_SCHEME + "aidl.html");

            // for account level
            WebActivity.start(
                this@MainActivity,
                "AIDL测试",
                DWebView.CONTENT_SCHEME.toString() + "aidl.html",
                WebConstants.LEVEL_ACCOUNT
            )
        })
    }
}