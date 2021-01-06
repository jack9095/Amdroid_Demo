package com.kuanquan.viewpager2

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.home_layout.*
import java.io.Serializable

class HomeFragment: BaseFragment() {

    companion object {

        const val PARAMS_TEXT = "params_text"

        @JvmStatic
        fun newInstance(str: String?) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(PARAMS_TEXT, str)
            }
        }

        private val ACTION_SHEET_RATIO = 0.75f
        private val ACTION_DIMAMOUNT = 0.5f
    }

    override fun onClick(v: View?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int = R.layout.home_layout

    override fun initView() {
        arguments?.run {
            getString(PARAMS_TEXT)?.let {
                tv.text = it
            }
        }
    }
}