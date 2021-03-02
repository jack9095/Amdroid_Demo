package com.kuanquan.panelemojikeyboard.scene.feed;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;


public abstract class BaseDialogFragment extends DialogFragment {

    protected Context mContext;
    protected View mRootView;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    protected int gravity() {
        return Gravity.CENTER;
    }

    protected boolean canceledOnTouchOutside() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(canceledOnTouchOutside());
        mRootView = inflater.inflate(getLayoutId(), container, true);
        initData();
        initView();

//        Window window = getDialog().getWindow();
//        if (window != null) {
//            window.setGravity(Gravity.CENTER);
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//            WindowManager.LayoutParams windowParams = window.getAttributes();
//            windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//            windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//            window.setAttributes(windowParams);
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.setDimAmount(0f);
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
//        }



        return mRootView;
    }

    protected void initData() {
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    public void show(FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!isAdded()) {
            transaction.add(this, this.getClass().getSimpleName());
        }
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void dismiss() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        try {
            dismissAllowingStateLoss();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDismiss(@NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
