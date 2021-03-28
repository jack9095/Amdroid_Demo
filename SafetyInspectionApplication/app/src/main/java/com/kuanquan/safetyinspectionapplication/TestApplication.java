package com.kuanquan.safetyinspectionapplication;

import android.app.Application;

import com.lahm.library.EasyProtectorLib;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyProtectorLib.checkXposedExistAndDisableIt();
//        可以在启动时创建localServerSocket
//        EasyProtectorLib.checkIsRunningInVirtualApk(getPackageName(), new VirtualCheckCallback() {
//            @Override
//            public void findSuspect() {
//                System.exit(0);
//            }
//        });
    }
}