package com.mouble.baselibrary.base

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mouble.baselibrary.http.*

abstract class BaseViewModelActivity<VM : BaseViewModel>: BaseActivity() {
    lateinit var viewModel: VM

    override fun initView(savedInstanceState: Bundle?) {
        providerVMClass()?.let { viewModelClass ->
            viewModel = ViewModelProvider(this).get(viewModelClass)
            viewModel.loadState.observe(this, Observer { state ->
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
            viewModel.requestLiveData.observe(this, Observer {
                showProgressDialog()
            })
            dataObserver()
            lifecycle.addObserver(viewModel)
        }
        initView()
    }

    abstract fun providerVMClass(): Class<VM>

    abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

//    val observer = Observer<String> {
//        it?.let {
//            showToast(it)
//        }
//    }
}