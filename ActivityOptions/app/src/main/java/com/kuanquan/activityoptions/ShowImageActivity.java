package com.kuanquan.activityoptions;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class ShowImageActivity extends AppCompatActivity {

    ImageView mImageViewShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        mImageViewShow = findViewById(R.id.iv_show);
        String url = getIntent().getStringExtra("imageURL");
        Glide.with(ShowImageActivity.this)
                .load(url)
//                .fitCenter()
//                .dontAnimate()
                .into(mImageViewShow);
    }

}
