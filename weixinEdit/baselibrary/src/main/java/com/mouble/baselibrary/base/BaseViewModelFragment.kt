package com.mouble.baselibrary.base

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mouble.baselibrary.http.*

abstract class BaseViewModelFragment<VM : BaseViewModel>: BaseFragment(), View.OnClickListener {
    protected lateinit var mViewModel: VM

    override fun initView() {
        providerVMClass()?.let { viewModelClass ->
            mViewModel = ViewModelProvider(this).get(viewModelClass)
            mViewModel.loadState.observe(this, Observer { state ->
                state?.let {
                    dismissProgressDialog()
                    if (!TextUtils.isEmpty(state)) {
                        when (state) {
                            ERROR_STATE -> showToast("加载错误")
                            NET_WORK_STATE -> showToast("网络不好，请稍后重试")
                            LOADING_STATE -> showToast("加载中")
                            SUCCESS_STATE -> {
                                   showToast("加载成功");
                            }
                            else -> {
//                                showToast(state)
                            }
                        }
                    }
                }
            })
            mViewModel.requestLiveData.observe(this, Observer {
                showProgressDialog()
            })
            dataObserver()
            lifecycle.addObserver(mViewModel)
        }
    }

    abstract fun providerVMClass(): Class<VM>?

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mViewModel)
    }
}