package com.kuanquan.demo.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseMAinActivity extends AppCompatActivity {

    /************************************************  跳转第三方APP  ********************************************************/
    public void aiqiyi(View view) {
        startAPP("com.qiyi.video");
    }

    public void TencentVideo(View view) {
        startAPP("com.tencent.qqlive");
    }

    public void youku(View view) {
        startAPP("com.youku.phone");
    }

    public void bilibili(View view) {
        startAPP("tv.danmaku.bili");
    }

    public void souhu(View view) {
        startAPP("com.sohu.sohuvideo");
    }

    public void haokan(View view) {
        startAPP("com.baidu.haokan");
    }

    public void xigua(View view) {
        startAPP("com.ss.android.article.video");
    }

    public void mangguo(View view) {
        startAPP("com.hunantv.imgo.activity");
    }

    public void hanju(View view) {
        startAPP("com.babycloud.hanju");
    }

    public void renren(View view) {
        startAPP("com.zhongduomei.rrmj.society");
    }

    private void startAPP(String packageName){
        // 获取目标应用安装包的 Intent
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    /**
     * 检测 app 是否安装
     * @param context 上下文
     * @param packageName 包名
     * @return true 存在 false 不存在
     */
    @SuppressLint("WrongConstant")
    private boolean uninstallSoftware(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
