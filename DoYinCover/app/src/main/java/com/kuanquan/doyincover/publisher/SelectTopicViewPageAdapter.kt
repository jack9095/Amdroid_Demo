package com.shuashuakan.android.modules.publisher

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.shuashuakan.android.data.api.model.ugc.TopicCategory

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/10/10
 * Description:
 */
class SelectTopicViewPageAdapter(
    fm: FragmentManager,
    private val categories: List<TopicCategory>
) : FragmentStatePagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    return TopicSublistFragment.create(categories[position].id, categories[position].name)
  }

  override fun getCount(): Int {
    return categories.size
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return categories[position].name
  }
}