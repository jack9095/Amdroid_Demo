package com.kuanquan.projection;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.kuanquan.projection.media.MediaUtils;


/**
 * Created by huzongyao on 2018/6/6.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        MediaUtils.configureExoMedia(this);
    }
}
