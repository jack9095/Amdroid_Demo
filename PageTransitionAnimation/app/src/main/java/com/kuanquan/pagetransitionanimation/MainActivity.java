package com.kuanquan.pagetransitionanimation;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.kuanquan.pagetransitionanimation.adapter.MyAdapter;
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity;
import com.kuanquan.pagetransitionanimation.util.DataUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<String> datas;
//    private MyAdapter myAdapter;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();

        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container, mainFragment)   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();

//        datas = DataUtil.INSTANCE.getData();

//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//        myAdapter = new MyAdapter(this, datas);
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
//            @Override
//            public void onClick(View v, int position) {
//                Intent intent = new Intent(MainActivity.this, ShareElementsActivity.class);
//                intent.putExtra("url", (Serializable) datas);
//                intent.putExtra("index", position);
//
//                // TODO 1. 共享元素动画
//                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, datas.get(position)).toBundle();
//                startActivity(intent,options);
////                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle());
//            }
//        });

        // TODO 2. 共享元素动画
//        setAnimator(gridLayoutManager);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        int position = bundle.getInt("index", 0);
                        sharedElements.clear();
                        names.clear();
                        View itemView = mainFragment.gridLayoutManager.findViewByPosition(position);
                        ImageView imageView = itemView.findViewById(R.id.image);
                        names.add(itemView.getTransitionName());
                        //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
                        sharedElements.put(mainFragment.datas.get(position), imageView);
                        bundle = null;
                    }
                }
            });
        }

        getWindow().getDecorView().postDelayed(runnableTabLayout, 1000);
    }

    private final Runnable runnableTabLayout = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().getDecorView().removeCallbacks(runnableTabLayout);
    }

    public void setAnimator(final GridLayoutManager lm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        try {
                            int position = bundle.getInt("index", 0);
                            sharedElements.clear();
                            names.clear();
                            View itemView = lm.findViewByPosition(position);
                            ImageView imageView = itemView.findViewById(R.id.image);
                            names.add(itemView.getTransitionName());
                            //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
//                            sharedElements.put(datas.get(position), itemView);
                            bundle = null;
                        } catch (Exception e) {

                        }
                    }
                }
            });
        }
    }

    private View shareView;
    private String shareUrl;

    public void setAnimatorFragment(String url, View view, List<String> lists) {
        shareView = view;
        shareUrl = url;
        datas = lists;
    }

    private Bundle bundle;

    // TODO 3. 共享元素动画
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        bundle = new Bundle(data.getExtras());
//    }
}
