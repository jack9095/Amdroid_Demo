package com.kuanquan.botttomsheetdialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuanquan.botttomsheetdialog.demo.DemoActivity
import com.kuanquan.botttomsheetdialog.test.TestActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            TestDialogFragment.newInstance().show(supportFragmentManager)
        }
        test.setOnClickListener {
            startActivity(Intent(this@MainActivity, TestActivity::class.java))
//            startActivity(Intent(this@MainActivity, DemoActivity::class.java))
        }
    }
}