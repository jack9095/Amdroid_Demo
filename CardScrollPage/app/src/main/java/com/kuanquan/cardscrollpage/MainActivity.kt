package com.kuanquan.cardscrollpage

import android.app.Activity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.kuanquan.cardscrollpage.widget.SimpleOverlayAdapter


class MainActivity : Activity() {

    private val imgUrls = arrayOf(
        "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1383192171,3573826053&fm=26&gp=0.jpg"
        ,
        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1670905418,2793220050&fm=26&gp=0.jpg"
        ,
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3391864021,2947070253&fm=26&gp=0.jpg"
        ,
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1705579171,700114950&fm=26&gp=0.jpg"
        ,
        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3577773561,2706257243&fm=26&gp=0.jpg"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val vp: ViewPager = findViewById(R.id.vp)
        val adapter = SimpleOverlayAdapter(this)
        adapter.setImgUrlsAndBindViewPager(vp, imgUrls, 2)
        vp.adapter = adapter
        vp.currentItem = 5000

    }
}
