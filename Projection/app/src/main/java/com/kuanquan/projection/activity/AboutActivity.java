package com.kuanquan.projection.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kuanquan.projection.R;

/**
 * Created by huzongyao on 2018/3/27.
 * About activity
 */

public class AboutActivity extends AppCompatActivity {

    TextView mTextviewVersion;
    TextView mAboutContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getPackageVersionInfo();
        mTextviewVersion = findViewById(R.id.textview_version);
        mAboutContent = findViewById(R.id.about_content);
        mAboutContent.setText(R.string.about_content);
    }

    private void getPackageVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mTextviewVersion.setText(packageInfo.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
