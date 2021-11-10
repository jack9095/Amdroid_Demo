package com.kuanquan.custompopupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.custompopupwindow.utils.FitPopupUtil;

import java.util.List;

/**
 * Created by DongJr on 2017/2/21.
 */

public class ListAdapter extends RecyclerView.Adapter<ListHolder> {

    private List<String> mList;
    private Context mContext;

    public ListAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }


    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_list, parent, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitPopupUtil fitPopupUtil = new FitPopupUtil((Activity) mContext);
                fitPopupUtil.setOnClickListener(new FitPopupUtil.OnCommitClickListener() {
                    @Override
                    public void onClick(String reason) {
                        Toast.makeText(mContext,reason,Toast.LENGTH_SHORT).show();
                    }
                });
                fitPopupUtil.showPopup(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
