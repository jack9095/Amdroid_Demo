package com.kuanquan.koin

import android.content.Context
import android.widget.Toast
import com.kuanquan.koin.app.BasePresenter

class MainPresenter(var context: Context, var dbService: DbService) : BasePresenter {
    fun getData(data: String) {
        Toast.makeText(context, dbService.getData(data), Toast.LENGTH_SHORT).show()
    }
}