//package com.shuashuakan.android.modules.publisher
//
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.request.RequestOptions
//import com.chad.library.adapter.base.BaseQuickAdapter
//import com.chad.library.adapter.base.BaseViewHolder
//import com.qiniu.pili.droid.shortvideo.PLBuiltinFilter
//import com.shuashuakan.android.R
//import com.kuanquan.doyincover.publisher.utils.StringUtil
//import com.shuashuakan.android.modules.widget.transform.GlideCircleBorderTransform
//
///**
// * Author:  Chenglong.Lu
// * Email:   1053998178@qq.com | w490576578@gmail.com
// * Date:    2018/12/17
// * Description:
// */
//class FilterListDialogAdapter : BaseQuickAdapter<PLBuiltinFilter, BaseViewHolder>(R.layout.item_filter_list) {
//
//  var selectPosition = 0
//
//  override fun convert(helper: BaseViewHolder, item: PLBuiltinFilter) {
//    val image = helper.getView<ImageView>(R.id.image)
//    val transform = if (selectPosition == helper.adapterPosition) {
//      helper.setTextColor(R.id.text, image.context.getColor1(R.color.color_ffef30))
//        GlideCircleBorderTransform(mContext, 2, image.context.getColor1(R.color.color_ffef30))
//    } else {
//      helper.setTextColor(R.id.text, image.context.getColor1(R.color.white_color_50))
//        GlideCircleBorderTransform(mContext, 0, image.context.getColor1(R.color.transparent))
//    }
//
//    if (item.name == "none"){
//      Glide.with(image.context).load("file:///android_asset/normal.png").apply(RequestOptions()
//          .transform(transform).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(image)
//    }else{
//      Glide.with(image.context).load("file:///android_asset/${item.assetFilePath}").apply(RequestOptions()
//          .transform(transform).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(image)
//    }
//    helper.setText(R.id.text, StringUtil.getChineseName(item.name,helper.itemView.context))
//  }
//}
