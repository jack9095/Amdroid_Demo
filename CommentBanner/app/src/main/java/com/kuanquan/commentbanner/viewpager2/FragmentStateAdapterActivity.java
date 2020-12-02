package com.kuanquan.commentbanner.viewpager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.kuanquan.commentbanner.R;
import com.kuanquan.commentbanner.util.Utils;
import com.kuanquan.commentbanner.v2.Banner;
import com.kuanquan.commentbanner.v2.IndicatorView;
import com.kuanquan.commentbanner.v2.ScaleInTransformer;
import com.kuanquan.commentbanner.viewpager2.adapter.FragmentAdapter;


/**
 * auth aboom
 * date 2020/6/6
 */
public class FragmentStateAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_adapter);

        Banner banner = findViewById(R.id.banner);
        final IndicatorView indicatorView = new IndicatorView(this)
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRatio(3)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_CIRCLE)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.WHITE);


        FragmentAdapter fragmentAdapter = new FragmentAdapter(this);

        banner.setAutoPlay(false)
                .setIndicator(indicatorView)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(800)
                .setPageMargin((int)Utils.dp2px(40), (int)Utils.dp2px(10))
                .addPageTransformer(new ScaleInTransformer())


                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Log.e("aa", "onPageSelected position " + position);
                    }
                })
                .setAdapter(fragmentAdapter);
        fragmentAdapter.addData(Utils.getImage(2));



//        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
////        viewPager2.setOffscreenPageLimit(2);
//        ImageAdapter adapter = new ImageAdapter();
//        viewPager2.setAdapter(adapter);
//        adapter.addData(Utils.getData(10));


    }
}
