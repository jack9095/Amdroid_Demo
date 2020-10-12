package com.kuanquan.doublelistrecyclerview.adapte;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kuanquan.doublelistrecyclerview.R;
import com.kuanquan.doublelistrecyclerview.bean.ClassModel;

import java.util.List;

public class ClassAdapter extends BaseQuickAdapter<ClassModel, BaseViewHolder> {
    private int position;

    public ClassAdapter(@Nullable List data) {
        super(R.layout.class_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassModel item) {
        helper.setTextColor(R.id.tv_class_name, helper.getLayoutPosition() == position ? Color.parseColor("#8470FF") : Color.parseColor("#363636"));
        helper.setText(R.id.tv_class_name, item.getName());
    }

    public void setSelection(int pos) {
        this.position = pos;
        notifyDataSetChanged();
    }
}