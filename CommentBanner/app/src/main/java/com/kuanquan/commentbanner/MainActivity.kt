/*
    val adapter = BannerImageAdapter(imageUrls)
        banner?.let {
            it.addBannerLifecycleObserver(this)
            it.indicator = RectangleIndicator(this)
            it.setBannerRound(0f)
            it.setAdapter(adapter)
        }
        banner?.setAdapter(bannerAdapter)
        bannerAdapter.setNewInstance(imageUrls)
 */
package com.kuanquan.commentbanner

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.kuanquan.commentbanner.banner.BannerQuickAdapter
import com.kuanquan.commentbanner.v2.IndicatorDrawView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var imageUrls = mutableListOf(
        "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2771978851,2906984932&fm=26&gp=0.jpg",
        "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1819216937,2118754409&fm=26&gp=0.jpg",
        "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2797486824,1669366989&fm=26&gp=0.jpg",
        "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=188173295,510375359&fm=26&gp=0.jpg",
        "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2853553659,1775735885&fm=26&gp=0.jpg",
        "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2482360593,2739674846&fm=26&gp=0.jpg"
    )
//    fun Int.res2Drawable(): Drawable? = ContextCompat.getDrawable(this@MainActivity, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val selectDrawble = ContextCompat.getDrawable(this@MainActivity, R.drawable.selected)
        val normalDrawble = ContextCompat.getDrawable(this@MainActivity, R.drawable.nomal)
        val bannerAdapter = BannerQuickAdapter(imageUrls)
        val indicator by lazy {
            IndicatorDrawView(this@MainActivity).apply {
                setSelectColor(selectDrawble)  //被选中后的样式
                setNormalColor(normalDrawble)  //未选中的样式
                setNormalAlpha(0.6f) //未选中的透明度 0 ~ 1
                startIndicator(0, imageUrls.size)
            }
        }

        banner?.run {
//            setIndicator(indicator
//                    IndicatorDrawView(this@MainActivity)
//                    .setIndicatorColor(Color.GRAY)
//                    .setIndicatorSelectorColor(Color.WHITE)
//                    .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE)
//            )
            setIndicator(indicator)
            setOuterPageChangeListener(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                    indicator.changeIndicator(position)
                }
            })
            setRoundCorners(8f)
            adapter = bannerAdapter
//            setAdapter(ImageAdapter(imageUrls), 0)
        }

//        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
//        indicator.layoutParams = layoutParams
//        frameLayout.addView(indicator)
    }

    internal class ImageAdapter(private val items: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_image, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageViewHolder = holder as ImageViewHolder
            Glide.with(imageViewHolder.image)
                    .load(items[position])
                    //  .apply(new RequestOptions()
                    //  .transform(new RoundedCorners(Utils.dp2px(10))))
                    .into(imageViewHolder.image)
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    internal class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img)

    }
}