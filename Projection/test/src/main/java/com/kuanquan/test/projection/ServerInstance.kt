package com.kuanquan.test.projection

import android.annotation.SuppressLint
import android.util.Log
import com.kuanquan.test.enum_p.State
import com.plutinosoft.platinum.DLNABridge
import com.plutinosoft.platinum.ServerParams
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * TODO 3
 * the server instance
 * 枚举单例
 */
object ServerInstance {
    private const val TAG = "ServerInstance"

    @Volatile
    var mState = State.IDLE
    private var mDLNABridge: DLNABridge? = null

    @SuppressLint("CheckResult")
    fun start(params: ServerParams?) {
        Observable.create { emitter: ObservableEmitter<Any?>? ->
            startAsync(params) // 异步开启
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { }
    }

    private fun startAsync(param: Any?) {
        if (mState === State.IDLE) {
            Log.e(TAG, "开启 native 与 本地数据交互的桥")
            if (param is ServerParams) {
                mState = State.STARTING
                mDLNABridge = DLNABridge()
                mDLNABridge?.setCallback(
                    CallbackInstance.getDLNACallback()
                )
                mDLNABridge?.start(param)
                mState = State.RUNNING
            }
        }
    }

    @SuppressLint("CheckResult")
    fun stop() {
        Observable.create { emitter: ObservableEmitter<Any?>? ->
            stopAsync()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { }
    }

    private fun stopAsync() {
        if (mState === State.RUNNING) {
            mState = State.STOPPING
            mDLNABridge?.stop()
            mDLNABridge?.destroy()
            mState = State.IDLE
        }
    }
}