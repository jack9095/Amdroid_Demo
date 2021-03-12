package com.kuanquan.customlayoutmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.customlayoutmanager.R;
import com.kuanquan.customlayoutmanager.stack.CardItemTouchHelperCallback;
import com.kuanquan.customlayoutmanager.stack.CardLayoutManager;
import com.kuanquan.customlayoutmanager.stack.TouchRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> list = new ArrayList<>();

    public StackAdapter() {
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

            final ItemTouchHelper touchHelper = new ItemTouchHelper(cardCallback);
            final CardLayoutManager cardLayoutManager = new CardLayoutManager();
            cardLayoutManager.setParam(holder.recyclerView, touchHelper);
            holder.recyclerView.setLayoutManager(cardLayoutManager);
            touchHelper.attachToRecyclerView(holder.recyclerView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return 0;
        } else {
            return 1;
        }
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
