package com.kuanquan.viewpager2

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 这样也可以达到缓存的目的,可以避免一次性把 fragment 压入栈内
 */
class AdapterFragmentPagerOther(fragmentActivity: FragmentActivity, val data: MutableList<String>) : FragmentStateAdapter(fragmentActivity) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setItemViewCacheSize(data.size)
    }

    override fun createFragment(position: Int): Fragment {
        return HomeFragment.newInstance(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
