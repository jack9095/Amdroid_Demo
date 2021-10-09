package com.kuanquan.videocover.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kuanquan.videocover.dialog.PictureLoadingDialog;
import com.kuanquan.videocover.entity.PictureConfig;
import com.kuanquan.videocover.entity.PictureSelectionConfig;

public abstract class PictureBaseActivity extends AppCompatActivity {
    protected View container;
    protected PictureLoadingDialog mLoadingDialog;
    protected PictureSelectionConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            config = savedInstanceState.getParcelable(PictureConfig.EXTRA_CONFIG);
        }
        if (config == null) {
            config = getIntent() != null ? getIntent().getParcelableExtra(PictureConfig.EXTRA_CONFIG) : config;
        }
        super.onCreate(savedInstanceState == null ? new Bundle() : savedInstanceState);
//        super.onCreate(savedInstanceState);
        initWidgets();
    }

    /**
     * init Views
     */
    protected void initWidgets() {

    }

    protected Context getContext() {
        return this;
    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        try {
            if (!isFinishing()) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new PictureLoadingDialog(getContext());
                }
                if (mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        if (!isFinishing()) {
            try {
                if (mLoadingDialog != null
                        && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            } catch (Exception e) {
                mLoadingDialog = null;
                e.printStackTrace();
            }
        }
    }
}
