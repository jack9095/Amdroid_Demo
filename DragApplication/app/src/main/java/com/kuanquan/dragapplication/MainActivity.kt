package com.kuanquan.dragapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.kuanquan.dragapplication.divider.BaseItemDecoration
import com.kuanquan.dragapplication.divider.GridItemDecoration
import com.kuanquan.dragapplication.test.Pic
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mainAdapter by lazy {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val width = outMetrics.widthPixels
        val imageWidth = (width - dip2px(this,48f)) / 3
        MainAdapter(imageWidth)
    }

    var count = 9

    private fun RecyclerView.addInsert(spanCount: Int) {
        if (itemDecorationCount == 0) {
            val mSpacing = dip2px(this@MainActivity, 8f)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)
                    val column = position % spanCount
                    outRect.left = column * mSpacing / spanCount
                    outRect.right = mSpacing - (column + 1) * mSpacing / spanCount
                    outRect.top = mSpacing
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        with(recyclerView) {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            addInsert(3)
//            addItemDecoration(
//                GridItemDecoration.Builder(this@MainActivity, OrientationHelper.VERTICAL)
//                    .setDividerWidthPx(dip2px(this@MainActivity, 0f)) // 分割线的宽度 单位px
//                    .setDividerMarginPx(dip2px(this@MainActivity, 4f), dip2px(this@MainActivity, 8f), dip2px(this@MainActivity, 4f), 0) // 设置分割线距离item的间隔
//                    .setDividerColorProvider(object :
//                        BaseItemDecoration.DividerColorProvider {
//                        override fun getDividerColor(position: Int, parent: RecyclerView): Int {
//                            return Color.parseColor("#ffffff")
//                        }
//                    })
//                    .build()
//            )
            adapter = mainAdapter
        }

        val lists = mutableListOf<Pic>()
        var pic: Pic?
        for (i in 0..8) {
            pic = Pic()
            pic.id = i
            pic.path = ""
            lists.add(pic)
        }
        mainAdapter.list = lists as java.util.ArrayList<Pic>?

        mainAdapter.setPicClickListener(object : MainAdapter.PicClickListener {
            override fun onPicClick(view: View?, pos: Int) {
                Toast.makeText(
                    applicationContext,
                    "pos:" + pos + " id:" + mainAdapter.list[pos].id,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAddClick(view: View?) {
                val pic = Pic()
                pic.id = count
                mainAdapter.addItem(pic)
                count++
                        recyclerView.smoothScrollToPosition(mainAdapter.itemCount - 1)
                recyclerView.postDelayed(object : Runnable {
                    override fun run() {
                    }
                }, 300)
            }
        })

        val dragHelperCallback = DragHelperCallback(mainAdapter, deleteArea)
        ItemTouchHelper(dragHelperCallback).attachToRecyclerView(recyclerView)
        dragHelperCallback.setDragListener(object : DragHelperCallback.DragListener {
            override fun onDragFinish(isInside: Boolean) {
                deleteArea.visibility = View.INVISIBLE
                deleteArea.alpha = 0.8f
                deleteTv.text = resources.getString(R.string.post_delete_tv_d)
            }

            override fun onDragAreaChange(isInside: Boolean, isIdle: Boolean) {
                if (isIdle) return
                if (isInside) { // 删除区域
                    deleteArea.alpha = 1.0f
                    deleteTv.text = resources.getString(R.string.post_delete_tv_s)
                } else {
                    deleteArea.alpha = 0.8f
                    deleteTv.text = resources.getString(R.string.post_delete_tv_d)
                }
            }

            override fun onDragStart() {
                deleteArea.visibility = View.VISIBLE
            }
        })
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (pxValue / scale + 0.5f).toInt()
    }
}