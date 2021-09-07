package com.kuanquan.recyclerviewbanner;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.kuanquan.recyclerviewbanner.adapter.LocalDataAdapter;
import com.kuanquan.recyclerviewbanner.banner.layoutmanager.CenterScrollListener;
import com.kuanquan.recyclerviewbanner.banner.layoutmanager.OverFlyingLayoutManager;

/**
 * 层叠版本
 */
public class OverFlyingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    OverFlyingLayoutManager mOverFlyingLayoutManager;
    Handler mHandler;
    Runnable mRunnable;
    int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_flying);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_banner);
        mOverFlyingLayoutManager = new OverFlyingLayoutManager(0.75f, 385, OverFlyingLayoutManager.HORIZONTAL);

        recyclerView.setAdapter(new LocalDataAdapter());
        recyclerView.setLayoutManager(mOverFlyingLayoutManager);

        recyclerView.addOnScrollListener(new CenterScrollListener());
        mOverFlyingLayoutManager.setOnPageChangeListener(new OverFlyingLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                Log.d("recyclerBanner", currentPosition + " ");
               mOverFlyingLayoutManager.scrollToPosition(currentPosition);
                //  recyclerView.smoothScrollToPosition(currentPosition);
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(mRunnable, 3000);

        SeekBar seekBar = (SeekBar) findViewById(R.id.progress);
        seekBar.setProgress(385);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mOverFlyingLayoutManager.setItemSpace(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
