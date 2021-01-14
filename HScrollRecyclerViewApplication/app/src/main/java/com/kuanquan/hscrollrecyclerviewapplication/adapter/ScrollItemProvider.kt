package com.kuanquan.hscrollrecyclerviewapplication.adapter

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.hscrollrecyclerviewapplication.R
import com.kuanquan.hscrollrecyclerviewapplication.bean.TestBean

class ScrollItemProvider : BaseItemProvider<TestBean>() {

    override val itemViewType: Int =
        TestBean.TYPE_SCROLL

    override val layoutId: Int =
        R.layout.item_scroll_layout

    override fun convert(helper: BaseViewHolder, item: TestBean) {
        val lrv = helper.getView<RecyclerView>(R.id.leftRecyclerView)
        val rrv = helper.getView<RecyclerView>(R.id.rightRecyclerView)

        val lp = lrv.layoutParams as RelativeLayout.LayoutParams

        if (item.id == "101") {
            lrv.layoutManager = LinearLayoutManager(context)
            rrv.visibility = View.VISIBLE
            helper.setGone(R.id.title_tv_follow, false)
            helper.setGone(R.id.title_tv_follow_right, false)
            lp.width = dp2px(context, 240f)
        } else {
            rrv.visibility = View.GONE
            helper.setGone(R.id.title_tv_follow, true)
            helper.setGone(R.id.title_tv_follow_right, true)
            lrv.layoutManager = GridLayoutManager(context,2)
//            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.width = screenWidth() - dp2px(context, 32f)
        }

        lrv.layoutParams = lp
        lrv.adapter = ChildAdapter(getData())

        rrv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = ChildAdapter(getData())
        }
    }


    private fun getData(): MutableList<String>{
        val list = mutableListOf<String>()
        for (i in 0..3){
            list.add("火山小视频$i")
        }
        return list
    }

    private fun dp2px(context: Context?, dp: Float): Int {
        if (context == null) {
            return (dp * 3).toInt()
        }
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
        return Math.round(px)
    }

    private fun screenWidth(): Int{
        val outMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        return widthPixels
    }
}