package com.kuanquan.demo.copy;

import android.annotation.SuppressLint;
import android.util.Log;

import com.kuanquan.demo.CallbackInstance;
import com.kuanquan.demo.enum_p.State;
import com.plutinosoft.platinum.DLNABridge;
import com.plutinosoft.platinum.ServerParams;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO 3
 * the server instance
 * 枚举单例
 */
public enum ServerInstanceCopy {

    INSTANCE;
    ServerInstanceCopy() {
        Log.e(TAG, "Init! ->>>>> ");
    }

    private static final String TAG = "ServerInstance";

    public volatile State mState = State.IDLE;
    private DLNABridge mDLNAServer;

    @SuppressLint("CheckResult")
    public void start(ServerParams params) {
        Log.e(TAG, "开启 ServerAsyncEvent");
        Observable.create(emitter -> {
            // 子线程
//                emitter.onNext("哈哈"); // 把数据发射出去
//                emitter.onComplete();
            startAsync(params); // 异步开启
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //                LogUtil.e(TAG,"subscribe   " + Thread.currentThread().getName());
                    }
                });
    }

    private void startAsync(Object param) {
        if (mState == State.IDLE) {
            Log.e(TAG, "开启 startAsync");
            if (param instanceof ServerParams) {
                ServerParams serverParam = (ServerParams) param;
                mState = State.STARTING;
                mDLNAServer = new DLNABridge();
                mDLNAServer.setCallback(CallbackInstance.INSTANCE.getCallback());
                mDLNAServer.start(serverParam);
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
            mDLNAServer.stop();
            mDLNAServer.destroy();
            mState = State.IDLE;
        }
    }
}
