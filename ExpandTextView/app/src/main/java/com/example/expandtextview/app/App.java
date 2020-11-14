package com.example.expandtextview.app;

import android.app.Application;
import android.content.Context;

/**
 * @作者: njb
 * @时间: 2019/7/22 11:07
 * @描述:
 */
public class App extends Application {
    //全局Context
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }


    public static Context getContext() {
        return sContext;
    }
}
