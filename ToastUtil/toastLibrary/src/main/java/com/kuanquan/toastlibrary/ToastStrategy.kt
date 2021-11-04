package com.kuanquan.toastlibrary

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.kuanquan.toastlibrary.config.IToast
import com.kuanquan.toastlibrary.config.IToastStrategy
import com.kuanquan.toastlibrary.config.IToastStyle
import java.lang.ref.SoftReference

/**
 * Toast 默认处理器
 */
class ToastStrategy : Handler(Looper.getMainLooper()), IToastStrategy {
    /** 应用上下文  */
    private var mApplication: Application? = null

    /** Activity 栈管理  */
    private var mActivityStack: ActivityStack? = null

    /** Toast 对象  */
    private var mToastReference: SoftReference<IToast?>? = null

    /** Toast 样式  */
    private var mToastStyle: IToastStyle<*>? = null
   override fun registerStrategy(application: Application?) {
        mApplication = application
        mActivityStack = ActivityStack.register(application!!)
    }

    override fun bindStyle(style: IToastStyle<*>?) {
        mToastStyle = style
    }

    override fun createToast(application: Application?): IToast {
        val resumedActivity: Activity? = mActivityStack?.foregroundActivity
        val toast: IToast
        toast = resumedActivity?.let { ActivityToast(it) }
            ?: if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                // 处理 Android 7.1 上 Toast 在主线程被阻塞后会导致报错的问题
                SafeToast(application)
            } else {
                SystemToast(application)
            }

        // targetSdkVersion >= 30 的情况下在后台显示自定义样式的 Toast 会被系统屏蔽，并且日志会输出以下警告：
        // Blocking custom toast from package com.xxx.xxx due to package not in the foreground
        // targetSdkVersion < 30 的情况下 new Toast，并且不设置视图显示，系统会抛出以下异常：
        // java.lang.RuntimeException: This Toast was not created with Toast.makeText()
        if (toast is ActivityToast || Build.VERSION.SDK_INT < Build.VERSION_CODES.R || application!!.applicationInfo.targetSdkVersion < Build.VERSION_CODES.R) {
            toast.setView(mToastStyle?.createView(application))
            toast.setGravity(
                mToastStyle?.getGravity() ?: 0,
                mToastStyle?.getXOffset() ?: 0,
                mToastStyle?.getYOffset() ?: 0
            )
            toast.setMargin(mToastStyle?.getHorizontalMargin() ?: 0F, mToastStyle?.getVerticalMargin() ?: 0F)
        }
        return toast
    }

    override fun showToast(text: CharSequence?) {
        removeMessages(TYPE_SHOW)
        // 延迟一段时间之后再执行，因为在没有通知栏权限的情况下，Toast 只能显示当前 Activity
        // 如果当前 Activity 在 ToastUtils.show 之后进行 finish 了，那么这个时候 Toast 可能会显示不出来
        // 因为 Toast 会显示在销毁 Activity 界面上，而不会显示在新跳转的 Activity 上面
        val msg = Message.obtain()
        msg.what = TYPE_SHOW
        msg.obj = text
        sendMessageDelayed(msg, DELAY_TIMEOUT.toLong())
    }

   override fun cancelToast() {
        removeMessages(TYPE_CANCEL)
        sendEmptyMessage(TYPE_CANCEL)
    }

    override fun handleMessage(msg: Message) {
        var toast: IToast? = null
        if (mToastReference != null) {
            toast = mToastReference!!.get()
        }
        when (msg.what) {
            TYPE_SHOW -> {
                // 返回队列头部的元素，如果队列为空，则返回 null
                if (msg.obj is CharSequence) {
                    val text = msg.obj as CharSequence
                    toast?.cancel()
                    toast = createToast(mApplication)
                    mToastReference = SoftReference<IToast?>(toast)
                    toast.setDuration(getToastDuration(text))
                    toast.setText(text)
                    toast.show()
                }
            }
            TYPE_CANCEL -> {
                toast?.cancel()
            }
            else -> {
            }
        }
    }

    /**
     * 获取 Toast 显示时长
     */
    protected fun getToastDuration(text: CharSequence): Int {
        return if (text.length > 20) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    }

    companion object {
        /** 延迟时间  */
        private const val DELAY_TIMEOUT = 200

        /** 显示吐司  */
        private const val TYPE_SHOW = 1

        /** 取消显示  */
        private const val TYPE_CANCEL = 2
    }
}