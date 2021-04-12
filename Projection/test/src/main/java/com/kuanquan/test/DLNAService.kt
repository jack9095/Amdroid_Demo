package com.kuanquan.test

import android.app.Service
import android.content.*
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.MulticastLock
import android.os.IBinder
import android.util.Log
import com.plutinosoft.platinum.ServerParams

/**
 * The service that manage the server instance
 * 管理服务器实例的服务
 * TODO 2
 */
class DLNAService: Service() {

    private val TAG = "DLNAService"

    companion object{
        // 从 MainActivity 传递到 DLNAService 的参数的 key
        val EXTRA_SERVER_PARAMS_KEY = "EXTRA_SERVER_PARAMS_KEY"
    }

    private var mMulticastLock: MulticastLock? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        acquireMulticastLock()
    }

    private fun acquireMulticastLock() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        mMulticastLock = wifiManager?.createMulticastLock(TAG)
        mMulticastLock?.acquire()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val params: ServerParams? = intent?.getParcelableExtra(EXTRA_SERVER_PARAMS_KEY)
            if (params != null) {
                Log.e("DLNAService", "开启服务")
                ServerInstance.start(params)
            }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mMulticastLock?.release()
        mMulticastLock = null
        ServerInstance.stop()
        super.onDestroy()
    }
}