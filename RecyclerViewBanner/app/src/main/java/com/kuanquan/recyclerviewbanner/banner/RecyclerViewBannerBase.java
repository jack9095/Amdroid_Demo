package com.kuanquan.recyclerviewbanner.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.kuanquan.recyclerviewbanner.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewBannerBase<L extends RecyclerView.LayoutManager, A extends RecyclerView.Adapter> extends FrameLayout {

    protected int autoPlayDuration = 4000;//刷新间隔时间

    protected boolean showIndicator;//是否显示指示器
    protected RecyclerView indicatorContainer;
    protected Drawable mSelectedDrawable;
    protected Drawable mUnselectedDrawable;
    protected IndicatorAdapter indicatorAdapter;
    protected int indicatorMargin;//指示器间距

    protected RecyclerView mRecyclerView;
    protected A adapter;
    protected L mLayoutManager;

    protected int WHAT_AUTO_PLAY = 1000;

    protected boolean hasInit;
    protected int bannerSize = 1;
    protected int currentIndex;
    protected boolean isPlaying;

    protected boolean isAutoPlaying;
    protected List<String> tempUrlList = new ArrayList<>();


    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                mRecyclerView.smoothScrollToPosition(++currentIndex);
                refreshIndicator();
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);

            }
            return false;
        }
    });

    public RecyclerViewBannerBase(Context context) {
        this(context, null);
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewBannerBase);
        showIndicator = a.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true);
        autoPlayDuration = a.getInt(R.styleable.RecyclerViewBannerBase_interval, 4000);
        isAutoPlaying = a.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true);
        mSelectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc);
        mUnselectedDrawable = a.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc);
        if (mSelectedDrawable == null) {
            //绘制默认选中状态图形
            GradientDrawable selectedGradientDrawable = new GradientDrawable();
            selectedGradientDrawable.setShape(GradientDrawable.OVAL);
            selectedGradientDrawable.setColor(Color.RED);
            selectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            selectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mSelectedDrawable = new LayerDrawable(new Drawable[]{selectedGradientDrawable});
        }
        if (mUnselectedDrawable == null) {
            //绘制默认未选中状态图形
            GradientDrawable unSelectedGradientDrawable = new GradientDrawable();
            unSelectedGradientDrawable.setShape(GradientDrawable.OVAL);
            unSelectedGradientDrawable.setColor(Color.GRAY);
            unSelectedGradientDrawable.setSize(dp2px(5), dp2px(5));
            unSelectedGradientDrawable.setCornerRadius(dp2px(5) / 2);
            mUnselectedDrawable = new LayerDrawable(new Drawable[]{unSelectedGradientDrawable});
        }

        indicatorMargin = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorSpace, dp2px(4));
        int marginLeft = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, dp2px(16));
        int marginRight = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginRight, dp2px(0));
        int marginBottom = a.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, dp2px(11));
        int g = a.getInt(R.styleable.RecyclerViewBannerBase_indicatorGravity, 0);
        int gravity;
        if (g == 0) {
            gravity = GravityCompat.START;
        } else if (g == 2) {
            gravity = GravityCompat.END;
        } else {
            gravity = Gravity.CENTER;
        }
        int o = a.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0);
        int orientation = 0;
        if (o == 0) {
            orientation = LinearLayoutManager.HORIZONTAL;
        } else if (o == 1) {
            orientation = LinearLayoutManager.VERTICAL;
        }
        a.recycle();
        //recyclerView部分
        mRecyclerView = new RecyclerView(context);
        initViewPagerScrollProxy();
        new PagerSnapHelper().attachToRecyclerView(mRecyclerView);
        mLayoutManager = getLayoutManager(context, orientation);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onBannerScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                onBannerScrollStateChanged(recyclerView, newState);

            }
        });
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, vpLayoutParams);
        //指示器部分
        indicatorContainer = new RecyclerView(context);

        LinearLayoutManager indicatorLayoutManager = new LinearLayoutManager(context, orientation, false);
        indicatorContainer.setLayoutManager(indicatorLayoutManager);
        indicatorAdapter = new IndicatorAdapter();
        indicatorContainer.setAdapter(indicatorAdapter);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | gravity;
        params.setMargins(marginLeft, 0, marginRight, marginBottom);
        addView(indicatorContainer, params);
        if (!showIndicator) {
            indicatorContainer.setVisibility(GONE);
        }
    }

    protected void onBannerScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    protected void onBannerScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    protected abstract L getLayoutManager(Context context, int orientation);

    protected abstract A getAdapter(Context context, List<String> list, OnBannerItemClickListener onBannerItemClickListener);

    /**
     * 设置轮播间隔时间
     *
     * @param millisecond 时间毫秒
     */
    public void setIndicatorInterval(int millisecond) {
        this.autoPlayDuration = millisecond;
    }

    /**
     * 设置是否自动播放（上锁）
     *
     * @param playing 开始播放
     */
    protected synchronized void setPlaying(boolean playing) {
        if (isAutoPlaying && hasInit) {
            if (!isPlaying && playing ) {
                mHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, autoPlayDuration);
                isPlaying = true;
            } else if (isPlaying && !playing) {
                mHandler.removeMessages(WHAT_AUTO_PLAY);
                isPlaying = false;
            }
        }
    }

    /**
     * 设置是否禁止滚动播放
     */
    public void setAutoPlaying(boolean isAutoPlaying) {
        this.isAutoPlaying = isAutoPlaying;
        setPlaying(this.isAutoPlaying);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 是否显示指示器
     * @param showIndicator true 显示
     */
    public void setShowIndicator(boolean showIndicator) {
        this.showIndicator = showIndicator;
        indicatorContainer.setVisibility(showIndicator ? VISIBLE : GONE);
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(List<String> newList, OnBannerItemClickListener onBannerItemClickListener) {
        //解决recyclerView嵌套问题
        if (compareListDifferent(newList, tempUrlList)) {
            hasInit = false;
            setVisibility(VISIBLE);
            setPlaying(false);
            adapter = getAdapter(getContext(), newList, onBannerItemClickListener);
            mRecyclerView.setAdapter(adapter);
            tempUrlList = newList;
            bannerSize = tempUrlList.size();
            if (bannerSize > 1) {
                indicatorContainer.setVisibility(VISIBLE);
                currentIndex = bannerSize * 10000;
                mRecyclerView.scrollToPosition(currentIndex);
                indicatorAdapter.notifyDataSetChanged();
                setPlaying(true);
            } else {
                indicatorContainer.setVisibility(GONE);
                currentIndex = 0;
            }
            hasInit = true;
        }
        if (!showIndicator) {
            indicatorContainer.setVisibility(GONE);
        }
    }

    /**
     * 设置轮播数据集
     */
    public void initBannerImageView(List<String> newList) {
        initBannerImageView(newList, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        //解决recyclerView嵌套问题
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //解决recyclerView嵌套问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    /**
     * 标示点适配器
     */
    protected class IndicatorAdapter extends RecyclerView.Adapter {

        int currentPosition = 0;

        public void setPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView bannerPoint = new ImageView(getContext());
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(indicatorMargin, indicatorMargin, indicatorMargin, indicatorMargin);
            bannerPoint.setLayoutParams(lp);
            return new RecyclerView.ViewHolder(bannerPoint) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView bannerPoint = (ImageView) holder.itemView;
            bannerPoint.setImageDrawable(currentPosition == position ? mSelectedDrawable : mUnselectedDrawable);

        }

        @Override
        public int getItemCount() {
            return bannerSize;
        }
    }


    /**
     * 改变导航的指示点
     */
    protected synchronized void refreshIndicator() {
        if (showIndicator && bannerSize > 1) {
            indicatorAdapter.setPosition(currentIndex % bannerSize);
            indicatorAdapter.notifyDataSetChanged();
        }
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 获取颜色
     */
    protected int getColor(@ColorRes int color) {
        return ContextCompat.getColor(getContext(), color);
    }

    protected boolean compareListDifferent(List<String> newTabList, List<String> oldTabList) {
        if (oldTabList == null || oldTabList.isEmpty())
            return true;
        if (newTabList.size() != oldTabList.size())
            return true;
        for (int i = 0; i < newTabList.size(); i++) {
            if (TextUtils.isEmpty(newTabList.get(i)))
                return true;
            if (!newTabList.get(i).equals(oldTabList.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
//            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
            mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            LinearLayoutManager o = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(getContext(), o);
            mRecyclerView.setLayoutManager(proxyLayoutManger);

//            Field mRecyclerView = RecyclerView.LayoutManager.class.getDeclaredField("mRecyclerView");
//            mRecyclerView.setAccessible(true);
//            mRecyclerView.set(o, mRecyclerView);

//            Field LayoutMangerField = RecyclerView.class.getDeclaredField("mLayoutManager");
//            LayoutMangerField.setAccessible(true);
//            LayoutMangerField.set(mRecyclerView, proxyLayoutManger);

//            Field pageTransformerAdapterField = RecyclerView.class.getDeclaredField("mPageTransformerAdapter");
//            pageTransformerAdapterField.setAccessible(true);
//            Object mPageTransformerAdapter = pageTransformerAdapterField.get(mRecyclerView);
//            if (mPageTransformerAdapter != null) {
//                Class<?> aClass = mPageTransformerAdapter.getClass();
//                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
//                layoutManager.setAccessible(true);
//                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger);
//            }
//            Field scrollEventAdapterField = RecyclerView.class.getDeclaredField("mScrollEventAdapter");
//            scrollEventAdapterField.setAccessible(true);
//            Object mScrollEventAdapter = scrollEventAdapterField.get(mRecyclerView);
//            if (mScrollEventAdapter != null) {
//                Class<?> aClass = mScrollEventAdapter.getClass();
//                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
//                layoutManager.setAccessible(true);
//                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ProxyLayoutManger extends LinearLayoutManager {
        private final RecyclerView.LayoutManager layoutManager;

        ProxyLayoutManger(Context context, LinearLayoutManager layoutManager) {
            super(context, LinearLayoutManager.HORIZONTAL, false);
            this.layoutManager = layoutManager;
        }

        @Override
        public boolean performAccessibilityAction(@NonNull RecyclerView.Recycler recycler,
                                                  @NonNull RecyclerView.State state, int action, @Nullable Bundle args) {
            return layoutManager.performAccessibilityAction(recycler, state, action, args);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(@NonNull RecyclerView.Recycler recycler,
                                                      @NonNull RecyclerView.State state, @NonNull AccessibilityNodeInfoCompat info) {
            layoutManager.onInitializeAccessibilityNodeInfo(recycler, state, info);
        }

        @Override
        public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
                                                     @NonNull View child, @NonNull Rect rect, boolean immediate,
                                                     boolean focusedChildVisible) {
            return layoutManager.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        }

        @Override
        protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state,
                                                 @NonNull int[] extraLayoutSpace) {
            try {
                Method method = layoutManager.getClass().getDeclaredMethod("calculateExtraLayoutSpace", state.getClass(), extraLayoutSpace.getClass());
                method.setAccessible(true);
                method.invoke(layoutManager, state, extraLayoutSpace);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected int calculateTimeForDeceleration(int dx) {
                    return (int) (pagerScrollDuration * (1 - .3356));
                }
            };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }
    }

    private long pagerScrollDuration = 800; // 两个页面切换时长
}