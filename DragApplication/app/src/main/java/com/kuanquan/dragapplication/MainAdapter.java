package com.kuanquan.dragapplication;

import android.view.*;
import android.widget.*;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.kuanquan.dragapplication.test.Pic;
import java.util.*;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pic> mList;
    private static final int MAX_COUNT = 9;

    private int imageWidth;

    public MainAdapter(int width) {
        this.imageWidth = width;
    }

    private int maxCount = MAX_COUNT;

    private PicClickListener mPicClickListener;

    public static final int TYPE_PIC = 101;
    public static final int TYPE_PIC_ADD = 102;

    public void setList(ArrayList<Pic> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public ArrayList<Pic> getList() {
        return mList;
    }

    public void removeItem(int pos) {
        if (mList == null) {
            return;
        }
        if (pos < 0 || pos > mList.size()) {
            return;
        }
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeItemFromDrag(int pos) {
        if (mList == null) {
            return;
        }
        if (pos < 0 || pos > mList.size()) {
            return;
        }
        mList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount() - pos, "payload");
    }

    public void addItem(Pic pic) {
        if (mList == null) {
            return;
        }
        mList.add(pic);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null) {
            return TYPE_PIC_ADD;
        }
        int count = mList.size();
        if (count >= maxCount) {
            return TYPE_PIC;
        } else {
            if (position == count) { //-1+1
                return TYPE_PIC_ADD;
            } else {
                return TYPE_PIC;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 1;
        }
        int count = mList.size();
        if (count >= maxCount) {
            count = maxCount;
        } else {
            count = count + 1;
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PIC) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);
            return new PicViewHolder(view);
        } else if (viewType == TYPE_PIC_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic, parent, false);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_add, parent, false);
            return new PicAddViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (viewType == TYPE_PIC) {
            Pic pic = mList.get(position);
            PicViewHolder picHolder = (PicViewHolder) holder;
            picHolder.itemView.setVisibility(View.VISIBLE);
            picHolder.pic.setImageResource(R.mipmap.ic_launcher);
            picHolder.txt.setText("" + pic.id);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) picHolder.pic.getLayoutParams();
            layoutParams.height = imageWidth;
            layoutParams.width = imageWidth;
            picHolder.pic.setLayoutParams(layoutParams);
        } else if (viewType == TYPE_PIC_ADD) {
            PicAddViewHolder picAddHolder = (PicAddViewHolder) holder;
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) picAddHolder.addView.getLayoutParams();
            layoutParams.height = imageWidth;
            layoutParams.width = imageWidth;
            picAddHolder.addView.setImageResource(R.mipmap.mine_btn_plus);
            picAddHolder.addView.setLayoutParams(layoutParams);
        }
    }

    class PicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView edit;
        private ImageView pic;
        private TextView txt;

        public PicViewHolder(View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.img_edit);
            pic = itemView.findViewById(R.id.img_pic);
            txt = itemView.findViewById(R.id.txt);
            itemView.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (v == itemView) {
                if (mPicClickListener != null) {
                    mPicClickListener.onPicClick(v, pos);
                }
            } else if (v == edit) {
                removeItem(pos);
            }
        }
    }

    class PicAddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView addView;
        public PicAddViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            addView = itemView.findViewById(R.id.img_pic);
        }

        @Override
        public void onClick(View v) {
            if (mPicClickListener != null) {
                mPicClickListener.onAddClick(v);
            }
        }
    }

    public interface PicClickListener {
        void onPicClick(View view, int pos);
        void onAddClick(View view);
    }

    public void setPicClickListener(PicClickListener listener) {
        mPicClickListener = listener;
    }

}
