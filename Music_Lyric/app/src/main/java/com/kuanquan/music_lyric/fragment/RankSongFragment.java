package com.kuanquan.music_lyric.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.music_lyric.R;
import com.kuanquan.music_lyric.adapter.RankSongAdapter;
import com.kuanquan.music_lyric.libs.utils.ToastUtil;
import com.kuanquan.music_lyric.model.AudioInfo;
import com.kuanquan.music_lyric.net.api.RankSongHttpUtil;
import com.kuanquan.music_lyric.net.entity.RankListResult;
import com.kuanquan.music_lyric.net.model.HttpResult;
import com.kuanquan.music_lyric.receiver.AudioBroadcastReceiver;
import com.kuanquan.music_lyric.receiver.FragmentReceiver;
import com.kuanquan.music_lyric.utils.AsyncTaskHttpUtil;
import com.kuanquan.music_lyric.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * 排行详情
 */
public class RankSongFragment extends BaseFragment {

    /**
     *
     */
    private RankListResult mRankListResult;

    //
    private RankSongAdapter mAdapter;
    private ArrayList<AudioInfo> mDatas;

    /**
     * 列表视图
     */
    private RecyclerView mRecyclerView;

    private static final int LOADDATA = 0;
    /**
     * http请求
     */
    private AsyncTaskHttpUtil mAsyncTaskHttpUtil;

    /**
     *
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADDATA:
                    loadDataUtil(300, true);
                    break;
            }
        }
    };

    /**
     * 页码
     */
    private int mPage = 1;
    /**
     * 每页显示条数
     */
    private int mPageSize = 30;

    private AudioBroadcastReceiver mAudioBroadcastReceiver;

    /**
     * 广播监听
     */
    private AudioBroadcastReceiver.AudioReceiverListener mAudioReceiverListener = new AudioBroadcastReceiver.AudioReceiverListener() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doAudioReceive(context, intent);
        }
    };


    public RankSongFragment() {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mRankListResult == null)
            mRankListResult = mHPApplication.getRankListResult();
    }

    /**
     * 处理音频监听事件
     *
     * @param context
     * @param intent
     */
    private void doAudioReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (action.equals(AudioBroadcastReceiver.ACTION_NULLMUSIC)) {
            mAdapter.reshViewHolder(null);
        } else if (action.equals(AudioBroadcastReceiver.ACTION_INITMUSIC)) {
            //初始化
            // AudioMessage audioMessage = (AudioMessage) intent.getSerializableExtra(AudioMessage.KEY);
            AudioInfo audioInfo = mHPApplication.getCurAudioInfo();//audioMessage.getAudioInfo();
            mAdapter.reshViewHolder(audioInfo);
        }
    }


    @Override
    protected int setContentViewId() {
        return R.layout.layout_fragment_rank;
    }

    @Override
    protected int setTitleViewId() {
        return R.layout.layout_title;
    }

    @Override
    protected void initViews(Bundle savedInstanceState, View mainView) {
        //
        TextView titleView = mainView.findViewById(R.id.title);
        if (mRankListResult == null)
            mRankListResult = mHPApplication.getRankListResult();
        titleView.setText(mRankListResult.getRankName());
        //
        RelativeLayout backRelativeLayout = mainView.findViewById(R.id.backImg);
        backRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent closeIntent = new Intent(FragmentReceiver.ACTION_CLOSEDFRAGMENT);
                closeIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                mActivity.sendBroadcast(closeIntent);
            }
        });


        //
        mRecyclerView = mainView.findViewById(R.id.rank_recyclerView);
        //初始化内容视图
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));

        //
        mDatas = new ArrayList<AudioInfo>();
        mAdapter = new RankSongAdapter(mHPApplication, mActivity.getApplicationContext(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setRankSongListener(new RankSongAdapter.RankSongListener() {
            @Override
            public void loadMoreData() {
                loadMoreDataHandler();
            }
        });


        showLoadingView();

        setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                showLoadingView();

                loadDataUtil(300, true);
            }
        });


        //
        //注册监听
        mAudioBroadcastReceiver = new AudioBroadcastReceiver(mActivity.getApplicationContext(), mHPApplication);
        mAudioBroadcastReceiver.setAudioReceiverListener(mAudioReceiverListener);
        mAudioBroadcastReceiver.registerReceiver(mActivity.getApplicationContext());

    }

    @Override
    protected void loadData(boolean isRestoreInstance) {
        if (isRestoreInstance) {
            mPage = 1;
            mDatas.clear();
        }
        mHandler.sendEmptyMessage(LOADDATA);

    }

    /**
     * 加载更多数据
     */
    private void loadMoreDataHandler() {
        loadDataUtil(0, false);
    }

    /**
     * 加载数据
     */
    private void loadDataUtil(int sleepTime, final boolean showView) {
        //
        if (mAsyncTaskHttpUtil != null && !mAsyncTaskHttpUtil.isCancelled()) {
            mAsyncTaskHttpUtil.cancel(true);
        }

        //
        mAdapter.setState(RankSongAdapter.LOADING);
        mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
        //
        mAsyncTaskHttpUtil = new AsyncTaskHttpUtil();
        mAsyncTaskHttpUtil.setSleepTime(sleepTime);
        mAsyncTaskHttpUtil.setAsyncTaskListener(new AsyncTaskHttpUtil.AsyncTaskListener() {
            @Override
            public HttpResult doInBackground() {

                return RankSongHttpUtil.rankSong(mHPApplication, mActivity.getApplicationContext(), mRankListResult.getRankId(), mRankListResult.getRankType(), mPage + "", mPageSize + "");

            }

            @Override
            public void onPostExecute(HttpResult httpResult) {
                logger.e("RankSongFragment ->排行 -> 11111");
                if (httpResult.getStatus() == HttpResult.STATUS_NONET) {
                    if (showView)
                        showNoNetView();
                    ToastUtil.showTextToast(mActivity.getApplicationContext(), httpResult.getErrorMsg());
                } else if (httpResult.getStatus() == HttpResult.STATUS_SUCCESS) {

                    //
                    Map<String, Object> returnResult = (Map<String, Object>) httpResult.getResult();

                    ArrayList<AudioInfo> datas = (ArrayList<AudioInfo>) returnResult.get("rows");

                    if (mPage == 1 && datas.size() == 0) { //没有数据
                        mAdapter.setState(RankSongAdapter.NODATA);
                    } else {
                        if (datas.size() < mPageSize) {
                            mAdapter.setState(RankSongAdapter.NOMOREDATA);
                        } else {
                            mAdapter.setState(RankSongAdapter.HASMOREDATA);
                            mPage++;
                        }
                        for (int i = 0; i < datas.size(); i++) {
                            Log.e("RankSongFragment ->", "排行 fragment -> " + GsonUtils.toJson(datas.get(i)));
                            mDatas.add(datas.get(i));
                        }
                    }
//                    Log.e("RankSongFragment ->", "排行 -> " + GsonUtils.toJson(mDatas));
                    mAdapter.notifyDataSetChanged();
                    if (showView)
                        showContentView();

                } else {
                    if (showView)
                        showContentView();
                    ToastUtil.showTextToast(mActivity.getApplicationContext(), httpResult.getErrorMsg());
                }


            }
        });

        mAsyncTaskHttpUtil.execute("");
    }


    @Override
    public void onDestroy() {
        if (mAsyncTaskHttpUtil != null)
            mAsyncTaskHttpUtil.cancel(true);
        mAudioBroadcastReceiver.unregisterReceiver(mActivity.getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected boolean isAddStatusBar() {
        return true;
    }
}
