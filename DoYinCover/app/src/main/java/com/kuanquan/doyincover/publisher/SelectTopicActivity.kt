package com.shuashuakan.android.modules.publisher

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.shuashuakan.android.R
import com.shuashuakan.android.data.api.model.ugc.TopicCategory
import com.shuashuakan.android.modules.account.AccountManager
import com.shuashuakan.android.ui.base.FishActivity
import com.shuashuakan.android.utils.ViewHelper
import com.shuashuakan.android.utils.bindView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import javax.inject.Inject

//选择话题页面
class SelectTopicActivity : FishActivity(), SelectTabApiView<List<TopicCategory>> {

  @Inject
  lateinit var accountManager: AccountManager
  @Inject
  lateinit var presenter: SelectTabPresenter

  private val toolbar by bindView<Toolbar>(R.id.toolbar)
  private val tabLayout by bindView<MagicIndicator>(R.id.home_indicator)
  private val viewPager by bindView<ViewPager>(R.id.view_pager)
  private val loadingBar by bindView<View>(R.id.loadingBar)
  private val errorView by bindView<View>(R.id.error_view)


  private lateinit var vpAdapter: SelectTopicViewPageAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_select_topic)
    presenter.attachView(this)
    initToolbar()
    ViewHelper.crossfade(loadingBar,errorView)
    presenter.request()
    initListener()
  }

  private fun initListener() {
    errorView.setOnClickListener {
      ViewHelper.crossfade(loadingBar,errorView)
      presenter.request()
    }
  }

  private fun initTabBar(categorys: List<TopicCategory>) {
    vpAdapter = SelectTopicViewPageAdapter(supportFragmentManager, categorys)
    viewPager.adapter = vpAdapter

    val commonNavigator = CommonNavigator(this)
    commonNavigator.scrollPivotX = 0.65f

    commonNavigator.adapter = object : CommonNavigatorAdapter() {
      override fun getCount(): Int {
        return categorys.size
      }

      override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        val simplePagerTitleView = com.shuashuakan.android.modules.widget.ScaleTransitionPagerTitleView(context)
        simplePagerTitleView.text = categorys[index].name
        simplePagerTitleView.textSize = 18f
        simplePagerTitleView.normalColor = getColor1(R.color.color_normal_838791)
        simplePagerTitleView.selectedColor = getColor1(R.color.white)
        simplePagerTitleView.setOnClickListener { viewPager.currentItem = index }
        return simplePagerTitleView
      }

      override fun getIndicator(context: Context): IPagerIndicator {
        val indicator = com.shuashuakan.android.modules.widget.GradualLinePagerIndicator(context)
        indicator.mode = LinePagerIndicator.MODE_EXACTLY
        indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
        indicator.lineWidth = UIUtil.dip2px(context, 28.0).toFloat()
        indicator.yOffset = UIUtil.dip2px(context, 7.5).toFloat()
        indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
        indicator.startInterpolator = AccelerateInterpolator()
        indicator.endInterpolator = DecelerateInterpolator(2.0f)
        indicator.setColors(getColor1(R.color.color_ffef30), getColor1(R.color.color_normal_59ff5a))
        return indicator
      }
    }
    tabLayout.navigator = commonNavigator
    ViewPagerHelper.bind(tabLayout, viewPager)
  }
  private fun initToolbar() {
    toolbar.background = ColorDrawable(resources.getColor(R.color.colorPrimaryDark))
    toolbar.setNavigationIcon(R.drawable.ic_closed)
    toolbar.setNavigationOnClickListener {
      onBackPressed()
    }
  }
  override fun showData(m: List<TopicCategory>) {
    ViewHelper.crossfade(tabLayout,loadingBar,errorView)
    initTabBar(m)
  }

  override fun showMessage(message: String) {
    ViewHelper.crossfade(errorView,loadingBar)
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.detachView(false)
  }
}
