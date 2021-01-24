package com.kuanquan.pagetransitionanimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kuanquan.pagetransitionanimation.R;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    private List<String> datas;
    private Context context;
    private OnClickListener onClickListener;

    public MyAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        Glide.with(context).load(datas.get(i)).into(myViewHolder.imageView);
        Glide.with(context).load(datas.get(i)).into(myViewHolder.imageViewcopy);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(v,myViewHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView imageViewcopy;

        myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            imageViewcopy = itemView.findViewById(R.id.imag_iv);
        }
    }

    public interface OnClickListener {
        void onClick(View v,int position);
    }
}
