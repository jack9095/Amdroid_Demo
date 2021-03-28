package com.kuanquan.binder_client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import com.kuanquan.binder_service.IFlyAidl
import com.kuanquan.binder_service.Person
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var iFlyAidl: IFlyAidl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService()
    }

    private fun bindService() {
        val intent = Intent()
        intent.component = ComponentName(
                "com.kuanquan.binder_service",
                "com.kuanquan.binder_service.FlyAidlService")
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iFlyAidl = IFlyAidl.Stub.asInterface(service)
        }

    }

    fun click(view: View) {
        try {
            iFlyAidl?.addPerson(Person("jack", 10))
            val personList = iFlyAidl?.personList
            Log.e("MainActivity", personList.toString() )
        } catch (e: Exception) {

        }
    }
}