package com.kuanquan.hscrollrecyclerviewapplication.frgment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuanquan.hscrollrecyclerviewapplication.R
import java.io.Serializable

class HomeFragment: BaseFragment() {

//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//    override fun onDestroyView() {
//        _binding = null
//    }

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
//                tv.text = it
            }
        }
    }
}