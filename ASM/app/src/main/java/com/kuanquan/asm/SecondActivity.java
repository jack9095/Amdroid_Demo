package com.kuanquan.asm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * https://blog.csdn.net/generalfu/article/details/105570234
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
