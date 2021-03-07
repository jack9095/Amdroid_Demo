package com.mumu.easyemoji;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by MuMu on 2016/11/2/0002.
 */

public class FragmentPager extends FragmentStatePagerAdapter {
    private final List<EmoJiFragment> fragments;

    public FragmentPager(FragmentManager fm, List<EmoJiFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
