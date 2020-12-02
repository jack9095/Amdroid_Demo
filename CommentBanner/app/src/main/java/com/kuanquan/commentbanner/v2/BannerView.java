//package com.kuanquan.commentbanner.v2;
//
//import android.content.Context;
//import android.graphics.Outline;
//import android.util.AttributeSet;
//import android.view.*;
//import android.widget.FrameLayout;
//import androidx.annotation.*;
//import androidx.recyclerview.widget.*;
//import androidx.recyclerview.widget.RecyclerView.ViewHolder;
//import androidx.viewpager2.widget.*;
//import java.lang.reflect.Field;
//
//public class BannerView extends FrameLayout {
//
//    private static final long DEFAULT_AUTO_TIME = 2500; // 默认自动滚动的时间值
//    private static final long DEFAULT_PAGER_DURATION = 800; // ViewPager2 切换时长
//    private static final int NORMAL_COUNT = 2;
//
//    private ViewPager2.OnPageChangeCallback changeCallback;
//    private BannerAdapterWrapper adapterWrapper;
//    private Indicator indicator;
//    private ViewPager2 viewPager2;
//    private boolean isAutoPlay = true; // 是否自动滚动
//    private boolean isBeginPagerChange = true;
//    private long autoTurningTime = DEFAULT_AUTO_TIME;
//    private long pagerScrollDuration = DEFAULT_PAGER_DURATION;
//    private int needPage = NORMAL_COUNT;
//    private int sidePage = needPage / NORMAL_COUNT;
//    private int tempPosition;
//
//    private float startX, startY, lastX, lastY;
//    private final int scaledTouchSlop;
//
//    public BannerView(Context context) {
//        this(context, null);
//    }
//
//    public BannerView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        // 它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
//        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() >> 1;
//        initViews(context);
//    }
//
//    private void initViews(final Context context) {
//        viewPager2 = new ViewPager2(context);
//        viewPager2.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        viewPager2.setPageTransformer(new CompositePageTransformer());
//        viewPager2.registerOnPageChangeCallback(new OnPageChangeCallback());
//        viewPager2.setAdapter(adapterWrapper = new BannerAdapterWrapper());
//        setOffscreenPageLimit(2);
//        initViewPagerScrollProxy();
//        addView(viewPager2);
//    }
//
//    private void startPager(int startPosition) {
//        tempPosition = startPosition + sidePage;
//        viewPager2.setCurrentItem(tempPosition, false);
//        adapterWrapper.notifyDataSetChanged();
//        if (indicator != null) {
//            indicator.initIndicatorCount(getRealCount());
//        }
//        if (isAutoPlay()) {
//            startTurning();
//        }
//    }
//
//    private int getRealCount() {
//        return adapterWrapper.getRealCount();
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (isAutoPlay()) {
//            startTurning();
//        }
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (isAutoPlay()) {
//            stopTurning();
//        }
//    }
//
//    private final Runnable task = new Runnable() {
//        @Override
//        public void run() {
//            if (isAutoPlay()) {
//                tempPosition++;
//                if (tempPosition == getRealCount() + sidePage + 1) {
//                    isBeginPagerChange = false;
//                    viewPager2.setCurrentItem(sidePage, false);
//                    post(task);
//                } else {
//                    isBeginPagerChange = true;
//                    viewPager2.setCurrentItem(tempPosition);
//                    postDelayed(task, autoTurningTime);
//                }
//            }
//        }
//    };
//
//    private int toRealPosition(int position) {
//        int realPosition = 0;
//        if (getRealCount() > 1) {
//            realPosition = (position - sidePage) % getRealCount();
//        }
//        if (realPosition < 0) {
//            realPosition += getRealCount();
//        }
//        return realPosition;
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (isAutoPlay() && viewPager2.isUserInputEnabled()) {
//            int action = ev.getAction();
//            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
//                    || action == MotionEvent.ACTION_OUTSIDE) {
//                startTurning();
//            } else if (action == MotionEvent.ACTION_DOWN) {
//                stopTurning();
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            startX = lastX = ev.getRawX();
//            startY = lastY = ev.getRawY();
//        } else if (action == MotionEvent.ACTION_MOVE) {
//            lastX = ev.getRawX();
//            lastY = ev.getRawY();
//            if (viewPager2.isUserInputEnabled()) {
//                float distanceX = Math.abs(lastX - startX);
//                float distanceY = Math.abs(lastY - startY);
//                boolean disallowIntercept;
//                if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    disallowIntercept = distanceX > scaledTouchSlop && distanceX > distanceY;
//                } else {
//                    disallowIntercept = distanceY > scaledTouchSlop && distanceY > distanceX;
//                }
//                getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
//            }
//        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
//            return Math.abs(lastX - startX) > scaledTouchSlop || Math.abs(lastY - startY) > scaledTouchSlop;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//
//    private class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            int realPosition = toRealPosition(position);
//            if (changeCallback != null) {
//                changeCallback.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
//            }
//            if (indicator != null) {
//                indicator.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
//            }
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            if (getRealCount() > 1) {
//                tempPosition = position;
//            }
//            if (isBeginPagerChange) {
//                int realPosition = toRealPosition(position);
//                if (changeCallback != null) {
//                    changeCallback.onPageSelected(realPosition);
//                }
//                if (indicator != null) {
//                    indicator.onPageSelected(realPosition);
//                }
//            }
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
//                if (tempPosition == sidePage - 1) {
//                    isBeginPagerChange = false;
//                    viewPager2.setCurrentItem(getRealCount() + tempPosition, false);
//                } else if (tempPosition == getRealCount() + sidePage) {
//                    isBeginPagerChange = false;
//                    viewPager2.setCurrentItem(sidePage, false);
//                } else {
//                    isBeginPagerChange = true;
//                }
//            }
//            if (changeCallback != null) {
//                changeCallback.onPageScrollStateChanged(state);
//            }
//            if (indicator != null) {
//                indicator.onPageScrollStateChanged(state);
//            }
//        }
//    }
//
//    private class BannerAdapterWrapper extends RecyclerView.Adapter<ViewHolder> {
//
//        private RecyclerView.Adapter adapter;
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return adapter.onCreateViewHolder(parent, viewType);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            adapter.onBindViewHolder(holder, toRealPosition(position));
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return adapter.getItemViewType(toRealPosition(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return getRealCount() > 1 ? getRealCount() + needPage : getRealCount();
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return adapter.getItemId(toRealPosition(position));
//        }
//
//        int getRealCount() {
//            return adapter == null ? 0 : adapter.getItemCount();
//        }
//
//        void registerAdapter(RecyclerView.Adapter adapter) {
//            if (this.adapter != null) {
//                this.adapter.unregisterAdapterDataObserver(itemDataSetChangeObserver);
//            }
//            this.adapter = adapter;
//            if (this.adapter != null) {
//                this.adapter.registerAdapterDataObserver(itemDataSetChangeObserver);
//            }
//        }
//    }
//
//    private RecyclerView.AdapterDataObserver itemDataSetChangeObserver = new RecyclerView.AdapterDataObserver() {
//
//        @Override
//        public final void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) { onChanged(); }
//
//        @Override
//        public final void onItemRangeInserted(int positionStart, int itemCount) {
//            if (positionStart > 1) onChanged();
//        }
//
//        @Override
//        public final void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) { onChanged(); }
//
//        @Override
//        public void onChanged() {
//            startPager(getCurrentPager());
//        }
//    };
//
//    private void initViewPagerScrollProxy() {
//        try {
//            //控制切换速度，采用反射方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
//            RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
//            recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//            ProxyLayoutManger proxyLayoutManger = new ProxyLayoutManger(getContext(), viewPager2.getOrientation());
//            recyclerView.setLayoutManager(proxyLayoutManger);
//
//            Field LayoutMangerField = ViewPager2.class.getDeclaredField("mLayoutManager");
//            LayoutMangerField.setAccessible(true);
//            LayoutMangerField.set(viewPager2, proxyLayoutManger);
//
//            Field pageTransformerAdapterField = ViewPager2.class.getDeclaredField("mPageTransformerAdapter");
//            pageTransformerAdapterField.setAccessible(true);
//            Object mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2);
//            if (mPageTransformerAdapter != null) {
//                Class<?> aClass = mPageTransformerAdapter.getClass();
//                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
//                layoutManager.setAccessible(true);
//                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger);
//            }
//            Field scrollEventAdapterField = ViewPager2.class.getDeclaredField("mScrollEventAdapter");
//            scrollEventAdapterField.setAccessible(true);
//            Object mScrollEventAdapter = scrollEventAdapterField.get(viewPager2);
//            if (mScrollEventAdapter != null) {
//                Class<?> aClass = mScrollEventAdapter.getClass();
//                Field layoutManager = aClass.getDeclaredField("mLayoutManager");
//                layoutManager.setAccessible(true);
//                layoutManager.set(mScrollEventAdapter, proxyLayoutManger);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private class ProxyLayoutManger extends LinearLayoutManager {
//
//        ProxyLayoutManger(Context context, int orientation) {
//            super(context, orientation, false);
//        }
//
//        @Override
//        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//            LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
//                @Override
//                protected int calculateTimeForDeceleration(int dx) {
//                    return (int) (pagerScrollDuration * (1 - .3356));
//                }
//            };
//            linearSmoothScroller.setTargetPosition(position);
//            startSmoothScroll(linearSmoothScroller);
//        }
//    }
//
//    /*--------------- 下面是对外暴露的方法 ---------------*/
//
//    public BannerView setAutoTurningTime(long autoTurningTime) {
//        this.autoTurningTime = autoTurningTime;
//        return this;
//    }
//
//    public BannerView setOuterPageChangeListener(ViewPager2.OnPageChangeCallback listener) {
//        this.changeCallback = listener;
//        return this;
//    }
//
//    public BannerView setOffscreenPageLimit(int limit) {
//        viewPager2.setOffscreenPageLimit(limit);
//        return this;
//    }
//
//    /**
//     * 设置viewpager2的切换时长
//     */
//    public BannerView setPagerScrollDuration(long pagerScrollDuration) {
//        this.pagerScrollDuration = pagerScrollDuration;
//        return this;
//    }
//
//    /**
//     * 是否自动轮播 大于1页轮播才生效
//     */
//    public BannerView setAutoPlay(boolean autoPlay) {
//        isAutoPlay = autoPlay;
//        if (isAutoPlay && getRealCount() > 1) {
//            startTurning();
//        }
//        return this;
//    }
//
//    public boolean isAutoPlay() {
//        return isAutoPlay && getRealCount() > 1;
//    }
//
//    public BannerView setIndicator(Indicator indicator) {
//        return setIndicator(indicator, true);
//    }
//
//    /**
//     * 设置indicator，支持在xml中创建
//     *
//     * @param attachToRoot true 添加到banner布局中
//     */
//    public BannerView setIndicator(Indicator indicator, boolean attachToRoot) {
//        if (this.indicator != null) {
//            removeView(this.indicator.getView());
//        }
//        if (indicator != null) {
//            this.indicator = indicator;
//            if (attachToRoot) {
//                addView(this.indicator.getView(), this.indicator.getParams());
//            }
//        }
//        return this;
//    }
//
//    /**
//     * 设置banner圆角 api21以上
//     */
//    public BannerView setRoundCorners(final float radius) {
//        setOutlineProvider(new ViewOutlineProvider() {
//            @Override
//            public void getOutline(View view, Outline outline) {
//                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
//            }
//        });
//        setClipToOutline(true);
//        return this;
//    }
//
//    /**
//     * 返回真实位置
//     */
//    public int getCurrentPager() {
//        int position = toRealPosition(tempPosition);
//        return Math.max(position, 0);
//    }
//
//    public RecyclerView.Adapter getAdapter() {
//        return adapterWrapper.adapter;
//    }
//
//    public void startTurning() {
//        stopTurning();
//        postDelayed(task, autoTurningTime);
//    }
//
//    public void stopTurning() {
//        removeCallbacks(task);
//    }
//
//    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
//        setAdapter(adapter, 0);
//    }
//
//    public void setAdapter(@Nullable RecyclerView.Adapter adapter, int startPosition) {
//        adapterWrapper.registerAdapter(adapter);
//        startPager(startPosition);
//    }
//}
