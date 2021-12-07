package com.kuanquan.hook;

import android.app.Application;

import com.kuanquan.hook.instrumentation.ActivityThreadHook;
import com.kuanquan.hook.reportSizeConfigurations.ReportSizeConfigurationsHook;

public class WorkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityThreadHook.hook();
        ReportSizeConfigurationsHook.hook();
    }
}
