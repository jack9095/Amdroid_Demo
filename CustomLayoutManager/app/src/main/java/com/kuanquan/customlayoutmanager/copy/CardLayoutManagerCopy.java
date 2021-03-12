package com.kuanquan.customlayoutmanager.copy;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.customlayoutmanager.stack.CardConfig;

public class CardLayoutManagerCopy extends RecyclerView.LayoutManager {
//public class CardLayoutManager extends LinearLayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    public void setParam(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
        this.mRecyclerView = checkIsNull(recyclerView);
        this.mItemTouchHelper = checkIsNull(itemTouchHelper);
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(final RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        // 当数据源个数大于最大显示数时
        if (itemCount > CardConfig.DEFAULT_SHOW_ITEM) {
            for (int position = CardConfig.DEFAULT_SHOW_ITEM; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (position == CardConfig.DEFAULT_SHOW_ITEM) {
                    view.setScaleX(1 - (position - 1) * CardConfig.DEFAULT_SCALE_X);
                    view.setScaleY(1 - (position - 1) * CardConfig.DEFAULT_SCALE_Y);
//                    view.setTranslationY((position - 1) * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                    view.setTranslationX((position - 1) * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE_X);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE_Y);
//                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                    view.setTranslationX(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
//                    view.setOnClickListener(clicklistener);

                }
            }
        } else {
            // 当数据源个数小于或等于最大显示数时
            for (int position = itemCount - 1; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (position > 0) {
                    view.setScaleX(1 - position * CardConfig.DEFAULT_SCALE_X);
                    view.setScaleY(1 - position * CardConfig.DEFAULT_SCALE_Y);
//                    view.setTranslationY(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                    view.setTranslationX(position * view.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
//                    view.setOnClickListener(clicklistener);
                }
            }
        }
    }

//    @Override
//    public boolean canScrollHorizontally() {
//        return true;
//    }

    float x;
    float y;

    private float mTouchStartX;
    private float mTouchStartY;
    private float startX;
    private float startY;
    private RecyclerView.ViewHolder childViewHolder;
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 获取相对屏幕的坐标，即以屏幕左上角为原点
            x = event.getRawX();
            y = event.getRawY() - 25; // 25是系统状态栏的高度

            childViewHolder = mRecyclerView.getChildViewHolder(v);
//            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
////                mItemTouchHelper.startSwipe(childViewHolder);
//
//            } else if (childViewHolder == null) {
//
//            }

            switch (MotionEventCompat.getActionMasked(event)) {
                case MotionEvent.ACTION_DOWN:
//                    mItemTouchHelper.startSwipe(childViewHolder);
                    x = event.getX();
                    y = event.getY();

                    startX = x;
                    startY = y;
                    // 获取相对View的坐标，即以此View左上角为原点
                    mTouchStartX = event.getX();
                    mTouchStartY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float xM = event.getX();
                    float yM = event.getY();
//                    if (Math.abs(x - xM) <  Math.abs(y - yM)){
//                        mRecyclerView.requestDisallowInterceptTouchEvent(false);
//                       return false;
//                    } else {
//                        mRecyclerView.requestDisallowInterceptTouchEvent(true);
//                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //关键部分：移动距离较小，视为onclick点击行为
                    if (Math.abs(x - startX) < 1.5 && Math.abs(y - startY) < 1.5){
//                        Toast.makeText(v.getContext(),"点击事件",Toast.LENGTH_SHORT).show();
                    }
                    mTouchStartX = mTouchStartY = 0;
                    break;
            }
            boolean b = gestureDetector.onTouchEvent(event);

            return true;
        }
    };

    private GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(mRecyclerView.getContext(),"点击事件",Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mItemTouchHelper.startSwipe(childViewHolder);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return false;
        }
    });

}
