package com.mouble.baselibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.mouble.baselibrary.util.EventCenter
import com.mouble.baselibrary.util.LogUtil
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment: Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this)
        }
    }

    abstract fun isBindEventBusHere(): Boolean
    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData(savedInstanceState: Bundle?)
    abstract fun dataObserver()

    protected fun showToast(msg: String) {
        Toast.makeText(activity?.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private var progressDialog: LoadDialog? = null
    fun showProgressDialog() {
        activity?.run {
            progressDialog = LoadDialog(this).apply {
                setCanceledOnTouchOutside(false)
                setCancelable(true)
            }
        }

        progressDialog?.run {
            try {
                if (isShowing)
                    dismiss()
                else
                    show()
            } catch (e: Exception) {
                LogUtil.e("异常 = ", e)
            }
        }
    }

    fun dismissProgressDialog() {
        try {
            progressDialog?.run {
                if (isShowing) dismiss()
            }
        } catch (e: Exception) {
            LogUtil.e("进度弹框取消异常 = ", e)
        }
    }

    fun postEventBus(type: String) {
        EventBus.getDefault().post(
            EventCenter<Any>(
                type
            )
        )
    }

    fun postEventBusSticky(type: String) {
        EventBus.getDefault().postSticky(
            EventCenter<Any>(
                type
            )
        )
    }

    fun postEventBusSticky(type: String, obj: Any) {
        EventBus.getDefault().postSticky(
            EventCenter(
                type,
                obj
            )
        )
    }

    fun postEventBus(type: String, obj: Any) {
        EventBus.getDefault().post(
            EventCenter(
                type,
                obj
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBindEventBusHere()) EventBus.getDefault().unregister(this)
        if (progressDialog != null) {
            dismissProgressDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData(savedInstanceState)
    }

    // TODO 型参数组 修饰符 vararg
    protected fun addOnClickListeners(listener: View.OnClickListener, @IdRes vararg ids: Int) {
        for (id in ids) {
            view?.findViewById<View>(id)?.setOnClickListener(listener)
        }
    }
}