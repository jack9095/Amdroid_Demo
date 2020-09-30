package com.kuanquan.weixinedit.model;

import android.view.View;

public class EventModel {
    public View view;
    public int position;

    public EventModel(View view, int position) {
        this.view = view;
        this.position = position;
    }
}
