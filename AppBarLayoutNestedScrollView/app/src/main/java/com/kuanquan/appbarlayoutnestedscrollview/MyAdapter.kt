package com.kuanquan.appbarlayoutnestedscrollview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(mContext: Context, lists: MutableList<String>): RecyclerView.Adapter<MyAdapter.myViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var datas: MutableList<String>? = null
    private var context: Context? = null

    init {
        context = mContext
        datas = lists
    }

    fun setOnClickListener(mOnClickListener: OnClickListener) {
        onClickListener = mOnClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): myViewHolder {
        return myViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false)
        )
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    fun getData() : MutableList<String>? {
        return datas
    }

    class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.image)
        }
    }

    interface OnClickListener {
        fun onClick(v: View?, position: Int)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        context?.let { holder?.imageView?.let { it1 ->
            Glide.with(it).load(datas?.get(position)).into(it1)
        } }
        holder?.imageView?.setOnClickListener { v ->
            if (onClickListener != null) {
                onClickListener?.onClick(v, holder.adapterPosition)
            }
        }
    }
}