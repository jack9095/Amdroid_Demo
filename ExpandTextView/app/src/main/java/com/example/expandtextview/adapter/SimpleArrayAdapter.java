package com.example.expandtextview.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * @作者: njb
 * @时间: 2019/9/16 15:20
 * @描述:
 */
public class SimpleArrayAdapter<T> extends ArrayAdapter {
    private List<String> data_list;
    public SimpleArrayAdapter(Context context, int resource, List<String> data_list) {
        super(context, resource);
        this.data_list = data_list;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
