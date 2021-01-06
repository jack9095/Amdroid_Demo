package com.kuanquan.viewpager2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2

/**
 * https://blog.csdn.net/m0_47761892/article/details/107669851
 */
class MainActivity : AppCompatActivity() {

    val list = mutableListOf<String>()
    var viewPager2: ViewPager2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..9){
            list.add("$i")
        }

        viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        initViewPager2()

        // 普通使用
//        val myAdapter = MyAdapter()
//        myAdapter.setList(data)
//        viewPager2?.adapter = myAdapter

        // 配合 fragment 使用
        viewPager2?.run {
//            adapter = AdapterFragmentPager(this@MainActivity)
//            offscreenPageLimit = 3
            adapter = AdapterFragmentPagerOther(this@MainActivity, list)
            isUserInputEnabled = true
        }
    }

    private fun initViewPager2() {

        // 竖直滑动
//        viewPager2?.orientation = ViewPager2.ORIENTATION_VERTICAL

        // 页面滑动事件监听
        viewPager2?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(this@MainActivity, "page selected $position", Toast.LENGTH_SHORT).show()
            }
        })

        // 设置是否禁止滑动  true:滑动，false：禁止滑动
//        viewPager2?.isUserInputEnabled = false

    }
}