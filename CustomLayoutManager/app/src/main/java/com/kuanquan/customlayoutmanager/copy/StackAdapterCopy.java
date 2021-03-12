package com.kuanquan.customlayoutmanager.copy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.customlayoutmanager.R;
import com.kuanquan.customlayoutmanager.adapter.MyAdapter;
import com.kuanquan.customlayoutmanager.stack.CardItemTouchHelperCallback;
import com.kuanquan.customlayoutmanager.stack.CardLayoutManager;
import com.kuanquan.customlayoutmanager.stack.OnSwipeListener;
import com.kuanquan.customlayoutmanager.stack.TouchRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StackAdapterCopy extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> list = new ArrayList<>();
    private List<String> stackLists;

    public StackAdapterCopy() {
//        this.stackLists = stackLists;
        list.add(R.drawable.shape_constellation);
        list.add(R.drawable.shape_age);
        list.add(R.drawable.shape_constellation);
        list.add(R.drawable.shape_age);
        list.add(R.drawable.shape_constellation);
        list.add(R.drawable.shape_age);
        list.add(R.drawable.shape_constellation);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stack, parent, false);
            return new MyViewHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new TwoViewHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {

        if (mHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) mHolder;
            holder.recyclerView.setAdapter(new MyAdapter(list));
            CardItemTouchHelperCallback cardCallback = new CardItemTouchHelperCallback(holder.recyclerView.getAdapter(), list);
            cardCallback.setOnSwipedListener(new OnSwipeListener<Integer>() {

                @Override
                public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                    MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                    viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
//                    if (direction == CardConfig.SWIPING_LEFT) {
//                        myHolder.dislikeImageView.setAlpha(Math.abs(ratio));
//                    } else if (direction == CardConfig.SWIPING_RIGHT) {
//                        myHolder.likeImageView.setAlpha(Math.abs(ratio));
//                    } else {
//                        myHolder.dislikeImageView.setAlpha(0f);
//                        myHolder.likeImageView.setAlpha(0f);
//                    }
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, Integer o, int direction) {
                    MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                    viewHolder.itemView.setAlpha(1f);
//                    myHolder.dislikeImageView.setAlpha(0f);
//                    myHolder.likeImageView.setAlpha(0f);
//                    Toast.makeText(viewHolder.itemView.getContext(), direction == CardConfig.SWIPED_LEFT ? "swiped left" : "swiped right", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSwipedClear() { // 数据全部清空回调
//                Toast.makeText(this, "data clear", Toast.LENGTH_SHORT).show();
//                recyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData();
//                        recyclerView.getAdapter().notifyDataSetChanged();
//                    }
//                }, 3000L);
                }
            });

            final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
            final CardLayoutManager cardLayoutManager = new CardLayoutManager();
            cardLayoutManager.setParam(holder.recyclerView, touchHelper);
            holder.recyclerView.setLayoutManager(cardLayoutManager);
            touchHelper.attachToRecyclerView(holder.recyclerView);

//            holder.recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//                public float y1,y2,x2,x1;
//
//                @Override
//                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//                }
//
//                @Override
//                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
//                        x1 = e.getX();
//                        y1 = e.getY();
//                    }
//                    if (e.getAction() == MotionEvent.ACTION_UP) {
//                        x2 = e.getX();
//                        y2 = e.getY();
//                        if (Math.abs(x1 - x2) < 6) {
//                            Toast.makeText(rv.getContext(),"点击事件 adapter",Toast.LENGTH_SHORT).show();
//                            return false;// 距离较小，当作click事件来处理
//                        }
//                        if(Math.abs(x1 - x2) >60){  // 真正的onTouch事件
//                            return true;
//                        }
//                    }
//                    return false;
//                }
//
//                @Override
//                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                }
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return 0;
        } else {
            return 1;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TouchRecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    class TwoViewHolder extends RecyclerView.ViewHolder {

        public TwoViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
