package com.kuanquan.annotationandeventbusdemo.eventbus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.annotationandeventbusdemo.R;
import com.kuanquan.annotationandeventbusdemo.eventbus.annotation.Subscribe;

import java.lang.ref.WeakReference;


public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MethodManager methodManager = new MethodManager();
        WeakReference<MethodManager> str = new WeakReference<MethodManager>(methodManager);
        setContentView(R.layout.activity_two);
        EventBus.getInstance().register(this);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getInstance().post("王菲");

                finish();
            }
        });
    }

    @Subscribe(value = ThreadMode.MAIN,stick = true)
    public void eventMainThread(Object obj){
        Log.e("TwoActivity",obj + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }
}
