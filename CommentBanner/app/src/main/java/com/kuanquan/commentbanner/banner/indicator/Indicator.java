package com.kuanquan.commentbanner.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.kuanquan.commentbanner.banner.config.IndicatorConfig;
import com.kuanquan.commentbanner.banner.listener.OnPageChangeListener;


public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
