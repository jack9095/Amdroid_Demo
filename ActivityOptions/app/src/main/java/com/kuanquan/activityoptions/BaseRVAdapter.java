package com.kuanquan.activityoptions;

import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseRVAdapter<DATA, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ArrayList<DATA> mDatas = new ArrayList<>();
    protected AdapterView.OnItemClickListener mItemClickListener;

    public BaseRVAdapter() {
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        DATA item = getItem(position);
        bindItemData(holder, item, position);
        setupOnItemClick(holder, position);
    }

    protected abstract void bindItemData(VH viewHolder, DATA data, int position);

    protected void setupOnItemClick(final VH viewHolder, final int position) {
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(viewHolder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public DATA getItem(int position) {
        return mDatas.get(position);
    }

    public List<DATA> getData() {
        return new ArrayList<>(mDatas);
    }

    public void setData(Collection<DATA> list) {
        setDataSilently(list);
        notifyDataSetChanged();
    }

    public void setDataSilently(Collection<DATA> list) {
        mDatas.clear();
        addAll(list);
    }

    public void addAll(Collection<DATA> collection) {
        if (collection != null) {
            mDatas.addAll(collection);
        }
    }

    public void addAll(int index, Collection<DATA> collection) {
        if (collection != null) {
            mDatas.addAll(index, collection);
        }
    }

    public void add(DATA item) {
        if (item != null) {
            mDatas.add(item);
        }
    }

    public void add(int index, DATA item) {
        if (item != null) {
            mDatas.add(index, item);
        }
    }

    public DATA remove(int position) {
        return mDatas.remove(position);
    }

    public void clear() {
        mDatas.clear();
    }

    public boolean remove(DATA item) {
        return mDatas.remove(item);
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    protected void onItemClick(VH viewHolder, int position) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(null, viewHolder.itemView, position, position);
        }
    }

}
