package com.kuanquan.pagetransitionanimation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    private List<String> datas;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxBus.get().register(this);
        datas = DataUtil.INSTANCE.getData();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        myAdapter = new MyAdapter(this, datas);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, ShareElementsActivity.class);
                intent.putExtra("url", (Serializable) datas);
                intent.putExtra("index", position);

                // TODO 1. 共享元素动画
                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, v, datas.get(position)).toBundle();
                startActivity(intent,options);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle());
            }
        });

    }

}
