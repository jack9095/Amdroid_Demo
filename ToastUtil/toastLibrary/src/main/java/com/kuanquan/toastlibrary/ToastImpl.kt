package com.kuanquan.toastlibrary

import android.app.Activity
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import com.kuanquan.toastlibrary.config.IToast

/**
 * 自定义 Toast 实现类
 */
internal class ToastImpl(activity: Activity?, toast: IToast) {

    /** 当前的吐司对象  */
    private var mToast: IToast? = toast

    /** WindowManager 辅助类  */
    private var mWindowLifecycle: WindowLifecycle? = WindowLifecycle(activity)

    /** 当前应用的包名  */
    private var mPackageName: String? = activity?.packageName

    /** 当前是否已经显示  */
    var isShow = false

    /***
     * 显示吐司弹窗
     */
    fun show() {
        if (isShow) {
            return
        }
        HANDLER.removeCallbacks(mShowRunnable)
        HANDLER.post(mShowRunnable)
    }

    /**
     * 取消吐司弹窗
     */
    fun cancel() {
        if (!isShow) {
            return
        }
        // 移除之前移除吐司的任务
        HANDLER.removeCallbacks(mCancelRunnable)
        HANDLER.post(mCancelRunnable)
    }

    private val mShowRunnable: Runnable = object : Runnable {
        override fun run() {
            val activity: Activity? = mWindowLifecycle?.activity
            if (activity == null || activity.isFinishing) {
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
                return
            }
            val windowManager = activity.windowManager ?: return
            val params = WindowManager.LayoutParams()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.width = WindowManager.LayoutParams.WRAP_CONTENT
            params.format = PixelFormat.TRANSLUCENT
            params.windowAnimations = android.R.style.Animation_Toast
            params.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            params.packageName = mPackageName
            params.gravity = mToast?.getGravity() ?: 0
            params.x = mToast?.getXOffset() ?: 0
            params.y = mToast?.getYOffset() ?: 0
            params.verticalMargin = mToast?.getVerticalMargin() ?: 0F
            params.horizontalMargin = mToast?.getHorizontalMargin() ?: 0F
            try {
                windowManager.addView(mToast?.getView(), params)
                // 添加一个移除吐司的任务
                HANDLER.postDelayed(Runnable { cancel() },
                    if (mToast?.getDuration() == Toast.LENGTH_LONG) LONG_DURATION_TIMEOUT else SHORT_DURATION_TIMEOUT
                )
                // 注册生命周期管控
                mWindowLifecycle?.register(this@ToastImpl)
                // 当前已经显示
                isShow = true
            } catch (e: IllegalStateException) {
                // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
                // java.lang.IllegalStateException: View android.widget.TextView has already been added to the window manager.
                // 如果 WindowManager 绑定的 Activity 已经销毁，则会抛出异常
                // android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@ef1ccb6 is not valid; is your activity running?
                e.printStackTrace()
            } catch (e: BadTokenException) {
                e.printStackTrace()
            }
        }
    }
    private val mCancelRunnable: Runnable = object : Runnable {
        override fun run() {
            try {
                val act: Activity = mWindowLifecycle?.activity ?: return
                val windowManager = act.windowManager ?: return
                windowManager.removeViewImmediate(mToast?.getView())
            } catch (e: IllegalArgumentException) {
                // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
                // java.lang.IllegalArgumentException: View=android.widget.TextView not attached to window manager
                e.printStackTrace()
            } finally {
                // 反注册生命周期管控
                mWindowLifecycle?.unregister()
                // 当前没有显示
                isShow = false
            }
        }
    }

    companion object {
        private val HANDLER = Handler(Looper.getMainLooper())

        /** 短吐司显示的时长  */
        private const val SHORT_DURATION_TIMEOUT = 2000L

        /** 长吐司显示的时长  */
        private const val LONG_DURATION_TIMEOUT = 3500L
    }

}