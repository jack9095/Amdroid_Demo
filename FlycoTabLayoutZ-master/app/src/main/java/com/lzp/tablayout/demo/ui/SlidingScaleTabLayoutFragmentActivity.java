package com.lzp.tablayout.demo.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.flyco.tablayout.SlidingScaleTabLayout;
import com.lzp.tablayout.demo.R;

/**
 * 与Fragment一起使用的SlidingScaleTabLayout
 */
public class SlidingScaleTabLayoutFragmentActivity extends AppCompatActivity {

    private String[] titles = {"Activity", "Likes", "Followers", "Mine"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlidingScaleTabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position % titles.length];
        }

        @Override
        public Fragment getItem(int i) {
            return SimpleCardFragment.getInstance("这是第" + i + "个Fragment");
        }
    }

}
