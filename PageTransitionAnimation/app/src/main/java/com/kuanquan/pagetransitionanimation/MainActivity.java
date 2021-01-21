package com.kuanquan.pagetransitionanimation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.pagetransitionanimation.adapter.MyAdapter;
import com.kuanquan.pagetransitionanimation.elementspage.DetailActivity2;
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        MyAdapter myAdapter = new MyAdapter(this, datas);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, ShareElementsActivity.class);
//                Intent intent = new Intent(MainActivity.this, DetailActivity2.class);
                intent.putExtra("url",datas.get(position));
//                Bundle options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                Bundle options =ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle();
                startActivity(intent,options);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, v, "sharedView").toBundle());
//                startActivity(intent);
            }
        });


    }
}
