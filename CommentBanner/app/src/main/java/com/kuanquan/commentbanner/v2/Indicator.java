package com.kuanquan.commentbanner.v2;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Px;

/**
 * 可以实现该接口，自定义Indicator 可参考内置的{@link IndicatorView}
 */
public interface Indicator {

    /**
     * 当数据初始化完成时调用
     *
     * @param pagerCount page num
     */
    void initIndicatorCount(int pagerCount);

    /**
     * return View，and add banner
     */
    View getView();

    /**
     * return RelativeLayout.LayoutParams，Set the position of the banner within the RelativeLayout
     */
    RelativeLayout.LayoutParams getParams();

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     * 此方法将在当前页面滚动时调用，无论是作为编程启动的平滑滚动还是用户启动的触摸滚动的一部分。
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels);

    /**
     * 当新页面被选中时，将调用此方法。动画不一定完成
     *
     * @param position Position index of the new selected page.
     */
    void onPageSelected(int position);

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see androidx.viewpager2.widget.ViewPager2#SCROLL_STATE_IDLE
     * @see androidx.viewpager2.widget.ViewPager2#SCROLL_STATE_DRAGGING
     * @see androidx.viewpager2.widget.ViewPager2#SCROLL_STATE_SETTLING
     */
    void onPageScrollStateChanged(int state);
}
