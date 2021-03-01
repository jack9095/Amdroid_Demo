package com.kuanquan.panelemojikeyboard.scene.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.panelemojikeyboard.R;
import com.kuanquan.panelemojikeyboard.databinding.VhChatLeftLayoutBinding;
import com.kuanquan.panelemojikeyboard.databinding.VhChatRightLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * chatting pager adapter
 * Created by yummyLau on 18-7-11
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatBaseVH> {

    private List<ChatInfo> mData;
    private Context mContext;

    public ChatAdapter(Context context) {
        mData = new ArrayList<>();
        mContext = context;
    }

    public ChatAdapter(Context context, List<ChatInfo> data) {
        if (data != null) {
            mData = data;
        } else {
            mData = new ArrayList<>();
        }
        mContext = context;
    }

    public ChatAdapter(Context context,int count) {
        mData = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mData.add(ChatInfo.CREATE("模拟数据第" + (i + 1) + "条"));
        }
        mContext = context;
    }

    public void insertInfo(ChatInfo chatInfo) {
        if (chatInfo != null) {
            mData.add(chatInfo);
            notifyItemInserted(mData.size() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).owner) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public ChatBaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            VhChatRightLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.vh_chat_right_layout, parent, false);
            return new ChatRightVH(binding);
        } else {
            VhChatLeftLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.vh_chat_left_layout, parent, false);
            return new ChatLeftVH(binding);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ChatBaseVH holder, int position) {
        holder.bindData((mData.get(position)), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
