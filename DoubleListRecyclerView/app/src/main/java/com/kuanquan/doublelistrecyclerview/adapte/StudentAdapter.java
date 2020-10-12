package com.kuanquan.doublelistrecyclerview.adapte;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kuanquan.doublelistrecyclerview.R;
import com.kuanquan.doublelistrecyclerview.bean.StudentModel;

import java.util.List;

public class StudentAdapter extends BaseQuickAdapter<StudentModel, BaseViewHolder> {
    public StudentAdapter(@Nullable List data) {
        super(R.layout.student_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentModel item) {
        helper.setText(R.id.tv_name, item.getName()+"").setText(R.id.tv_age, item.getAge()+"");
    }
}
