package com.kuanquan.hscrollrecyclerviewapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kuanquan.hscrollrecyclerviewapplication.frgment.HomeFragment
import com.kuanquan.hscrollrecyclerviewapplication.frgment.TestFragment

/**
 * 这样也可以达到缓存的目的,可以避免一次性把 fragment 压入栈内
 */
class AdapterFragmentPagerOther(fragmentActivity: FragmentActivity, val data: MutableList<String>) : FragmentStateAdapter(fragmentActivity) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setItemViewCacheSize(data.size)
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment.newInstance(data[position])
            1 -> return TestFragment.newInstance(data[position])
            2 -> return HomeFragment.newInstance(data[position])
            else -> return HomeFragment.newInstance(data[position])
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
