package com.xj.arouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.model.RouteMeta
import com.alibaba.android.arouter.facade.template.IRouteGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.xj.smartrefresh.R
import kotlinx.android.synthetic.main.activity_main.*

@Route(path = "/main/activity")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val map = mutableMapOf<String, RouteMeta>()
        val clazz = Class.forName("com.alibaba.android.arouter.core.Warehouse");
        val field = clazz.getDeclaredField("groupsIndex")
        field.isAccessible = true
        val groupsIndex = field.get(null) as? Map<String, Class<out IRouteGroup>>

        groupsIndex?.forEach {
            it.value.newInstance().loadInto(map)
        }
        val strings =  map.map {
            it.value.destination?.name
        }.joinToString(",")
        Log.e("xj",strings)




        ARouter.getInstance().build("/tow/activity").navigation()
    }


}