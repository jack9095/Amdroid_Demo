package com.kuanquan.commentbanner.v2

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import android.widget.*
import androidx.appcompat.widget.*
import com.blankj.utilcode.util.SizeUtils

class IndicatorDrawView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr), LIndicator {
    private var selectDrawable: Drawable? = null
    private var normalDrawable: Drawable? = null
    private var alphaDrawable = 0f

    /**
     * param selectPosition 初始选择第几个
     * param indicatorCount 指示器总数量
     */
    fun startIndicator(selectPosition: Int, indicatorCount: Int) {
        for (i in 0 until indicatorCount) {
            val ivIndicator = AppCompatImageView(context)
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.leftMargin = if (i == 0) 0 else SizeUtils.dp2px(6f)
            ivIndicator.layoutParams = lp
            ivIndicator.setBackgroundDrawable(if (i == selectPosition) selectDrawable else normalDrawable)
            ivIndicator.alpha = if (i == selectPosition) 1f else alphaDrawable
            addView(ivIndicator)
        }
    }

    // 在Viewpage的监听中改变选择的指示器 改变指示器选中项
    fun changeIndicator(position: Int) {
        val count = childCount
        for (i in 0 until count) {
            val ivIndecator = getChildAt(i) as AppCompatImageView
            ivIndecator.setBackgroundDrawable(normalDrawable)
            ivIndecator.alpha = alphaDrawable
        }
        val ivIndecator = getChildAt(position) as AppCompatImageView
        ivIndecator.setBackgroundDrawable(selectDrawable)
        ivIndecator.alpha = 1.0f
    }

    override fun initIndicatorCount(pagerCount: Int) {
        visibility = if (pagerCount > 1) View.VISIBLE else View.GONE
        requestLayout()
    }

    override fun getView(): View {
        return this
    }

    /**
     * 控制在banner中的位置
     */
    private var frameLayoutParams: FrameLayout.LayoutParams? = null
    override fun getParams(): FrameLayout.LayoutParams? {
        if (frameLayoutParams == null) {
            frameLayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            frameLayoutParams?.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            frameLayoutParams?.bottomMargin = dip2px(8f)
        }
        return frameLayoutParams
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        changeIndicator(position)
    }

    override fun onPageScrollStateChanged(state: Int) {}
    private fun dip2px(dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    fun setSelectColor(selectDrawable: Drawable?) {
        this.selectDrawable = selectDrawable
    }

    fun setNormalColor(normalDrawable: Drawable?) {
        this.normalDrawable = normalDrawable
    }

    fun setNormalAlpha(alphaDrawable: Float) {
        this.alphaDrawable = alphaDrawable
    }
    
}