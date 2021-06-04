package com.kuanquan.lintapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AvoidUsageApi {

    fun method(context: Context) {
        context.getSharedPreferences("", Context.MODE_PRIVATE)

        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()

        "".toInt()

        Log.i("wf", "")
    }

    fun construction() {
        Thread()
    }

    class MyActivity : AppCompatActivity()

}