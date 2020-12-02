package com.kuanquan.commentbanner.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.FrameLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.*;
import com.kuanquan.commentbanner.R;
import com.kuanquan.commentbanner.banner.adapter.BannerAdapter;
import com.kuanquan.commentbanner.banner.config.*;
import com.kuanquan.commentbanner.banner.util.*;
import java.lang.ref.WeakReference;

public class Banner<T, BA extends BannerAdapter> extends FrameLayout {
    public static final int INVALID_VALUE = -1;
    private ViewPager2 mViewPager2;
    private AutoLoopTask mLoopTask;
    private BA mAdapter;

    // 是否允许无限轮播（即首尾直接切换）
    private boolean mIsInfiniteLoop = BannerConfig.IS_INFINITE_LOOP;
    // 是否自动轮播
    private boolean mIsAutoLoop = BannerConfig.IS_AUTO_LOOP;
    // 轮播切换间隔时间
    private long mLoopTime = BannerConfig.LOOP_TIME;
    // 轮播切换时间
    private int mScrollTime = BannerConfig.SCROLL_TIME;
    // 轮播开始位置
    private int mStartPosition = 1;

    public static final int HORIZONTAL = 0;

    // 滑动距离范围
    private int mTouchSlop;
    // 记录触摸的位置（主要用于解决事件冲突问题）
    private float mStartX, mStartY;
    // 记录viewpager2是否被拖动
    private boolean mIsViewPager2Drag;
    // 是否要拦截事件
    private boolean isIntercept = true;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() / 2;
        mLoopTask = new AutoLoopTask(this);
        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);
        ScrollSpeedManger.reflectLayoutManager(this);
        addView(mViewPager2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!getViewPager2().isUserInputEnabled()) {
            return super.dispatchTouchEvent(ev);
        }

        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_OUTSIDE) {
            start();
        } else if (action == MotionEvent.ACTION_DOWN) {
            stop();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!getViewPager2().isUserInputEnabled() || !isIntercept) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                if (getViewPager2().getOrientation() == HORIZONTAL) {
                    mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY;
                } else {
                    mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX;
                }
                getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    static class AutoLoopTask implements Runnable {
        private final WeakReference<Banner> reference;

        AutoLoopTask(Banner banner) {
            this.reference = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            Banner banner = reference.get();
            if (banner != null && banner.mIsAutoLoop) {
                int count = banner.getItemCount();
                if (count == 0) {
                    return;
                }
                int next = (banner.getCurrentItem() + 1) % count;
                banner.setCurrentItem(next);
                banner.postDelayed(banner.mLoopTask, banner.mLoopTime);
            }
        }
    }

    /**
     * **********************************************************************
     * ------------------------ 对外公开API ---------------------------------*
     * **********************************************************************
     */

    public int getCurrentItem() {
        return getViewPager2().getCurrentItem();
    }

    public int getItemCount() {
        if (getAdapter() == null) {
            return 0;
        }
        return getAdapter().getItemCount();
    }

    public int getScrollTime() {
        return mScrollTime;
    }

    public boolean isInfiniteLoop() {
        return mIsInfiniteLoop;
    }

    public BA getAdapter() {
        if (mAdapter == null) {
            LogUtils.e(getContext().getString(R.string.banner_adapter_use_error));
        }
        return mAdapter;
    }

    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     */
    public void setCurrentItem(int position) {
        setCurrentItem(position, true);
    }

    /**
     * 跳转到指定位置（最好在设置了数据后在调用，不然没有意义）
     */
    public void setCurrentItem(int position, boolean smoothScroll) {
        getViewPager2().setCurrentItem(position, smoothScroll);
    }

    /**
     * 开始轮播
     */
    public void start() {
        if (mIsAutoLoop) {
            stop();
            postDelayed(mLoopTask, mLoopTime);
        }
    }

    /**
     * 停止轮播
     */
    public void stop() {
        if (mIsAutoLoop) {
            removeCallbacks(mLoopTask);
        }
    }

    /**
     * 移除一些引用
     */
    public void destroy() {
        stop();
    }

    /**
     * 设置banner的适配器
     */
    public void setAdapter(BA adapter) {
        if (adapter == null) {
            throw new NullPointerException(getContext().getString(R.string.banner_adapter_null_error));
        }
        this.mAdapter = adapter;
        if (!isInfiniteLoop()) {
            mAdapter.setIncreaseCount(0);
        }
        mViewPager2.setAdapter(adapter);
        setCurrentItem(mStartPosition, false);
    }

}
