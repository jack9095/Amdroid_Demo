package com.kuanquan.recyclerviewbanner.test

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.*
import com.kuanquan.recyclerviewbanner.R
import com.kuanquan.recyclerviewbanner.demo.MyAdapter

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        initRecycler()
        initRecyclerBanner()
    }

    /**
     * 使用开源的 banner, 基于 RecyclerView 封装的
     */
    private fun initRecyclerBanner() {
       val banner = findViewById<BannerRecyclerView>(R.id.bannerRecyclerView)
        val list: MutableList<String> = ArrayList()
        list.add("http://img0.imgtn.bdimg.com/it/u=1352823040,1166166164&fm=27&gp=0.jpg")
        list.add("http://img0.imgtn.bdimg.com/it/u=3184221534,2238244948&fm=27&gp=0.jpg")
        list.add("http://img4.imgtn.bdimg.com/it/u=1794621527,1964098559&fm=27&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1937885777,439518627&fm=26&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3114284585,3252063096&fm=26&gp=0.jpg")
        list.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1744329700,1413485322&fm=26&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1684231733,1620228646&fm=26&gp=0.jpg")
        list.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3258484351,1612376358&fm=26&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1408371311,2263836872&fm=26&gp=0.jpg")
        list.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2482360593,2739674846&fm=26&gp=0.jpg")

        banner.setIndicator(IndicatorView(this).apply {
            setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_DASH)
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2.5f)
                .setIndicatorSelectedRatio(2f)
                .setIndicatorSelectedRadius(2.5f)
                .setIndicatorColor(Color.parseColor("#a6ffffff"))
                .setIndicatorSelectorColor(Color.WHITE)
                .setIndicatorSpacing(2f)
        })

        banner.initBannerImageView(list, object : BaseBannerRecyclerView.OnBannerItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@TestActivity, "clicked:$position", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun initRecycler() {
        val  recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView?.run {
            // 轮播图通常是横向的，所以这里也是设置的横向的布局管理器
            layoutManager = LinearLayoutManager(this@TestActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        //LinearSnapHelper是SnapHelper的一个官方实现类，作用是让RecyclerView在滚动时可以保持有一个item在中间位置，类似于轮播图的效果。
        //创建好之后，通过attachToRecyclerView()方法绑定上RecyclerView即可
        LinearSnapHelper().attachToRecyclerView(recyclerView)

        recyclerView?.adapter = MyAdapter()

        //为了更好的用户体验，对RecyclerView的滑动事件做一下处理
//        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                val manager = recyclerView.layoutManager as LinearLayoutManager?
//                val firstVisibleItemPosition = manager!!.findFirstVisibleItemPosition()
//                when (newState) {
//                    SCROLL_STATE_IDLE -> if (isSlidingByHand) {
//                        mCurrentPosition = firstVisibleItemPosition
//                    }
//                    SCROLL_STATE_DRAGGING -> {
//                        isSlidingByHand = true
//                        isSlidingAuto = false
//                    }
//                    SCROLL_STATE_SETTLING -> isSlidingAuto = !isSlidingByHand
//                }
//            }
//        })
    }

}