package com.kuanquan.pagetransitionanimation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
//import com.kuanquan.pagetransitionanimation.copy.MainFragment;


public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = new MainFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, mainFragment)   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }

    // TODO 3. 共享元素动画  这一步必须要放到 Activity 中做，其他的可以放到 fragment 中处理
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        Log.e("onActivityReenter", "requestCode = " + resultCode);
        mainFragment.setBundle(new Bundle(data.getExtras()));
//        mainFragment.bundle = new Bundle(data.getExtras());
    }
}
