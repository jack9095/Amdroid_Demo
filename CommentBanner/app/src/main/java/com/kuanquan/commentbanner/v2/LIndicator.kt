package com.kuanquan.commentbanner.v2

import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Px

interface LIndicator {

    fun initIndicatorCount(pagerCount: Int)

    fun getView(): View?

    fun getParams(): FrameLayout.LayoutParams?

    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    fun onPageSelected(position: Int)

    fun onPageScrollStateChanged(state: Int)
}