package com.kuanquan.safetyinspectionapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lahm.library.EasyProtectorLib
import com.lahm.library.SecurityCheckUtil
import com.lahm.library.VirtualApkCheckUtil

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.checkByPrivateFilePath).setOnClickListener(this)
        findViewById<View>(R.id.checkByOriginApkPackageName).setOnClickListener(this)
        findViewById<View>(R.id.checkByMultiApkPackageName).setOnClickListener(this)
        findViewById<View>(R.id.checkByHasSameUid).setOnClickListener(this)
        findViewById<View>(R.id.checkByPortListening).setOnClickListener(this)
        findViewById<View>(R.id.checkByCreateLocalServerSocket).setOnClickListener(this)
        findViewById<View>(R.id.checkRoot).setOnClickListener(this)
        findViewById<View>(R.id.checkDebuggable).setOnClickListener(this)
        findViewById<View>(R.id.checkDebuggerAttach).setOnClickListener(this)
        findViewById<View>(R.id.checkTracer).setOnClickListener(this)
        findViewById<View>(R.id.checkXP).setOnClickListener(this)
        findViewById<View>(R.id.readSysProperty).setOnClickListener(this)
        findViewById<View>(R.id.test).setOnClickListener(this)
    }

    private fun forTest() {
        //only for test
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.test -> forTest()
            R.id.checkByPrivateFilePath -> {
                val v1 = findViewById<TextView>(R.id.v1)
                v1.text = if (VirtualApkCheckUtil.getSingleInstance().checkByPrivateFilePath(this, null)) "privatePath-NO" else "privatePath-OK"
            }
            R.id.checkByOriginApkPackageName -> {
                val v2 = findViewById<TextView>(R.id.v2)
                v2.text = if (VirtualApkCheckUtil.getSingleInstance().checkByOriginApkPackageName(this, null)) "packageName-NO" else "packageName-OK"
            }
            R.id.checkByMultiApkPackageName -> {
                val v3 = findViewById<TextView>(R.id.v3)
                v3.text = if (VirtualApkCheckUtil.getSingleInstance().checkByMultiApkPackageName(null)) "maps-NO" else "maps-OK"
            }
            R.id.checkByHasSameUid -> {
                val v4 = findViewById<TextView>(R.id.v4)
                v4.text = if (VirtualApkCheckUtil.getSingleInstance().checkByHasSameUid(null)) "uid-NO" else "uid-OK"
            }
            R.id.checkByPortListening -> {
                VirtualApkCheckUtil.getSingleInstance().checkByPortListening("port", null)
                val v5 = findViewById<TextView>(R.id.v5)
                v5.text = "port listening"
            }
            R.id.checkByCreateLocalServerSocket -> {
                val v6 = findViewById<TextView>(R.id.v6)
                v6.text = if (EasyProtectorLib.checkIsRunningInVirtualApk(packageName, null)) "LocalServerSocket-NO" else "LocalServerSocket-OK"
            }
            // 是否 ROOT
            R.id.checkRoot -> {
                val r1 = findViewById<TextView>(R.id.r1)
                r1.text = if (EasyProtectorLib.checkIsRoot()) "rooted" else "no-root"
            }
            R.id.checkDebuggable -> {
                val d1 = findViewById<TextView>(R.id.d1)
                d1.text = if (EasyProtectorLib.checkIsDebug(this)) "debuggable" else "release"
            }
            R.id.checkDebuggerAttach -> {
                val d2 = findViewById<TextView>(R.id.d2)
                d2.text = if (SecurityCheckUtil.getSingleInstance().checkIsUsbCharging(this@MainActivity)) if (SecurityCheckUtil.getSingleInstance().checkIsDebuggerConnected()) "debugger-connect！！" else "only-usb-charging" else "only-charging"
            }
            R.id.checkTracer -> {
                EasyProtectorLib.checkIsBeingTracedByC()
                val d3 = findViewById<TextView>(R.id.d3)
                d3.text = "see log"
            }
            R.id.checkXP -> {
                val x1 = findViewById<TextView>(R.id.x1)
                x1.text = if (EasyProtectorLib.checkIsXposedExist()) "shutdown xp success" else "failed"
            }
            R.id.readSysProperty -> {
                val e1 = findViewById<TextView>(R.id.e1)
                EasyProtectorLib.checkIsRunningInEmulator(this) { emulatorInfo -> e1.text = emulatorInfo }
            }
        }
    }
}