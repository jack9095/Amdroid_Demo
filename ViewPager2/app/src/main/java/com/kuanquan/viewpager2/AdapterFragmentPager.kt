package com.kuanquan.viewpager2

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterFragmentPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

//    private val fragments: SparseArray<BaseFragment> = SparseArray()
    private val fragments: MutableList<BaseFragment> = mutableListOf()

    init {
        for (it in 0..9) {
            fragments.add(HomeFragment.newInstance("首页$it"))
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    companion object {

        const val PAGE_HOME = 0

        const val PAGE_FIND = 1

        const val PAGE_INDICATOR = 2

        const val PAGE_OTHERS = 3

    }
}
