package com.kuanquan.doyincover.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

class ViewOnSubscribe(val view: View) : ObservableOnSubscribe<View> {

    private lateinit var mObservableEmitter: ObservableEmitter<View>

    init {
        view.setOnClickListener {
            mObservableEmitter.onNext(it)
        }
    }

    override fun subscribe(emitter: ObservableEmitter<View>) {
        mObservableEmitter = emitter
    }

}

@SuppressLint("CheckResult")
fun View.setSafeClickListener(listener: (View) -> Unit) {
    Observable.create(ViewOnSubscribe(this))
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                listener(it)
            }

}

@SuppressLint("CheckResult")
fun View.setSafeClickListener(listener: View.OnClickListener?, unSafeListener: View.OnClickListener? = null) {
    Observable.create(ViewOnSubscribe(this))
            .doOnNext {
                unSafeListener?.onClick(it)
            }
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                listener?.onClick(it)
            }

}

class DoubleTouchEvent(var onDoubleClick: (View, MotionEvent) -> Unit) : View.OnTouchListener {
    private val DOUBLE_TAP_TIMEOUT = 200
    private var mCurrentDownEvent: MotionEvent? = null
    private var mPreviousUpEvent: MotionEvent? = null

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (mPreviousUpEvent != null
                    && mCurrentDownEvent != null
                    && isConsideredDoubleTap(mCurrentDownEvent!!,
                            mPreviousUpEvent!!, event)) {
                onDoubleClick(v!!, event)
                return true
            }
            mCurrentDownEvent = MotionEvent.obtain(event)
        } else if (event?.action == MotionEvent.ACTION_UP) {
            mPreviousUpEvent = MotionEvent.obtain(event)
        }
        return false

    }

    private fun isConsideredDoubleTap(firstDown: MotionEvent,
                                      firstUp: MotionEvent, secondDown: MotionEvent): Boolean {
        if (secondDown.eventTime - firstUp.eventTime > DOUBLE_TAP_TIMEOUT) {
            return false
        }
        val deltaX = firstUp.x.toInt() - secondDown.x.toInt()
        val deltaY = firstUp.y.toInt() - secondDown.y.toInt()
        return deltaX * deltaX + deltaY * deltaY < 10000
    }

}
