package com.kuanquan.pagetransitionanimation.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.pagetransitionanimation.R
import com.kuanquan.pagetransitionanimation.bean.MainData

class MainAdapter(data: MutableList<MainData>): BaseQuickAdapter<MainData, BaseViewHolder>(R.layout.main_item_layout, data) {

    var mainChildAdapter: MainChildAdapter? = null
    var gridLayoutManager: GridLayoutManager? = null
    var onItemClickListener: OnAdapterItemClickListener? = null

//    private var onClickListener: OnClickListener? = null

//    fun setOnClickListener(onClickListener: OnClickListener) {
//        this.onClickListener = onClickListener
//    }

    override fun convert(holder: BaseViewHolder, item: MainData) {
        holder.setText(R.id.textView, item.title)
        gridLayoutManager = GridLayoutManager(context, 3)

        val recyclerView = holder.getView<RecyclerView>(R.id.recyclerView)

        holder.getView<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = gridLayoutManager
//            if (mainChildAdapter == null) {
                mainChildAdapter = MainChildAdapter(item.list)
//            }
            adapter = mainChildAdapter
        }

        mainChildAdapter?.setOnItemClickListener { adapter, view, position ->
            val imageView = view.findViewById<ImageView>(R.id.image_iv)
            onItemClickListener?.onClick(position, imageView, holder.layoutPosition)
        }
    }

    fun setOnAdapterItemClickListener(onClickListener: OnAdapterItemClickListener) {
        this.onItemClickListener = onClickListener
    }

    interface OnAdapterItemClickListener {
        fun onClick(position: Int, view: View, parentPosition: Int)
    }


}
