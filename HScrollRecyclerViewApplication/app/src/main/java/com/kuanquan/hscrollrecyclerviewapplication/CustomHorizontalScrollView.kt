package com.kuanquan.hscrollrecyclerviewapplication

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView

class CustomHorizontalScrollView(context: Context, attrs: AttributeSet? = null) : HorizontalScrollView(context, attrs) {

//    @SuppressLint("ClickableViewAccessibility", "LongLogTag")
//    override fun setOnTouchListener(l: OnTouchListener?) {
//        super.setOnTouchListener { v, event ->
//            when(event?.action){
//                MotionEvent.ACTION_DOWN -> {
//
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    val firstView = (v as HorizontalScrollView).getChildAt(0)
//                    if (firstView.measuredWidth <= v.getScrollX() + v.getWidth()) {
//                        // 滑动到最右端
//                        Log.e("CustomHorizontalScrollView", "滑动到最右端")
//                        getParent().requestDisallowInterceptTouchEvent(false)
//                    } else if (v.getScrollX() == 0) {
//                        // 滑动到最左端
//                        Log.e("CustomHorizontalScrollView", "滑动到最左端")
//                        getParent().requestDisallowInterceptTouchEvent(false)
//                    } else {
//                        getParent().requestDisallowInterceptTouchEvent(true)
//                    }
//                }
//            }
//            false
//        }
//
//    }

    var isScroll = true

    // getParent().requestDisallowInterceptTouchEvent(true)剥夺父view 对touch 事件的处理权
    @SuppressLint("LongLogTag")
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        val view: View = getChildAt(childCount - 1) as View

        val firstView: View = getChildAt(0) as View

        if (firstView.measuredWidth <= scrollX + width) {
//            isScroll = true
//            requestDisallowInterceptTouchEvent(true)
//            getParent().requestDisallowInterceptTouchEvent(false)
            // 滑动到最右端
//            Log.e("onScrollChanged ->", "滑动到最右端")
        } else if (scrollX == 0) {
//            isScroll = true
//            getParent().requestDisallowInterceptTouchEvent(false)
//            requestDisallowInterceptTouchEvent(true)
            // 滑动到最左端
//            Log.e("onScrollChanged ->", "滑动到最左端")
        } else {
//            isScroll = false
//            getParent().requestDisallowInterceptTouchEvent(true)
//            requestDisallowInterceptTouchEvent(false)
//            Log.e("onScrollChanged scrollX ->", "$scrollX")
        }

        var d: Int = view.right
        d -= width + scrollX
        if (d == 0) {
//            Log.e("onScrollChanged", "滑动到最右端")
        } else {

        }
    }

    //    var downX = 0
//    var downY = 0
    @SuppressLint("LongLogTag")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
//                downX = getX().toInt()
//                downY = getY().toInt()
            }
            MotionEvent.ACTION_MOVE -> {
//                if (getX().toInt() - downX > getY().toInt() - downY && isScroll) {
//                    getParent().requestDisallowInterceptTouchEvent(true)
//                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false)
            }
            MotionEvent.ACTION_UP -> {
//                isScroll = false
//                parent.requestDisallowInterceptTouchEvent(false)
//                Log.e("onTouchEvent ACTION_UP isScroll ->", "$isScroll")
            }
            MotionEvent.ACTION_CANCEL -> {
//                isScroll = false
//                parent.requestDisallowInterceptTouchEvent(false)
//                Log.e("onTouchEvent ACTION_CANCEL isScroll ->", "$isScroll")
            }
        }
        return super.onTouchEvent(event)
    }

    private var startX = 0f
    private var startY = 0f

    @SuppressLint("LongLogTag")
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
                parent.requestDisallowInterceptTouchEvent(true)
                Log.e("dispatchTouchEvent ACTION_DOWN isScroll ->", "$isScroll")
            }
            MotionEvent.ACTION_MOVE -> {
//                if ((Math.abs(ev.x - startX) > Math.abs(ev.y - startY) && isScroll) || Math.abs(ev.x - startX) < Math.abs(ev.y - startY)) {
                if (Math.abs(ev.x - startX) < Math.abs(ev.y - startY)) {
                    Log.e("dispatchTouchEvent ACTION_MOVE isScroll ->", "$isScroll")
                    parent.requestDisallowInterceptTouchEvent(false)
                }

                val firstView = getChildAt(0)
                if (firstView.measuredWidth <= scrollX + width && ev.x - startX < 0) {
                    // 滑动到最右端
                    Log.e("CustomHorizontalScrollView", "滑动到最右端")
                    parent.requestDisallowInterceptTouchEvent(false)
                } else if (scrollX == 0 && ev.x - startX > 0) {
                    // 滑动到最左端
                    Log.e("CustomHorizontalScrollView", "滑动到最左端")
                    parent.requestDisallowInterceptTouchEvent(false)
//                } else {
//                    parent.requestDisallowInterceptTouchEvent(true)
                }

                startX = ev.x
                startY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                isScroll = false
                parent.requestDisallowInterceptTouchEvent(false)
                Log.e("dispatchTouchEvent ACTION_UP isScroll ->", "$isScroll")
            }
            MotionEvent.ACTION_CANCEL -> {
                isScroll = false
                parent.requestDisallowInterceptTouchEvent(false)
                Log.e("dispatchTouchEvent ACTION_CANCEL isScroll ->", "$isScroll")
            }
            else -> {
                parent.requestDisallowInterceptTouchEvent(false)
                isScroll = false
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}