package com.kuanquan.annotationandeventbusdemo.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.annotationandeventbusdemo.R;
import com.kuanquan.annotationandeventbusdemo.eventbus.annotation.Subscribe;


public class OneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        EventBus.getInstance().register(this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getInstance().postStick("跳转到B");
                startActivity(new Intent(OneActivity.this,TwoActivity.class));
            }
        });
    }

    @Subscribe(value = ThreadMode.MAIN)
    public void onEventMethod(String str){
        Log.e("OneActivity",str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }
}
