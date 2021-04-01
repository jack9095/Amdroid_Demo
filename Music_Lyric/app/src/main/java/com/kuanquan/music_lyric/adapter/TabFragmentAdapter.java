package com.kuanquan.music_lyric.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: tab适配器
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {
    //存储所有的fragment
    private List<Fragment> list;

    public TabFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;

    }

    @Override
    public Fragment getItem(int index) {

        return list.get(index);
    }

    @Override
    public int getCount() {

        return list.size();
    }

}
