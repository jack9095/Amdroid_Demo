package com.kuanquan.dragapplication.test;

import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.util.Property;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 拖拽帮助类
 * https://blog.csdn.net/wuyuxing24/article/details/78985026
 */
public class PicDragHelperCallback extends ItemTouchHelper.Callback {

    private PicMgrAdapter mAdapter;
    private View delArea;  // 删除区域
    private DragListener mDragListener;
    private boolean mIsInside = false;
    private int delPos = -1; // 可能要被移除的的 itemView 的 position
    private RecyclerView.ViewHolder tempHolder;
    private float mScale = 1.1f;
    private float mAlpha = 1.0f;

    private float mInsideScale = 0.86f;
    private float mInsideAlpha = 0.3f;

    private float mMoveScale = mScale;

    public PicDragHelperCallback(@NonNull PicMgrAdapter adapter, View delArea) {
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
        if (viewHolder instanceof PicMgrAdapter.PicAddViewHolder) {
            return 0;
        }
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
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
    private void startActivatingAnim(View view, float from, float to, long duration) {
        Object tag = view.getTag();
        if (tag instanceof ObjectAnimator) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, scaleProperty, from, to);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, scaleProperty, from, 0.5f);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX",1.2f);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",1.2f);
//        animator.setDuration(duration);
        animator.start();
//        animatorY.start();
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

        // 用于防止交换位置时角标越界的问题
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (target instanceof PicMgrAdapter.PicAddViewHolder) {
            return false;
        }
        Log.e("ItemTouchHelp", "from = " + viewHolder.getAdapterPosition());
        Log.e("ItemTouchHelp", "target = " + target.getAdapterPosition());
        ArrayList<Pic> list = mAdapter.getList();
        if (list == null || list.size() < 2) {
            return false;
        }

        // 返回布局中最新的计算位置，和用户所见到的位置一致，当做用户输入（例如点击事件）的时候考虑使用
//        viewHolder.getLayoutPosition();

        // 返回数据在Adapter中的位置（也许位置的变化还未来得及刷新到布局中），
        // 当使用Adapter的时候（例如调用Adapter的notify相关方法时）考虑使用
        int from = viewHolder.getAdapterPosition();  // 这里指拖拽移动前的位置
        int endPosition = target.getAdapterPosition(); // 这里指拖拽移动后的位置
        Log.d("jiabin", "onMove from:" + from + " end:" + endPosition);
        delPos = endPosition;

        // 交换在指定列表中的指定位置的元素
        Collections.swap(list, from, endPosition);

        // 移动指定未知的元素并更新
        mAdapter.notifyItemMoved(from, endPosition);

        return true;
    }

    // 针对drag状态，滑动超过百分之多少的距离可以可以调用onMove()函数(注意哦，这里指的是onMove()函数的调用，并不是随手指移动的那个view哦)
    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//        return super.getMoveThreshold(viewHolder);
        return .5f;
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
        //Log.d("jiabin", "onSelectedChanged:" + actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) { // 非闲置状态
            clearActivatingAnim(viewHolder.itemView);

            // 给 itemView 添加放大动画
            startActivatingAnim(viewHolder.itemView, 1.0f, mScale, 20);

            // 给 itemView 添加透明度动画
            viewHolder.itemView.setAlpha(mAlpha);

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

        // Log.d("jiabin","itemWidth:" + itemWidth + " | itemHeight:" + itemHeight + " | itemX:" + itemX + " | itemY:" + itemY);

        int scaleItemWidth = (int) (itemWidth * mMoveScale);
        int scaleItemHeight = (int) (itemHeight * mMoveScale);

        int centerX = itemX + scaleItemWidth / 2;
        int centerY = itemY + scaleItemHeight / 2;

        boolean isInside = false;
        if (centerY > delAreaY && centerY < delAreaY + delAreaHeight && centerX > delAreaX && centerX < delAreaX + delAreaWidth) {
            isInside = true;
//            Log.e("ItemTouchHelp", "进入删除区域");
        } else {
//            Log.e("ItemTouchHelp", "非删除区域");
            isInside = false;
        }
        if (isInside != mIsInside) {
            if (tempHolder != null) {
                if (isInside) {
//                    Log.e("ItemTouchHelp", "mInsideScale" + mInsideScale);
                    mMoveScale = mInsideScale;

//                    clearActivatingAnim(viewHolder.itemView);
//                    startActivatingAnim(viewHolder.itemView, mScale, mInsideScale, 150);
//                    viewHolder.itemView.setAlpha(mInsideAlpha);
                } else {
//                    Log.e("ItemTouchHelp", "mScale" + mScale);
                    mMoveScale = mScale;
//                    clearActivatingAnim(viewHolder.itemView);
//                    startActivatingAnim(viewHolder.itemView, mInsideScale, mScale, 150);
//                    viewHolder.itemView.setAlpha(mAlpha);
                }
            }
            if (mDragListener != null) {
                mDragListener.onDragAreaChange(isInside, tempHolder == null);
            }
        }
        mIsInside = isInside;

        if (!isCurrentlyActive) {
//            Log.e("ItemTouchHelp", "不操作");
        } else {
//            Log.e("ItemTouchHelp", "用户操作");
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getBoundingBoxMargin() {
//        return super.getBoundingBoxMargin();
        return 300;
    }

    /**
     * 用户操作完毕或者动画完毕后会被调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        clearActivatingAnim(viewHolder.itemView);
        startActivatingAnim(viewHolder.itemView, mScale, 1.0f, 10);
        viewHolder.itemView.setAlpha(1.0f);
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
        // 支持长按拖拽功能
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
    public boolean isItemViewSwipeEnabled() {
        // 不支持滑动功能
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

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

    /**
     * 设置选中后的放大效果
     *
     * @param scale
     */
    public void setScale(float scale) {
//        mScale = scale;
        mMoveScale = mScale;
    }

    /**
     * 设置选中后的透明效果
     *
     * @param alpha
     */
    public void setAlpha(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        mAlpha = alpha;
    }

}
