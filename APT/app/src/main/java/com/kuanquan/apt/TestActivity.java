package com.kuanquan.apt;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.inject_annotation.BindView;
import com.mobile.inject.ViewBinder;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // 调用自己定义的 ViewBinder
        ViewBinder.bind(this);
        tv.setText("Hi,ViewBinder!");
    }
}
