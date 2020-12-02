package com.kuanquan.commentbanner.util;


import android.content.res.Resources;
import android.graphics.Outline;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

import com.kuanquan.commentbanner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * auth aboom
 * date 2019-12-26
 */
public class Utils {
    public static final int[] IMAGES = {
            R.mipmap.image1,
            R.mipmap.image2,
            R.mipmap.image3,
            R.mipmap.image4,
            R.mipmap.image5,
    };

    public static List<Integer> getImage(int count) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(IMAGES[i]);
        }
        return list;
    }

    public static List<String> getUrl(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(URLS[i]);
        }
        return list;
    }

    public static int getRandomImage() {
        return IMAGES[new Random().nextInt(IMAGES.length)];
    }


    /**
     * -----------------------------------------------------------------------
     */

    private static final String[] URLS = {
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2860421298,3956393162&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=163638141,898531478&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1028426622,4209712325&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1462142898,440466184&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3210855908,3095539181&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2080505558,2205047574&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2894881224,342594760&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2894881224,342594760&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3162346827,2000964752&fm=26&gp=0.jpg"
    };

    public static String getRandom() {
        return URLS[new Random().nextInt(URLS.length)];
    }

    public static List<String> getData(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(getRandom());
        }
        return list;
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 设置view圆角
     *
     * @param radius
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setBannerRound(View view, float radius) {
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        view.setClipToOutline(true);
    }

}
