package com.kuanquan.pagetransitionanimation;

import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kuanquan.pagetransitionanimation.adapter.MyAdapter;
import com.kuanquan.pagetransitionanimation.elementspage.DetailActivity2;
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<String> datas;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.get().register(this);
        datas = new ArrayList<>();
        datas.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2478350582,3338695212&fm=26&gp=0.jpg");
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3485183293,847227336&fm=26&gp=0.jpg");
        datas.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2037944750,611917901&fm=26&gp=0.jpg");
        datas.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1745933341,3133887881&fm=26&gp=0.jpg");
        datas.add("http://b162.photo.store.qq.com/psb?/V14EhGon4cZvmh/z2WukT5EhNE76WtOcbqPIgwM2Wxz4Tb7Nub.rDpsDgo!/b/dOaanmAaKQAA");
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3576736246,2692877583&fm=26&gp=0.jpg");
        datas.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2354651587,2307656983&fm=26&gp=0.jpg");
        datas.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3903227663,309999411&fm=26&gp=0.jpg");
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2233497321,4233551410&fm=26&gp=0.jpg");
        datas.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3355332996,90856357&fm=26&gp=0.jpg");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        myAdapter = new MyAdapter(this, datas);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, ShareElementsActivity.class);
//                Intent intent = new Intent(MainActivity.this, DetailActivity2.class);
                intent.putExtra("url", (Serializable) datas);
                intent.putExtra("index", position);
//                Bundle options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
//                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle();
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, datas.get(position)).toBundle();
                startActivity(intent,options);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle());
//                startActivity(intent);
            }
        });

        setAnimator(gridLayoutManager);
    }

    public void setAnimator(final GridLayoutManager lm){
        if (Build.VERSION.SDK_INT >= 22) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        try {
                            int i = bundle.getInt("index", 0);
                            sharedElements.clear();
                            names.clear();
                            View itemView = lm.findViewByPosition(i);
                            ImageView imageView = itemView.findViewById(R.id.image);
                            names.add(imageView.getTransitionName());
                            //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
                            sharedElements.put(datas.get(i), imageView);
                            bundle = null;
                        } catch (Exception e){

                        }
                    }
                }
            });
        }
    }

    private Bundle bundle;
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("updateView")})
    public void updateView(Integer integer) {
        //此处使用rxbus通知对应的view重新显示出来，解决在滑动返回手指拖动的过程中，看到上一个页面点击的图片显示空白的问题
//        View view = myAdapter.getViewByPosition(photosRv, integer, R.id.rv_item_fake_iv);
//        if (view != null) {
//            //以下代码已经没必要设置，因为demo中的动画效果已经全部设置在了rv_item_fake_iv上
//            view.setAlpha(1f);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
