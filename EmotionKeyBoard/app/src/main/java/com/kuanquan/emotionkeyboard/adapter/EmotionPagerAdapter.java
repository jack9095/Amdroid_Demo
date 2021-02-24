package com.kuanquan.emotionkeyboard.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
/**
 * Created by zejian
 * Time  16/1/7 下午4:09
 * Email shinezejian@163.com
 * Description: EmotiomComplateFragment界面Viewpager数据适配器
 */
public class EmotionPagerAdapter extends PagerAdapter {

	private List<GridView> gvs;

	public EmotionPagerAdapter(List<GridView> gvs) {
		this.gvs = gvs;
	}

	@Override
	public int getCount() {
		return gvs.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(gvs.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(gvs.get(position));
		return gvs.get(position);
	}

}
