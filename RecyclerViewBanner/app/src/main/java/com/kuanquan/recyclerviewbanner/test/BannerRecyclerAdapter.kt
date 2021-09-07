package com.kuanquan.recyclerviewbanner.test

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuanquan.recyclerviewbanner.test.BaseBannerRecyclerView.OnBannerItemClickListener

class BannerRecyclerAdapter(
    private var urlList: List<String?>?,
    private var onBannerItemClickListener: OnBannerItemClickListener?
) : RecyclerView.Adapter<BannerRecyclerAdapter.BannerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHolder {
        return BannerHolder(ImageView(parent.context))
    }

    override fun onBindViewHolder(holder: BannerHolder, position: Int) {
        if (urlList == null || urlList!!.isEmpty()) return
        val url = urlList!![position % urlList!!.size]
        val img = holder.itemView as ImageView
        Glide.with(holder.itemView.context).load(url).into(img)
        img.setOnClickListener {
            if (onBannerItemClickListener != null) {
                onBannerItemClickListener!!.onItemClick(position % urlList!!.size)
            }
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    inner class BannerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bannerItem: ImageView = itemView as ImageView

        init {
            val params = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            bannerItem.layoutParams = params
            bannerItem.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    fun setData(urlList: List<String?>?, onBannerItemClickListener: OnBannerItemClickListener?) {
        this.urlList = urlList
        this.onBannerItemClickListener = onBannerItemClickListener
        notifyDataSetChanged()
    }
}