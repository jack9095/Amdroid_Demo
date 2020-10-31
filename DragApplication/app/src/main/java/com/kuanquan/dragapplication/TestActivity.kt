package com.kuanquan.dragapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.kuanquan.dragapplication.divider.BaseItemDecoration
import com.kuanquan.dragapplication.divider.GridItemDecoration
import com.kuanquan.dragapplication.divider.LinerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class TestActivity : AppCompatActivity() {

    private val lists = mutableListOf<DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        lists.add(DataBean("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1255973422,411881591&fm=26&gp=0.jpg",0))
        lists.add(DataBean("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3902763800,2906880900&fm=26&gp=0.jpg",1))
        lists.add(DataBean("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3265612790,285913880&fm=26&gp=0.jpg",2))
        lists.add(DataBean("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3630025410,2903535830&fm=26&gp=0.jpg",3))
        lists.add(DataBean("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3824075960,2723462822&fm=26&gp=0.jpg",4))
        lists.add(DataBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3153405721,1524067674&fm=26&gp=0.jpg",5))
        lists.add(DataBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3321709900,4195624059&fm=26&gp=0.jpg",6))
        lists.add(DataBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2151063877,2713161468&fm=26&gp=0.jpg",7))
        lists.add(DataBean("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2980016546,3199841401&fm=26&gp=0.jpg",8))
        val recyclerViewAdapter = RecyclerViewAdapter(lists)
        with(recyclerView) {
            layoutManager = GridLayoutManager(this@TestActivity, 3)
//            addItemDecoration(GridDividerItemDecoration(this@MainActivity))
            adapter = recyclerViewAdapter
        }

        grid()
    }

    // 线性
    @SuppressLint("WrongConstant")
    private fun linear() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 20)//设置分割线距离item的间隔
                .setDividerDrawByChild(true)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
                .setHeadViewCount(0)//设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                .setFooterViewCount(0)//设置尾布局的个数 默认为0 尾布局之间没有分割线
                .showLastDivider(false)//最后一个item后面是否有分割线 默认为false
                .setDividerColorProvider(object : BaseItemDecoration.DividerColorProvider {
                    override fun getDividerColor(position: Int, parent: RecyclerView): Int {
                        when ((position + 1) % 4) {
                            0 -> {
                                return Color.parseColor("#FF0000")
                            }
                            1 -> {
                                return Color.parseColor("#00FF00")
                            }
                            2 -> {
                                return Color.parseColor("#0000FF")
                            }
                            3 -> {
                                return Color.parseColor("#000000")
                            }
                            else -> {
                                return Color.parseColor("#000000")
                            }
                        }

                    }
                })//设置分割线绘制的颜色  我们可以设置在不同的位置绘制不同的颜色
                .setDividerVisibleProvider(object : BaseItemDecoration.DividerVisibleProvider {
                    override fun shouldHideDivider(position: Int, parent: RecyclerView): Boolean {
                        //在3的倍数位置 不显示颜色
                        return (position + 1) % 3 == 0
                    }
                })//设置在某个位置隐藏分割线 但是分割线的间隔还是在的,只是不再绘制而已
                .build()
        )
    }


    // 网格
    @SuppressLint("WrongConstant")
    private fun grid() {
        recyclerView.layoutManager = GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            GridItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(10) // 分割线的宽度 单位px
                .setFooterViewCount(0) // 设置尾布局的个数 默认为0 尾布局之间没有分割线
                .setHeadViewCount(0) // 设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                .setDividerMarginPx(0, 0, 0, 0) // 设置分割线距离item的间隔
                .setDividerColorProvider(object : BaseItemDecoration.DividerColorProvider { //设置分割线绘制的颜色  我们可以设置在不同的位置绘制不同的颜色
                    override fun getDividerColor(position: Int, parent: RecyclerView): Int {
                        return Color.parseColor("#ffffff")
                    }

                })
                .build()
        )
    }

    // 混合
    @SuppressLint("WrongConstant")
    private fun multi() {
        recyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(20)//分割线的宽度 单位px
                .setDividerMarginPx(10, 10, 10, 10)//设置分割线距离item的间隔
                .setDividerDrawByChild(false)//设置绘制分割线的长度是否是根据item的长度来绘制 默认为false代表绘制是根据RecyclerView的长度来的
                .setHeadViewCount(0)//设置头布局的个数 默认为0 头布局之间没有分割线 以及头布局与第一条数据之间也是没有分割线
                .setFooterViewCount(0)//设置尾布局的个数 默认为0 尾布局之间没有分割线
                .showLastDivider(false)//最后一个item后面是否有分割线 默认为false
                .setDividerPaintProvider(object : BaseItemDecoration.DividerPaintProvider {
                    override fun getDividerPaint(position: Int, parent: RecyclerView): Paint {
                        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                        paint.strokeWidth = 5f
                        paint.color = Color.BLUE
                        paint.style = Paint.Style.STROKE
                        paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
                        return paint
                    }
                })
                .build()
        )
    }

    // 空格
    @SuppressLint("WrongConstant")
    private fun space() {
        recyclerView.layoutManager = LinearLayoutManager(this,  OrientationHelper.VERTICAL, false)
        recyclerView.addItemDecoration(
            LinerItemDecoration.Builder(this, OrientationHelper.VERTICAL)
                .setDividerWidthPx(10)
                .setFooterViewCount(0)
                .setHeadViewCount(0)
                .setDividerMarginPx(10, 10, 10, 10)
                .setDividerSpaceProvider(object : BaseItemDecoration.DividerSpaceProvider{
                    override fun getDividerSpace(position: Int, parent: RecyclerView): Int {
                        return 20+position
                    }

                })
                .build()
        )
    }
}