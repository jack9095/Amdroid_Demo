package com.kuanquan.pickturelib.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.kuanquan.pickturelib.R;
import com.kuanquan.pickturelib.constant.Constant;


/**
 * Created by Stefan on 2016/10/17.
 * styleable参数样式管理类
 */
public class PickStyleableHelper {

    int column;

    public PickStyleableHelper(Context mContext) {
        TypedArray a = mContext.obtainStyledAttributes(R.styleable.PickRecyclerView);

        column = a.getInteger(R.styleable.PickRecyclerView___column, Constant.DEFAULT_DSP_COLUMN);

        a.recycle();
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
