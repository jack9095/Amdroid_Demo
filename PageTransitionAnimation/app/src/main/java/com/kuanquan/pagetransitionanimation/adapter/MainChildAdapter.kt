package com.kuanquan.pagetransitionanimation.adapter

import android.content.Intent
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kuanquan.pagetransitionanimation.R
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity

class MainChildAdapter(data: MutableList<String>?): BaseQuickAdapter<String, BaseViewHolder>(R.layout.main_child_item_layout, data) {

    override fun convert(holder: BaseViewHolder, item: String) {

        val imageView = holder.getView<ImageView>(R.id.image)
        val mImageView = holder.getView<ImageView>(R.id.image_iv)

        Glide.with(context).load(item).into(imageView)

        // TODO 2. 共享元素动画
        Glide.with(context).load(item).into(mImageView)
    }
}