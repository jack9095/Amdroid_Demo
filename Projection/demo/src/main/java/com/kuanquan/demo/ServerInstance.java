package com.kuanquan.demo;

import android.annotation.SuppressLint;
import android.util.Log;

import com.kuanquan.demo.event.ServerAsyncEvent;
import com.plutinosoft.platinum.DLNABridge;
import com.plutinosoft.platinum.ServerParams;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huzongyao on 2018/6/10.
 * the server instance
 * 枚举单例
 */
public enum ServerInstance {

    INSTANCE;

    private static final String TAG = "ServerInstance";

    private volatile State mState = State.IDLE;
    private DLNABridge mDLNAServer;

    ServerInstance() {
        Log.d(TAG, "Init!");
    }

    public State getState() {
        return mState;
    }

    @SuppressLint("CheckResult")
    public void start(ServerParams params) {
        Log.e("ServerInstance", "开启 ServerAsyncEvent");
        ServerAsyncEvent event = new ServerAsyncEvent(ServerAsyncEvent.EVENT_START);
        event.setParam(params);

        Observable.create(emitter -> {
            // 子线程
//                emitter.onNext("哈哈"); // 把数据发射出去
//                emitter.onComplete();
            startAsync(event);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //                LogUtil.e(TAG,"subscribe   " + Thread.currentThread().getName());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void stop() {
        Observable.create(emitter -> {
            // 子线程
            stopAsync();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> { });
    }

    public enum State {
        IDLE,
        STARTING,
        RUNNING,
        STOPPING
    }

    private void startAsync(ServerAsyncEvent event) {
        if (mState == State.IDLE) {
            Log.e("ServerInstance", "开启 startAsync");
            Object param = event.getParam();
            if (param != null && param instanceof ServerParams) {
                ServerParams serverParam = (ServerParams) param;
                mState = State.STARTING;
                mDLNAServer = new DLNABridge();
                mDLNAServer.setCallback(CallbackInstance.INSTANCE.getCallback());
                mDLNAServer.start(serverParam);
                mState = State.RUNNING;
            }
        }
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
