package com.shuashuakan.android.modules.publisher

import android.content.Context
import com.shuashuakan.android.commons.di.AppContext
import com.shuashuakan.android.data.api.model.ugc.TopicCategory
import com.shuashuakan.android.data.api.services.ApiService
import com.shuashuakan.android.exts.mvp.ApiPresenter
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Author:  lijie
 * Date:   2018/12/8
 * Email:  2607401801@qq.com
 */
class SelectTabPresenter @Inject constructor(
    @AppContext val context: Context, val apiService: ApiService
): ApiPresenter<SelectTabApiView<List<TopicCategory>>, List<TopicCategory>>(context){

  override fun onSuccess(data: List<TopicCategory>) {
    view.showData(data)
  }

  override fun getObservable(): Observable<List<TopicCategory>> {
    return apiService.getChannelCategory()
  }

  fun request() {
    subscribe()
  }
}
interface SelectTabApiView<in DATA> : com.shuashuakan.android.exts.mvp.ApiView {
  fun showData(m: DATA)
}