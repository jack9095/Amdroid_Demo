package com.kuanquan.customlayoutmanager;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_act);

        int res = getIntent().getIntExtra("resId",0);
        if(res != 0){
            Glide.with(this).load(res).into((ImageView) findViewById(R.id.img));
        }
    }
}
