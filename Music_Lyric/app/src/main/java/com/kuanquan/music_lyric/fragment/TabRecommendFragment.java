package com.kuanquan.music_lyric.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuanquan.music_lyric.R;
import com.kuanquan.music_lyric.adapter.RecommendAdapter;
import com.kuanquan.music_lyric.libs.utils.ToastUtil;
import com.kuanquan.music_lyric.net.api.RankListHttpUtil;
import com.kuanquan.music_lyric.net.entity.RankListResult;
import com.kuanquan.music_lyric.net.model.HttpResult;
import com.kuanquan.music_lyric.utils.AsyncTaskHttpUtil;
import com.kuanquan.music_lyric.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @Description: tab推荐界面  排行
 */
public class TabRecommendFragment extends BaseFragment {

    /**
     * 列表视图
     */
    private RecyclerView mRecyclerView;

    /**
     * 是否已加载数据
     */
    private boolean isLoadData = false;
    //
    private RecommendAdapter mAdapter;
    private ArrayList<RankListResult> mDatas;
    /**
     * http请求
     */
    private AsyncTaskHttpUtil mAsyncTaskHttpUtil;

    public TabRecommendFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected int setContentViewId() {
        return R.layout.layout_fragment_recommend;
    }

    @Override
    protected void initViews(Bundle savedInstanceState, View mainView) {

        //
        mRecyclerView = mainView.findViewById(R.id.recyclerView);
        //初始化内容视图
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));

        //
        mDatas = new ArrayList<RankListResult>();
        mAdapter = new RecommendAdapter(mHPApplication, mActivity.getApplicationContext(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //
        showLoadingView();

        setRefreshListener(new RefreshListener() {
            @Override
            public void refresh() {
                showLoadingView();

                loadDataUtil(300);
            }
        });
    }

    @Override
    protected void loadData(boolean isRestoreInstance) {
        if (isLoadData) {
            if (isRestoreInstance) {
                mDatas.clear();
            }
            loadDataUtil(300);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && !isLoadData) {
            isLoadData = true;
            loadDataUtil(0);
        }
    }

    /**
     * 加载数据
     */
    private void loadDataUtil(final int sleepTime) {
        mAsyncTaskHttpUtil = new AsyncTaskHttpUtil();
        mAsyncTaskHttpUtil.setSleepTime(sleepTime);
        mAsyncTaskHttpUtil.setAsyncTaskListener(new AsyncTaskHttpUtil.AsyncTaskListener() {
            @Override
            public HttpResult doInBackground() {
                return RankListHttpUtil.rankList(mHPApplication, mActivity.getApplicationContext());
            }

            @Override
            public void onPostExecute(HttpResult httpResult) {
                if (httpResult.getStatus() == HttpResult.STATUS_NONET) {
                    showNoNetView();
                } else if (httpResult.getStatus() == HttpResult.STATUS_SUCCESS) {

                    //
                    Map<String, Object> returnResult = (Map<String, Object>) httpResult.getResult();

                    ArrayList<RankListResult> datas = (ArrayList<RankListResult>) returnResult.get("rows");
                    if (datas.size() == 0) {
                        mAdapter.setState(RecommendAdapter.NODATA);
                    } else {
                        for (int i = datas.size() - 1; i >= 0; i--) {
                            mDatas.add(0, datas.get(i));
                            Log.e("RankSongFragment ->", "排行 fragment -> " + GsonUtils.toJson(datas.get(i)));
                        }
                        mAdapter.setState(RecommendAdapter.NOMOREDATA);
                    }
                    Log.e("TabRecommendFragment ->", "排行 -> " + GsonUtils.toJson(mDatas));
                    mAdapter.notifyDataSetChanged();

                    showContentView();

                } else {
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
        super.onDestroy();
    }


    @Override
    protected int setTitleViewId() {
        return 0;
    }

    @Override
    protected boolean isAddStatusBar() {
        return false;
    }
}