package com.kuanquan.afewscreens

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kuanquan.afewscreens.databinding.ActivityMainBinding
import com.kuanquan.afewscreens.glide.GlideApp


/**
 * https://blog.csdn.net/u012864297/article/details/107247756/
 * 某个activity不想用viewBinding
 * 在相应的布局下配置
    tools:viewBindingIgnore="true"
 */
class MainActivity : AppCompatActivity() {

    var imageUrls = mutableListOf(
            "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2771978851,2906984932&fm=26&gp=0.jpg",
            "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1819216937,2118754409&fm=26&gp=0.jpg",
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2797486824,1669366989&fm=26&gp=0.jpg",
            "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=188173295,510375359&fm=26&gp=0.jpg",
            "https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2853553659,1775735885&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1642005357,2652294048&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1874734052,880491637&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1478872620,3991387917&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3605466499,2642010544&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1774415219,3140490468&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1344801497,3054664199&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1320650709,3535973136&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3601466020,2387133791&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1864477876,3688010859&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3096767721,1517595873&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1924411978,2074577357&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1937885777,439518627&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3114284585,3252063096&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1744329700,1413485322&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1684231733,1620228646&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3258484351,1612376358&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1408371311,2263836872&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2482360593,2739674846&fm=26&gp=0.jpg"
    )

    private lateinit var viewBinding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels
        val heightPixels = outMetrics.heightPixels
        Log.e("屏幕的高度 = ", "$heightPixels")

        // 亲测有效（LinearLayoutManager）
        viewBinding.recyclerView.setOnScrollChangeListener(object: View.OnScrollChangeListener{

            override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                val offset: Int = viewBinding.recyclerView.computeVerticalScrollOffset()
                Log.e("滑动距离监听 = ", " \n scrollY -> $scrollY \n oldScrollY -> $oldScrollY \n offset -> $offset")

                if (offset >= heightPixels * 3) {
                    Log.e("滑出三屏幕了 = ", "$offset")
                }

                // 如果是垂直滑动，获取垂直滑动距离
                val distance: Float = dp2px(48f)
                if (offset <= distance) {
                    val percentage = 1 - offset / distance
//                    colorEvaluator(percentage)
                }

            }
        })

        val getHeightView = View(this@MainActivity)

        with(viewBinding.recyclerView){
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ImageAdapter(imageUrls)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var _firstItemPosition = -1
                private  var _lastItemPosition:Int = 0
                private var fistView: View? = null
                private  var lastView: View? = null
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    getHeightView.scrollBy(0, dy)
                    Log.e("滑动高度", "${getHeightView.scrollY}")
                    Log.e("dy = ", "$dy")


                    val layoutManager = recyclerView.layoutManager
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    if (layoutManager is LinearLayoutManager) {
                        //获取最后一个可见view的位置
                        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                        //获取第一个可见view的位置
                        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                        //获取可见view的总数
                        val visibleItemCount = layoutManager.childCount
                        if (_firstItemPosition < firstItemPosition) {
                            _firstItemPosition = firstItemPosition
                            _lastItemPosition = lastItemPosition
//                            GCView(fistView)
                            fistView = recyclerView.getChildAt(0)
                            lastView = recyclerView.getChildAt(visibleItemCount - 1)
                        } else if (_lastItemPosition > lastItemPosition) {
                            _firstItemPosition = firstItemPosition
                            _lastItemPosition = lastItemPosition
//                            GCView(lastView)
                            fistView = recyclerView.getChildAt(0)
                            lastView = recyclerView.getChildAt(visibleItemCount - 1)
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }

        val drawable = CommonShapeBuilder()
                .setCornerRadius(CommonShapeBuilder.ANGLE_TOP_LEFT, dp2px(100f))
                .setCornerRadius(CommonShapeBuilder.ANGLE_BOTTOM_RIGHT, dp2px(100f))
                .setGradientColors(intArrayOf(Color.parseColor("#637DFF"), Color.parseColor("#B57AFF")))
                .setOrientation(GradientDrawable.Orientation.LEFT_RIGHT).build()

        viewBinding.ll.background = drawable
    }

    internal class ImageAdapter(private val items: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_image, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageViewHolder = holder as ImageViewHolder
            // 最原始的加载，图片多和大的时候会卡顿
//            Glide.with(imageViewHolder.image)
//                    .load(items[position])
//                    .apply(RequestOptions())
//                    .transform(RoundedCorners(10))
//                    .into(imageViewHolder.image)

            // 优化后的，不卡顿
            GlideApp.with(imageViewHolder.image)
                    .asDrawable()
                    .load(items[position])
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .apply(RequestOptions().priority(Priority.IMMEDIATE))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .transform(RoundedCorners(1))
                    .into(imageViewHolder.image)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            val imageViewHolder = holder as ImageViewHolder
            GlideApp.with(imageViewHolder.image).clear(imageViewHolder.image)
        }
    }

    internal class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img)
    }

    private fun dp2px(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }
}