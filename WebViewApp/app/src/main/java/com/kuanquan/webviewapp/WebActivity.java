package com.kuanquan.webviewapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kuanquan.mylibrary.AccountWebFragment;
import com.kuanquan.mylibrary.BaseWebviewFragment;
import com.kuanquan.mylibrary.CommonWebFragment;
import com.kuanquan.mylibrary.WebConstants;

/**
 *
 */
public class WebActivity extends AppCompatActivity {

    private String title;
    private String url;

    BaseWebviewFragment webviewFragment;

    public static void start(Context context, String title, String url, int testLevel) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebConstants.INTENT_TAG_TITLE, title);
        intent.putExtra(WebConstants.INTENT_TAG_URL, url);
        intent.putExtra("level", testLevel);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        title = getIntent().getStringExtra(WebConstants.INTENT_TAG_TITLE);
        url = getIntent().getStringExtra(WebConstants.INTENT_TAG_URL);
        setTitle(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        int level = getIntent().getIntExtra("level", WebConstants.LEVEL_BASE);
        webviewFragment = null;
        if (level == WebConstants.LEVEL_BASE) {
            webviewFragment = CommonWebFragment.newInstance(url);
        } else {
            webviewFragment = AccountWebFragment.newInstance(url);
        }
        transaction.replace(R.id.web_view_fragment, webviewFragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webviewFragment != null && webviewFragment instanceof BaseWebviewFragment) {
            boolean flag = webviewFragment.onKeyDown(keyCode, event);
            if (flag) {
                return flag;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
