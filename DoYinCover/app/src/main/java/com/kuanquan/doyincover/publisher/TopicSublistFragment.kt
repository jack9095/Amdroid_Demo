package com.shuashuakan.android.modules.publisher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.shuashuakan.android.R
import com.shuashuakan.android.config.AppConfig
import com.shuashuakan.android.data.api.model.ugc.TopicNameListModel
import com.shuashuakan.android.data.api.services.ApiService
import com.shuashuakan.android.modules.account.AccountManager
import com.shuashuakan.android.ui.base.FishFragment
import com.shuashuakan.android.modules.widget.loadmoreview.SskLoadMoreView
import com.shuashuakan.android.exts.applySchedulers
import com.shuashuakan.android.utils.bindView
import com.shuashuakan.android.exts.subscribeApi
import javax.inject.Inject

/**
 * Author:  lijie
 * Date:   2018/12/8
 * Email:  2607401801@qq.com
 */
class TopicSublistFragment : FishFragment() {

    @Inject
    lateinit var accountManager: AccountManager
    @Inject
    lateinit var appConfig: AppConfig
    @Inject
    lateinit var apiService: ApiService
    val rec by bindView<RecyclerView>(R.id.rec)
    val errorView by bindView<TextView>(R.id.error_view)

    lateinit var adapter: BaseQuickAdapter<TopicNameListModel, BaseViewHolder>
    private var categoryId: Long = 0
    private var categoryName: String = ""
    private var page = 0
    private var isFirst = true

    companion object {
        private const val EXTRA_CATEGORY_ID = "extra_category_id"
        private const val EXTRA_CATEGORY_NAME = "extra_category_name"
        fun create(id: Long, name: String): TopicSublistFragment {
            val fragment = TopicSublistFragment()
            val bundle = Bundle()
            bundle.putLong(EXTRA_CATEGORY_ID, id)
            bundle.putString(EXTRA_CATEGORY_NAME, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_topic_sub_list, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        categoryId = arguments?.getLong(EXTRA_CATEGORY_ID, 0) ?: return
        categoryName = arguments?.getString(EXTRA_CATEGORY_NAME, "") ?: return
        initView()
        getData()
    }

    @SuppressLint("CheckResult")
    private fun getData() {
        errorView.visibility = View.GONE
        apiService.geTopicList(categoryId, page).applySchedulers().subscribeApi(onNext = {
            if (isFirst) {
                adapter.setNewData(it)
                isFirst = false
            } else {
                if (it.size < 10) {
                    adapter.loadMoreEnd(true)
                } else {
                    adapter.loadMoreComplete()
                }
                adapter.addData(it)
            }

        }, onApiError = {
            errorView.visibility = View.VISIBLE
        })
    }

    private fun initView() {
        rec.layoutManager = LinearLayoutManager(requireActivity())
        adapter = object : BaseQuickAdapter<TopicNameListModel, BaseViewHolder>(R.layout.item_topic_list) {
            override fun convert(helper: BaseViewHolder, item: TopicNameListModel) {
                helper.setText(R.id.topic_item_title, item.name)
                        .setText(R.id.topic_item_subscribe_num, String.format(getString(R.string.string_subscription_format), item.subscribedCount))
                Glide.with(requireActivity()).load(item.channelIcon).into(helper.getView<ImageView>(R.id.topic_item_icon))
            }
        }
        rec.adapter = adapter
        adapter.setOnLoadMoreListener {
            page++
            getData()
        }
        adapter.setLoadMoreView(SskLoadMoreView(SskLoadMoreView.TOPIC))
        adapter.setOnItemClickListener { adapter, view, position ->
            val topic = adapter.data[position] as TopicNameListModel
            requireActivity().setResult(Activity.RESULT_OK, Intent().putExtra("topic", topic))
            requireActivity().finish()
        }

    }
}