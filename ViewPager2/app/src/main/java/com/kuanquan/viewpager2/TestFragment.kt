package com.kuanquan.viewpager2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kuanquan.viewpager2.adapter.RecycleAdapterDome

class TestFragment: Fragment() {

    companion object {

        const val PARAMS_TEXT = "params_text"

        @JvmStatic
        fun newInstance(str: String?) = TestFragment().apply {
            arguments = Bundle().apply {
                putString(PARAMS_TEXT, str)
            }
        }

        private val ACTION_SHEET_RATIO = 0.75f
        private val ACTION_DIMAMOUNT = 0.5f
    }

    private var viewRoot: View? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("wangfei", "----- onCreateView -----")
//        viewRoot = inflater.inflate(R.layout.test_layout,null,false)
        if (viewRoot == null) {
            Log.e("wangfei", "----- 为空了 -----")
            viewRoot = inflater.inflate(R.layout.test_layout, container, false)
            recyclerView = viewRoot?.findViewById<RecyclerView>(R.id.recyclerView)
            //在这里做一些初始化处理
            initChoiceLayout()
//        } else {
//            val viewGroup = viewRoot?.parent as? ViewGroup
//            viewGroup?.removeView(viewRoot)
        }
        return viewRoot
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChoiceLayout() {
        val list = mutableListOf<String>()
        for (i in 0..90) {
            list.add("这是第" + i + "个测试")
        }

        recyclerView?.postDelayed({

            recyclerView?.run{
                layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
                adapter = RecycleAdapterDome(context,list)
            }

        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 在onDestroyView方法内把Fragment的RootView从ViewPager中remove
        if (viewRoot != null) {
            Log.e("wangfei", "----- 为空了 onDestroyView -----")
            (viewRoot?.parent as? ViewGroup)?.removeView(viewRoot)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}