package com.kuanquan.music_lyric.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.kuanquan.music_lyric.application.HPApplication;
import com.kuanquan.music_lyric.libs.utils.LoggerUtil;

import java.util.Date;

/**
 */

public class FragmentReceiver {

    /**
     *
     */
    private LoggerUtil logger;
    /**
     * 是否注册成功
     */
    private boolean isRegisterSuccess = false;
    private Context mContext;
    private HPApplication mHPApplication;
    /**
     * 注册成功广播
     */
    private String ACTION_FRAGMENTSUCCESS = "com.kuanquan.music_lyric.fragment.success_" + new Date().getTime();
    /**
     * 关闭
     */
    public static final String ACTION_CLOSEDFRAGMENT = "com.kuanquan.music_lyric.close.fragment";
    /**
     * 打开单个排行的歌曲列表
     */
    public static final String ACTION_OPENRANKSONGFRAGMENT = "com.kuanquan.music_lyric.fragment.open.ranksong";

    /**
     * 本地音乐界面
     */
    public static final String ACTION_OPENLOCALMUSICFRAGMENT = "com.kuanquan.music_lyric.fragment.open.localmusic";

    /**
     * 喜欢
     */
    public static final String ACTION_OPENLIKEMUSICFRAGMENT = "com.kuanquan.music_lyric.fragment.open.likemusic";

    /**
     * 下载
     */
    public static final String ACTION_OPENDOWNLOADMUSICFRAGMENT = "com.kuanquan.music_lyric.fragment.open.downloadmusic";
    /**
     * 最近
     */
    public static final String ACTION_OPENRECENTMUSICFRAGMENT = "com.kuanquan.music_lyric.fragment.open.recentmusic";
    private BroadcastReceiver mFragmentBroadcastReceiver;
    private IntentFilter mFragmentIntentFilter;
    private FragmentReceiverListener mFragmentReceiverListener;

    public FragmentReceiver(Context context, HPApplication hPApplication) {
        this.mHPApplication = hPApplication;
        this.mContext = context;
        logger = LoggerUtil.getZhangLogger(context);
        mFragmentIntentFilter = new IntentFilter();
        //
        mFragmentIntentFilter.addAction(ACTION_FRAGMENTSUCCESS);
        mFragmentIntentFilter.addAction(ACTION_CLOSEDFRAGMENT);
        mFragmentIntentFilter.addAction(ACTION_OPENRANKSONGFRAGMENT);
        mFragmentIntentFilter.addAction(ACTION_OPENLOCALMUSICFRAGMENT);
        mFragmentIntentFilter.addAction(ACTION_OPENLIKEMUSICFRAGMENT);
        mFragmentIntentFilter.addAction(ACTION_OPENDOWNLOADMUSICFRAGMENT);
        mFragmentIntentFilter.addAction(ACTION_OPENRECENTMUSICFRAGMENT);
    }

    private Handler mFragmentHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (mFragmentReceiverListener != null) {
                Intent intent = (Intent) msg.obj;
                if (intent.getAction().equals(ACTION_FRAGMENTSUCCESS)) {
                    isRegisterSuccess = true;
                } else {
                    mFragmentReceiverListener.onReceive(mContext, intent);
                }

            }
        }
    };


    /**
     * 注册广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {
        if (mFragmentBroadcastReceiver == null) {
            //
            mFragmentBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Message msg = new Message();
                    msg.obj = intent;
                    mFragmentHandler.sendMessage(msg);


                }
            };

            mContext.registerReceiver(mFragmentBroadcastReceiver, mFragmentIntentFilter);
            //发送注册成功广播
            Intent successIntent = new Intent(ACTION_FRAGMENTSUCCESS);
            successIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            mContext.sendBroadcast(successIntent);

        }
    }

    /**
     * 取消注册广播
     */
    public void unregisterReceiver(Context context) {
        if (mFragmentBroadcastReceiver != null && isRegisterSuccess) {

            mContext.unregisterReceiver(mFragmentBroadcastReceiver);

        }

    }


    ////////////////////
    public interface FragmentReceiverListener {
        void onReceive(Context context, Intent intent);
    }

    public void setFragmentReceiverListener(FragmentReceiverListener mFragmentReceiverListener) {
        this.mFragmentReceiverListener = mFragmentReceiverListener;
    }
}
