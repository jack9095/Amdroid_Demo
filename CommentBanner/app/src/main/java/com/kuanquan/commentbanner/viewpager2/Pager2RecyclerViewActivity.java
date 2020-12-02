//package com.kuanquan.commentbanner.viewpager2;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.blankj.utilcode.util.ToastUtils;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.chad.library.adapter.base.listener.OnItemClickListener;
//import com.kuanquan.commentbanner.R;
//import com.kuanquan.commentbanner.bean.Pager2BannerBean;
//import com.kuanquan.commentbanner.bean.TextBean;
//import com.kuanquan.commentbanner.util.Utils;
//import com.kuanquan.commentbanner.v2.Banner;
//import com.kuanquan.commentbanner.v2.IndicatorView;
//import com.kuanquan.commentbanner.viewpager2.adapter.ImageAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * date 2019-08-27
// */
//public class Pager2RecyclerViewActivity extends AppCompatActivity {
//
//    private BannerAdapter adapter;
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private Banner banner;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recycler);
////        StatusBarUtil.setStatusBarColor(this, Color.WHITE);
//        RecyclerView recyclerView = findViewById(R.id.list);
//
//        swipeRefreshLayout = findViewById(R.id.swipe);
//
//        adapter = new BannerAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData();
//            }
//        });
//
//        View inflate = View.inflate(this, R.layout.item_view_pager2_banner, null);
//        TextView textView = inflate.findViewById(R.id.text);
//        textView.setText("我是被addHeaderView添加进来的");
//        adapter.addHeaderView(inflate);
//        banner = inflate.findViewById(R.id.banner);
//        banner.setIndicator(new IndicatorView(this).setIndicatorColor(Color.GRAY).setIndicatorSelectorColor(Color.WHITE));
//        ImageAdapter imageAdapter = new ImageAdapter();
//        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort(String.valueOf(banner.getCurrentPager()));
//            }
//        });
//        imageAdapter.replaceData(Utils.getImage(4));
//        banner.setAdapter(imageAdapter);
//
//
//        loadData();
//
//        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Pager2BannerBean b = (Pager2BannerBean) adapter.getItem(0);
//                b.urls.clear();
//                int i = new Random().nextInt(4) + 1;
//                List<Integer> data = Utils.getImage(i);
//                b.urls.addAll(data);
//                adapter.setData(0, b);
//            }
//        });
//
//    }
//
//    private void loadData() {
//
//        List<MultiItemEntity> list = new ArrayList<>();
//
//        Pager2BannerBean bannerBean = new Pager2BannerBean();
//        bannerBean.urls = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            bannerBean.urls.add(Utils.getRandomImage());
//        }
//        list.add(bannerBean);
//        for (int i = 0; i < 100; i++) {
//            TextBean textBean = new TextBean();
//            textBean.text = "--- " + i;
//            list.add(textBean);
//        }
//        adapter.replaceData(list);
//
//        swipeRefreshLayout.setRefreshing(false);
//
//    }
//
//}
