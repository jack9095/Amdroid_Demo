package com.xj.arouter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.facade.model.RouteMeta
import com.alibaba.android.arouter.launcher.ARouter
import com.xj.smartrefresh.R
import com.xj.testaroutermodule.ModuleMainActivity

@Route(path="/tow/activity")
class TwoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
        ARouter.getInstance().build("/module1/main").navigation()

    }
}