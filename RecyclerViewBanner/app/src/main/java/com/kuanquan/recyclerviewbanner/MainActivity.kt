package com.kuanquan.recyclerviewbanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.kuanquan.recyclerviewbanner.adapter.WebBannerAdapter
import com.kuanquan.recyclerviewbanner.banner.BannerLayout

class MainActivity : AppCompatActivity(), BannerLayout.OnBannerItemClickListener {

    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerBanner: BannerLayout = findViewById(R.id.recycler)
        val bannerVertical: BannerLayout = findViewById(R.id.recycler_ver)

        val list: MutableList<String> = ArrayList()
        list.add("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg")
        list.add("http://img3.imgtn.bdimg.com/it/u=2293177440,3125900197&fm=27&gp=0.jpg")
        list.add("http://img3.imgtn.bdimg.com/it/u=3967183915,4078698000&fm=27&gp=0.jpg")
        list.add("http://img0.imgtn.bdimg.com/it/u=3184221534,2238244948&fm=27&gp=0.jpg")
        list.add("http://img4.imgtn.bdimg.com/it/u=1794621527,1964098559&fm=27&gp=0.jpg")
        list.add("http://img4.imgtn.bdimg.com/it/u=1243617734,335916716&fm=27&gp=0.jpg")
        val webBannerAdapter = WebBannerAdapter(this, list)
        webBannerAdapter.setOnBannerItemClickListener { position ->
            Toast.makeText(
                this@MainActivity,
                "点击了第  $position  项",
                Toast.LENGTH_SHORT
            ).show()
        }

        val webBannerAdapter2 = WebBannerAdapter(this, list)
        webBannerAdapter2.setOnBannerItemClickListener { position ->
            Toast.makeText(
                this@MainActivity,
                "点击了第  $position  项",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerBanner.setAdapter(webBannerAdapter)
        bannerVertical.setAdapter(webBannerAdapter2)

    }

    fun jump(view: View?) {
        startActivity(Intent(this@MainActivity, NormalActivity::class.java))
    }

    fun jumpOverFlying(view: View?) {
        startActivity(Intent(this@MainActivity, OverFlyingActivity::class.java))
    }

    override fun onItemClick(position: Int) {}

}