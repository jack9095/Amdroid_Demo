package com.kuanquan.emotionkeyboard.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zejian
 * Time  16/1/7 下午2:09
 * Email shinezejian@163.com
 * Description:
 */
public class NoHorizontalScrollerVPAdapter  extends FragmentPagerAdapter {

    private List<Fragment> datas = null;

    public NoHorizontalScrollerVPAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        this.datas = datas;

    }


    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
        super.destroyItem(container, position, object);
    }
}
