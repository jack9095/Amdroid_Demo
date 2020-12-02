//package com.kuanquan.commentbanner.indicator;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.RelativeLayout;
//
//
//import com.kuanquan.commentbanner.v2.Indicator;
//
//import net.lucode.hackware.magicindicator.MagicIndicator;
//import net.lucode.hackware.magicindicator.buildins.UIUtil;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
//import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * auth aboom
// * date 2019-12-26
// */
//public class LinePagerTitleIndicatorView extends MagicIndicator implements Indicator {
//    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
//    private List<String> mDataList = Arrays.asList(CHANNELS);
//
//    private CommonNavigator commonNavigator;
//
//    private int pagerCount;
//
//    public LinePagerTitleIndicatorView(Context context) {
//        this(context, null);
//    }
//
//    public LinePagerTitleIndicatorView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        commonNavigator = new CommonNavigator(context);
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (position != pagerCount - 1) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//        } else {
//            if (positionOffset > .5) {
//                super.onPageScrolled(0, 0, 0);
//            } else {
//                super.onPageScrolled(position, 0, 0);
//            }
//        }
//    }
//
//    @Override
//    public void initIndicatorCount(final int pagerCount) {
//        this.pagerCount = pagerCount;
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//
//            @Override
//            public int getCount() {
//                return pagerCount;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, int index) {
//                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
//                simplePagerTitleView.setText(mDataList.get(index));
//                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
//                simplePagerTitleView.setSelectedColor(Color.RED);
//                return simplePagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                indicator.setLineHeight(UIUtil.dip2px(context, 6));
//                indicator.setLineWidth(UIUtil.dip2px(context, 10));
//                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//                indicator.setColors(Color.RED);
//                return indicator;
//            }
//        });
//
//        setNavigator(commonNavigator);
//    }
//
//    @Override
//    public View getView() {
//        return this;
//    }
//
//    @Override
//    public RelativeLayout.LayoutParams getParams() {
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, UIUtil.dip2px(getContext(), 45));
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        params.bottomMargin = UIUtil.dip2px(getContext(), 20);
//        return params;
//    }
//}
