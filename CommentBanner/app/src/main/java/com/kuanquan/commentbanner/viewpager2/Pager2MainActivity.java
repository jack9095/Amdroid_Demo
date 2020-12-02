package com.kuanquan.commentbanner.viewpager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.kuanquan.commentbanner.R;
import com.kuanquan.commentbanner.util.ArrayStringItemSelectDialog;
import com.kuanquan.commentbanner.util.Utils;
import com.kuanquan.commentbanner.v2.Banner;
import com.kuanquan.commentbanner.v2.IndicatorView;
import com.kuanquan.commentbanner.viewpager2.adapter.ImageAdapter;
import com.saeed.infiniteflow.lib.OverlapSliderTransformer;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * auth aboom
 * date 2020-01-13
 */
public class Pager2MainActivity extends AppCompatActivity {

    public static final String[] ANIMS = {
            "NONE",
            "OverlapSliderTransformer",
            "SimpleSliderTransformer",
            "StackSliderTransformer",
    };

    private static final String[] INDICATOR_STR = {
            "INDICATOR_CIRCLE",
            "INDICATOR_CIRCLE_RECT",
            "INDICATOR_BEZIER",
            "INDICATOR_DASH",
            "INDICATOR_BIG_CIRCLE",
    };

    private Banner banner;
    private TextView noLoop;
    private int style;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager2);

        banner = findViewById(R.id.banner);
        final IndicatorView indicatorView = new IndicatorView(this)
                .setIndicatorRatio(1f)
                .setIndicatorRadius(2f)
                .setIndicatorSelectedRatio(3)
                .setIndicatorSelectedRadius(2f)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE)
                .setIndicatorColor(Color.GRAY)
                .setIndicatorSelectorColor(Color.WHITE);

        banner.setAutoPlay(false)
                .setIndicator(indicatorView)
                .setOrientation(ViewPager2.ORIENTATION_HORIZONTAL)
                .setPagerScrollDuration(800)
                .setPageMargin(Utils.dp2px(40), 0)
//                .addPageTransformer(new ScaleInTransformer())
                .addPageTransformer(new OverlapSliderTransformer(banner.getViewPager2().getOrientation(), 0.25f, 0, 1, 0))
                .setOuterPageChangeListener(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        Log.e("aa", "onPageSelected position " + position);
                    }
                });


        final ImageAdapter adapter = new ImageAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //onItemClick回调position是holder.getAdapterPosition()的，
                //holder.getAdapterPosition()是轮播中当前位置，可以通过banner.getCurrentPager()获取真实位置。
//                ToastUtils.showShort(String.valueOf(banner.getCurrentPager()) + " 和 经过处理 onItemClick 上参数position=" + position);


            }
        });
        adapter.addData(Utils.getRandom());
        adapter.addData(Utils.getRandom());
        banner.setAdapter(adapter);


        /*--------------- 下面是按钮点击事件 ---------------*/

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(Utils.getRandom());
                updateLoopText();
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getData().size() == 0) {
                    return;
                }
                int listRandom = new Random().nextInt(adapter.getData().size());
                Toast.makeText(Pager2MainActivity.this, "删除第" + listRandom + " 张", Toast.LENGTH_SHORT).show();
                adapter.remove(listRandom);
                updateLoopText();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object> data = adapter.getData();
                final int size = data.size();
                data.clear();
                for (int i = 0; i < size; i++) {
                    data.add(Utils.getRandom());
                }
                adapter.replaceData(data);
                updateLoopText();
            }
        });

        final TextView updateStyle = findViewById(R.id.updateStyle);
        updateStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ArrayStringItemSelectDialog(Pager2MainActivity.this)
                        .setValueStrings(Arrays.asList(INDICATOR_STR))
                        .setChoose(style)
                        .setOnItemClickListener(new ArrayStringItemSelectDialog.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, String value) {
                                updateStyle.setText(value);
                                style = position;
                                indicatorView.setIndicatorStyle(style);
                            }
                        }).show();
            }
        });

        noLoop = findViewById(R.id.noLoop);

        findViewById(R.id.noLoop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (banner.isAutoPlay()) {
                    banner.setAutoPlay(false);
                    updateLoopText();
                } else {
                    banner.setAutoPlay(true);
                    if (!banner.isAutoPlay()) {
                        Toast.makeText(Pager2MainActivity.this, "轮播页数需要大于1", Toast.LENGTH_SHORT).show();
                    }
                    updateLoopText();
                }
            }
        });

        updateLoopText();

        TextView transformerText = findViewById(R.id.transformer);
        transformerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShort("可以去看看ViewPager入口里的动画切换");
            }
        });

    }

    private void updateLoopText() {
        if (banner.isAutoPlay()) {
            noLoop.setText("停止自动轮播");
        } else {
            noLoop.setText("开始自动轮播");
        }
    }

}
