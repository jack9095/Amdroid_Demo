package com.example.expandtextview.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;

import com.example.expandtextview.R;

/**
 * @作者: njb
 * @时间: 2020/1/3 18:11
 * @描述: 观众端
 */
public class WatchLiveActivity extends AppCompatActivity {
    private AppCompatTextView liveMemberCount;
    ImageView liveClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_live_stream);
        initView();
        setListener();
    }

    private void initView() {
        liveMemberCount = findViewById(R.id.liveMemberCount);
        liveClose = findViewById(R.id.liveClose);
        //自适应手机大小
        TextViewCompat.setAutoSizeTextTypeWithDefaults(liveMemberCount, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(liveMemberCount, 8, 10, 1, TypedValue.COMPLEX_UNIT_SP);
        liveMemberCount.setText("8888");
    }

    private void setListener() {
        liveClose.setOnClickListener(v -> finish());
    }

}
