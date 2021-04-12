package com.kuanquan.demo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kuanquan.demo.base.BaseMAinActivity
import com.kuanquan.demo.enum_p.State
import com.plutinosoft.platinum.ServerParams
import java.util.*

// TODO 1
class MainActivity : BaseMAinActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            when (ServerInstance.INSTANCE.mState) {
                State.IDLE -> startServerService()
                State.RUNNING -> stopServerService()
                else -> {}
            }
        }
    }

    // Start the server
    private fun startServerService() {
        val intent = Intent(this, DLNAService::class.java)
        intent.putExtra(DLNAService.EXTRA_SERVER_PARAMS, ServerParams("demo", true,
            UUID.randomUUID().toString()))
        startService(intent)
        Toast.makeText(this,"开启服务", Toast.LENGTH_SHORT).show()
    }

    // Stop the server service
    private fun stopServerService() {
        val intent = Intent(this, DLNAService::class.java)
        stopService(intent)
        Toast.makeText(this,"停止服务", Toast.LENGTH_SHORT).show()
    }
}