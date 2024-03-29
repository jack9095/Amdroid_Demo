package com.kuanquan.projection

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kuanquan.projection.instance.ServerInstance
import com.kuanquan.projection.service.DLNAService
import com.kuanquan.projection.utils.UUIDUtils
import com.plutinosoft.platinum.ServerParams

class MainActivity : BaseMAinActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            when (ServerInstance.INSTANCE.state) {
                ServerInstance.State.IDLE -> startServerService()
                ServerInstance.State.RUNNING -> stopServerService()
                else -> {}
            }
        }
    }

    // Start the server
    private fun startServerService() {
        val intent = Intent(this, DLNAService::class.java)
        intent.putExtra(DLNAService.EXTRA_SERVER_PARAMS, ServerParams("Platinum", true, UUIDUtils.getRandomUUID()))
        startService(intent)
    }

    // Stop the server service
    private fun stopServerService() {
        val intent = Intent(this, DLNAService::class.java)
        stopService(intent)
    }
}