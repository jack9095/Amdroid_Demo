package com.kuanquan.hscrollrecyclerviewapplication.frgment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuanquan.hscrollrecyclerviewapplication.R
import com.kuanquan.hscrollrecyclerviewapplication.adapter.TestAdapter
import com.kuanquan.hscrollrecyclerviewapplication.bean.TestBean

class TestFragment: BaseFragment() {

    companion object {

        const val PARAMS_TEXT = "params_text"

        @JvmStatic
        fun newInstance(str: String?) = TestFragment().apply {
            arguments = Bundle().apply {
                putString(PARAMS_TEXT, str)
            }
        }

    }

    private val recyclerView : RecyclerView by lazy {
        view.findViewById<RecyclerView>(R.id.recycler_view)
    }

    private val testAdapter : TestAdapter by lazy {
        TestAdapter()
    }

    val list : MutableList<TestBean> by lazy {
        mutableListOf<TestBean>().apply {
            for (i in 0..2) {
                add(TestBean("$i"))
            }

            add(TestBean(id = "100", type = TestBean.TYPE_SCROLL))
            add(TestBean(id = "101", type = TestBean.TYPE_SCROLL))

            for (i in 0..9) {
                add(TestBean("$i"))
            }
        }
    }

    override fun onClick(v: View?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId(): Int = R.layout.test_layout

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = testAdapter
        }
        testAdapter.setList(list)

        // 必须设置Diff Callback
//        testAdapter.setDiffCallback(DiffDemoCallback())
//        testAdapter.setDiffNewData(list)
    }
}