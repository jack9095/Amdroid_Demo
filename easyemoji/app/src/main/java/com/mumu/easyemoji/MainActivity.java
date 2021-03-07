package com.mumu.easyemoji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mumu.easyemoji.user.dialog.CustomDialogFragment;

/**
 * Created by MuMu on 2016/11/11/0011.
 */

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mToolBar.setTitle("EasyEmoJi");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));

        mContext = this;
    }

    public void openWeiXinChat(View view) {
        CustomDialogFragment.newInstance().show(getSupportFragmentManager());
//        startActivity(new Intent(mContext, WeiXinChatActivity.class));
    }

    public void openQQChat(View view) {
        startActivity(new Intent(mContext, MultiEmotionActivity.class));
    }
}
