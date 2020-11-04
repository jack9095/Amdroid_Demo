package com.kuanquan.dragapplication;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.*;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import com.kuanquan.dragapplication.test.Pic;
import java.util.*;

/**
 * 拖拽帮助类
 * https://blog.csdn.net/wuyuxing24/article/details/78985026
 */
public class DragHelperCallback extends ItemTouchHelper.Callback {

    private MainAdapter mAdapter;
    private View delArea;  // 删除区域
    private DragListener mDragListener;
    private boolean mIsInside = false;
    private int delPos = -1; // 可能要被移除的的 itemView 的 position
    private RecyclerView.ViewHolder tempHolder;
    private float mScale = 1.2f;  // 设置选中放大的倍数

    public DragHelperCallback(@NonNull MainAdapter adapter, View delArea) {
        mAdapter = adapter;
        this.delArea = delArea;
    }

    /**
     * 设置滑动类型标记
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     *          返回一个整数类型的标识，用于判断Item哪种移动行为是允许的
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        if (viewHolder instanceof MainAdapter.PicAddViewHolder) {
            return 0;
        }
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 拖拽切换Item的回调
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     *          如果Item切换了位置，返回true；反之，返回false
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (target instanceof MainAdapter.PicAddViewHolder) {
            return false;
        }
        ArrayList<Pic> list = mAdapter.getList();
        if (list == null || list.size() < 2) {
            return false;
        }

        // 返回数据在Adapter中的位置（也许位置的变化还未来得及刷新到布局中），
        // 当使用Adapter的时候（例如调用Adapter的notify相关方法时）考虑使用
        int from = viewHolder.getAdapterPosition();  // 这里指拖拽移动前的位置
        int endPosition = target.getAdapterPosition(); // 这里指拖拽移动后的位置
        Log.d("jiabin", "onMove from:" + from + " end:" + endPosition);
        delPos = endPosition;

        // 交换在指定列表中的指定位置的元素
        if (from < endPosition) {
            for (int i = from; i < endPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = from; i > endPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }

        // 移动指定未知的元素并更新
        mAdapter.notifyItemMoved(from, endPosition);
        return true;
    }

    /**
     * 针对swipe和drag状态，当手指离开之后，view回到指定位置动画的持续时间(swipe可能是回到原位，也有可能是swipe掉)
     */
    @Override
    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        if (mIsInside) {
            return 0;
        }
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    /**
     * 执行 itemView 上的动画  这里是整个 item 放大的动画
     * @param view
     */
    private void startActivatingAnim(View view, float from, float to) {
        Object tag = view.getTag();
        if (tag instanceof ObjectAnimator) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, scaleProperty, from, to);
        animator.start();
        view.setTag(animator);
    }

    private boolean isActivatingAniming(View view) {
        Object tag = view.getTag();
        if (tag instanceof ObjectAnimator) {
            ObjectAnimator animator = (ObjectAnimator) tag;
            if (animator.isRunning()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除 itemView 上的动画
     * @param view
     */
    private void clearActivatingAnim(View view) {
        Object tag = view.getTag();
        if (tag instanceof ObjectAnimator) {
            ObjectAnimator animator = (ObjectAnimator) tag;
            animator.cancel();
            view.setTag(null);
        }
    }

    private ScaleProperty scaleProperty = new ScaleProperty("scale");

    public static class ScaleProperty extends Property<View, Float> {
        public ScaleProperty(String name) {
            super(Float.class, name);
        }

        @Override
        public Float get(View object) {
            return object.getScaleX();
        }

        @Override
        public void set(View object, Float value) {
            object.setScaleX(value);
            object.setScaleY(value);
        }
    }

    /**
     * Item被选中时候回调
     *
     * @param viewHolder
     * @param actionState
     *          当前Item的状态
     *          ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
     *          ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
     *          ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) { // 非闲置状态
            clearActivatingAnim(viewHolder.itemView);

            // 给 itemView 添加放大动画
            startActivatingAnim(viewHolder.itemView, 1.0f, mScale);

            // 给 itemView 添加透明度动画
            viewHolder.itemView.setAlpha(0.8f);

            if (mDragListener != null) {
                mDragListener.onDragStart();
            }
            delPos = viewHolder.getAdapterPosition();
            tempHolder = viewHolder;
            Log.e("jiabin", "onSelectedChanged delPos:" + delPos);
        } else { // 闲置状态
            if (mDragListener != null) {
                mDragListener.onDragFinish(mIsInside);
            }
            Log.e("PicDragHelperCallback", "闲置状态 onSelectedChanged mIsInside:" + mIsInside);
            if (mIsInside && delPos >= 0 && tempHolder != null) {
                Log.e("PicDragHelperCallback", "删除 onSelectedChanged delPos:" + delPos);
                // 先设置不可见，如果不设置的话，会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                tempHolder.itemView.setVisibility(View.INVISIBLE);
                mAdapter.removeItemFromDrag(delPos);
                mIsInside = false;
            }
            delPos = -1;
            tempHolder = null;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 移动过程中绘制Item
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     *          X轴移动的距离
     * @param dY
     *          Y轴移动的距离
     * @param actionState
     *          当前Item的状态
     * @param isCurrentlyActive
     *          如果当前被用户操作为true，反之为false
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (delArea == null || isActivatingAniming(viewHolder.itemView)) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }
        // 删除区域的宽度
        int delAreaWidth = delArea.getWidth();
        // 删除区域的高度
        int delAreaHeight = delArea.getHeight();

        // 获取相对在它父窗口里的坐标 View.getLeft() , View.getTop(), View.getBottom(), View.getRight()
        // View.getLocationInWindow()和 View.getLocationOnScreen()在window占据全部screen时，返回值相同，不同的典型情况是在Dialog中时。
        // 当Dialog出现在屏幕中间时，View.getLocationOnScreen()取得的值要比View.getLocationInWindow()取得的值要大
        int[] delLocation = new int[2];
        // 获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起，也就是包括了通知栏的高度。
//        delArea.getLocationOnScreen(delLocation);
        // 获取在当前窗口内的绝对坐标
        delArea.getLocationInWindow(delLocation);
        int delAreaX = delLocation[0];
        int delAreaY = delLocation[1];

        int itemWidth = viewHolder.itemView.getWidth();
        int itemHeight = viewHolder.itemView.getHeight();
        int[] itemLocation = new int[2];
        viewHolder.itemView.getLocationInWindow(itemLocation);
        int itemX = itemLocation[0];
        int itemY = itemLocation[1];

        int scaleItemWidth = (int) (itemWidth * mScale);
        int scaleItemHeight = (int) (itemHeight * mScale);

        int centerX = itemX + scaleItemWidth / 2;
        int centerY = itemY + scaleItemHeight / 2;

        boolean isInside;
        if (centerY > delAreaY && centerY < delAreaY + delAreaHeight && centerX > delAreaX && centerX < delAreaX + delAreaWidth) {
            isInside = true;
            Log.e("ItemTouchHelp", "进入删除区域");
        } else {
            Log.e("ItemTouchHelp", "非删除区域");
            isInside = false;
        }
        if (isInside != mIsInside) {
            if (mDragListener != null) {
                mDragListener.onDragAreaChange(isInside, tempHolder == null);
            }
        }
        mIsInside = isInside;

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * 用户操作完毕或者动画完毕后会被调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        clearActivatingAnim(viewHolder.itemView);
        startActivatingAnim(viewHolder.itemView, mScale, 1.0f);
        viewHolder.itemView.setAlpha(1.0f);

        // 不这么写数据更新后有的 item 会出现空白，这个 bug 改了好久，做个记录
        viewHolder.itemView.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewHolder.itemView.setVisibility(View.VISIBLE);
            }
        },300);
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * Item是否支持长按拖动
     *
     * @return
     *          true  支持长按操作
     *          false 不支持长按操作
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * Item是否支持滑动
     *
     * @return
     *          true  支持滑动操作
     *          false 不支持滑动操作
     */
    @Override
    public boolean isItemViewSwipeEnabled() { return false; }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }

    /**
     * 拖拽监听回调
     */
    public interface DragListener {
        /**
         * 开始拖拽
         */
        void onDragStart();
        /**
         * 结束拖拽
         */
        void onDragFinish(boolean isInside);
        /**
         * 拖拽区域的改变
         */
        void onDragAreaChange(boolean isInside, boolean isIdle);
    }

    public void setDragListener(DragListener listener) {
        mDragListener = listener;
    }

}
