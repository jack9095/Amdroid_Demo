package com.kuanquan.demo;

import android.annotation.SuppressLint;
import android.util.Log;

import com.kuanquan.demo.enum_p.State;
import com.plutinosoft.platinum.*;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO 3
 * the server instance
 * 枚举单例
 */
public enum ServerInstance {

    // 枚举单例
    INSTANCE;
    ServerInstance() {
        Log.e(TAG, "Init! ->>>>> ");
    }

    private static final String TAG = "ServerInstance";

    public volatile State mState = State.IDLE;
    private DLNABridge mDLNABridge;

    @SuppressLint("CheckResult")
    public void start(ServerParams params) {
        Observable.create(emitter -> {
            startAsync(params); // 异步开启
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {});
    }

    private void startAsync(Object param) {
        if (mState == State.IDLE) {
            Log.e(TAG, "开启 native 与 本地数据交互的桥");
            if (param instanceof ServerParams) {
                ServerParams serverParam = (ServerParams) param;
                mState = State.STARTING;
                mDLNABridge = new DLNABridge();
                mDLNABridge.setCallback(CallbackInstance.INSTANCE.getCallback());
                mDLNABridge.start(serverParam);
                mState = State.RUNNING;
            }
        }
    }


    @SuppressLint("CheckResult")
    public void stop() {
        Observable.create(emitter -> {
            // 子线程
            stopAsync();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> { });
    }

    private void stopAsync() {
        if (mState == State.RUNNING) {
            mState = State.STOPPING;
            mDLNABridge.stop();
            mDLNABridge.destroy();
            mState = State.IDLE;
        }
    }
}
