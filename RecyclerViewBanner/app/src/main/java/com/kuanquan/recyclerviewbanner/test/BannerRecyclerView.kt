package com.kuanquan.recyclerviewbanner.test

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BannerRecyclerView: BaseBannerRecyclerView<LinearLayoutManager, BannerRecyclerAdapter> {

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onBannerScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onBannerScrolled(recyclerView, dx, dy)
        // 解决连续滑动时指示器不更新的问题
        if (mBannerSize < 2) return
        val firstReal = mLayoutManager?.findFirstVisibleItemPosition() ?: 0
        val viewFirst = mLayoutManager?.findViewByPosition(firstReal)
        val width = width.toFloat()
        if (width != 0f && viewFirst != null) {
            val right = viewFirst.right.toFloat()
            val ratio = right / width
            if (ratio > 0.8) {
                if (mCurrentIndex != firstReal) {
                    mCurrentIndex = firstReal
                    refreshIndicator()
                }
            } else if (ratio < 0.2) {
                if (mCurrentIndex != firstReal + 1) {
                    mCurrentIndex = firstReal + 1
                    refreshIndicator()
                }
            }
        }
    }

    override fun onBannerScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onBannerScrollStateChanged(recyclerView, newState)
        val first = mLayoutManager?.findFirstVisibleItemPosition() ?: 0
        val last = mLayoutManager?.findLastVisibleItemPosition()
        if (mCurrentIndex != first && first == last) {
            mCurrentIndex = first
            refreshIndicator()
        }
    }

    override fun getLayoutManager(context: Context?, orientation: Int): LinearLayoutManager {
        return LinearLayoutManager(context, orientation, false)
    }

    override fun getAdapter(
        context: Context?, list: MutableList<String>?, onBannerItemClickListener: OnBannerItemClickListener?
    ): BannerRecyclerAdapter {
        return BannerRecyclerAdapter(list,onBannerItemClickListener)
    }
}