package com.kuanquan.doyincover.publisher.chains.solirateView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.kuanquan.doyincover.R;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.ScrollState;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.ArrayList;
import java.util.List;


/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/11/30
 * Description:
 */
public class SolirateNavigator extends FrameLayout implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
  private ScrollView mScrollView;
  private LinearLayout mTitleContainer;

  private CommonNavigatorAdapter mAdapter;
  private NavigatorHelper mNavigatorHelper;

  /**
   * 提供给外部的参数配置
   */
  /****************************************************/
  private float mScrollPivotX = 0.25f; // 滚动中心点 0.0f - 1.0f
  private boolean mReselectWhenLayout = true; // PositionData准备好时，是否重新选中当前页，为true可保证在极端情况下指示器状态正确

  public void setmScrollPivotX(float mScrollPivotX) {
    this.mScrollPivotX = mScrollPivotX;
  }

  public void setmReselectWhenLayout(boolean mReselectWhenLayout) {
    this.mReselectWhenLayout = mReselectWhenLayout;
  }

  /****************************************************/


  // 保存每个title的位置信息，为扩展indicator提供保障
  private List<PositionData> mPositionDataList = new ArrayList<PositionData>();

  private DataSetObserver mObserver = new DataSetObserver() {

    @Override
    public void onChanged() {
      mNavigatorHelper.setTotalCount(mAdapter.getCount());    // 如果使用helper，应始终保证helper中的totalCount为最新
      init();
    }

    @Override
    public void onInvalidated() {
      // 没什么用，暂不做处理
    }
  };

  public SolirateNavigator(Context context) {
    super(context);
    mNavigatorHelper = new NavigatorHelper();
    mNavigatorHelper.setNavigatorScrollListener(this);
  }

  @Override
  public void notifyDataSetChanged() {
    if (mAdapter != null) {
      mAdapter.notifyDataSetChanged();
    }
  }

  public CommonNavigatorAdapter getAdapter() {
    return mAdapter;
  }

  public void setAdapter(CommonNavigatorAdapter adapter) {
    if (mAdapter == adapter) {
      return;
    }
    if (mAdapter != null) {
      mAdapter.unregisterDataSetObserver(mObserver);
    }
    mAdapter = adapter;
    if (mAdapter != null) {
      mAdapter.registerDataSetObserver(mObserver);
      mNavigatorHelper.setTotalCount(mAdapter.getCount());
      if (mTitleContainer != null) {  // adapter改变时，应该重新init，但是第一次设置adapter不用，onAttachToMagicIndicator中有init
        mAdapter.notifyDataSetChanged();
      }
    } else {
      mNavigatorHelper.setTotalCount(0);
      init();
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  private void init() {
    removeAllViews();

    View root = LayoutInflater.from(getContext()).inflate(R.layout.view_solirate_indicator, this);

    mScrollView = root.findViewById(R.id.scroll_view);

    mScrollView.setOnTouchListener((v, event) -> true);

    mTitleContainer = root.findViewById(R.id.title_container);

    initTitlesAndIndicator();
  }

  /**
   * 初始化title和indicator
   */
  private void initTitlesAndIndicator() {
    for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
      IPagerTitleView v = mAdapter.getTitleView(getContext(), i);
      if (v instanceof View) {
        View view = (View) v;
        LinearLayout.LayoutParams lp;
        lp = new LinearLayout.LayoutParams((int) (36 * getResources().getDisplayMetrics().density), (int) (54 * getResources().getDisplayMetrics().density));
        mTitleContainer.addView(view, lp);
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (mAdapter != null) {
      preparePositionData();
      if (mReselectWhenLayout && mNavigatorHelper.getScrollState() == ScrollState.SCROLL_STATE_IDLE) {
        onPageSelected(mNavigatorHelper.getCurrentIndex());
        onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
      }
    }
  }

  /**
   * 获取title的位置信息，为打造不同的指示器、各种效果提供可能
   */
  private void preparePositionData() {
    mPositionDataList.clear();
    for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
      PositionData data = new PositionData();
      View v = mTitleContainer.getChildAt(i);
      if (v != null) {
        data.mLeft = v.getLeft();
        data.mTop = v.getTop();
        data.mRight = v.getRight();
        data.mBottom = v.getBottom();
        if (v instanceof IMeasurablePagerTitleView) {
          IMeasurablePagerTitleView view = (IMeasurablePagerTitleView) v;
          data.mContentLeft = view.getContentLeft();
          data.mContentTop = view.getContentTop();
          data.mContentRight = view.getContentRight();
          data.mContentBottom = view.getContentBottom();
        } else {
          data.mContentLeft = data.mLeft;
          data.mContentTop = data.mTop;
          data.mContentRight = data.mRight;
          data.mContentBottom = data.mBottom;
        }
      }
      mPositionDataList.add(data);
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    if (mAdapter != null) {

      mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
      // 手指跟随滚动
      if (mScrollView != null && mPositionDataList.size() > 0 && position >= 0 && position < mPositionDataList.size()) {
        int currentPosition = Math.min(mPositionDataList.size() - 1, position);
        int nextPosition = Math.min(mPositionDataList.size() - 1, position + 1);
        PositionData current = mPositionDataList.get(currentPosition);
        PositionData next = mPositionDataList.get(nextPosition);
        float scrollTo = current.verticalCenter() - mScrollView.getMeasuredHeight() * mScrollPivotX;
        float nextScrollTo = next.verticalCenter() - mScrollView.getMeasuredHeight() * mScrollPivotX;
        mScrollView.smoothScrollTo(0, (int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset));
      }
    }
  }

  @Override
  public void onPageSelected(int position) {
    if (mAdapter != null) {
      mNavigatorHelper.onPageSelected(position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    if (mAdapter != null) {
      mNavigatorHelper.onPageScrollStateChanged(state);
    }
  }

  @Override
  public void onAttachToMagicIndicator() {
    init(); // 将初始化延迟到这里
  }

  @Override
  public void onDetachFromMagicIndicator() {
  }

  @Override
  public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    if (mTitleContainer == null) {
      return;
    }
    View v = mTitleContainer.getChildAt(index);
    if (v instanceof IPagerTitleView) {
      ((IPagerTitleView) v).onEnter(index, totalCount, enterPercent, leftToRight);
    }
  }

  @Override
  public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    if (mTitleContainer == null) {
      return;
    }
    View v = mTitleContainer.getChildAt(index);
    if (v instanceof IPagerTitleView) {
      ((IPagerTitleView) v).onLeave(index, totalCount, leavePercent, leftToRight);
    }
  }

  @Override
  public void onSelected(int index, int totalCount) {
    if (this.mTitleContainer != null) {
      View v = this.mTitleContainer.getChildAt(index);
      if (v instanceof IPagerTitleView) {
        ((IPagerTitleView) v).onSelected(index, totalCount);
      }
//      if (this.mPositionDataList.size() > 0) {
//        int currentIndex = Math.min(this.mPositionDataList.size() - 1, index);
//        PositionData current = this.mPositionDataList.get(currentIndex);
//        float scrollTo = (float) current.verticalCenter() - (float) this.mScrollView.getMeasuredHeight() * this.mScrollPivotX;
//        this.mScrollView.smoothScrollTo(0, (int) scrollTo);
//      }
    }
  }

  @Override
  public void onDeselected(int index, int totalCount) {
    if (this.mTitleContainer != null) {
      View v = this.mTitleContainer.getChildAt(index);
      if (v instanceof IPagerTitleView) {
        ((IPagerTitleView) v).onDeselected(index, totalCount);
      }
    }
  }
}
