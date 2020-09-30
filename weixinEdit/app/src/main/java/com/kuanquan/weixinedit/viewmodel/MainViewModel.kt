package com.kuanquan.weixinedit.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mouble.baselibrary.base.BaseApiServiceViewModel
import com.mouble.baselibrary.util.LogUtil
import com.kuanquan.weixinedit.api.ApiService
import com.kuanquan.weixinedit.model.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel: BaseApiServiceViewModel<ApiService>(ApiService::class.java) {

    private val TAG = this.javaClass.simpleName

    private val _liveData = MutableLiveData("给定的值")

    val dataLiveData : MutableLiveData<List<DataResult.DataModel>> by lazy {
        MutableLiveData<List<DataResult.DataModel>>().also {
//            loadData()
        }
    }

    fun loadData(){
        presenterScope.launch {
            val time = System.currentTimeMillis()
            requestLiveData.value = ""
            LogUtil.e("线程 launch -> ",Thread.currentThread().name)
            try {
                val responseDatas = retrofitSuspendQuery()
                // 解析接口返回的数据
                dataLiveData.value = responseDatas
            } catch (e: Throwable) {
                LogUtil.e(TAG, "异常：$e")
            } finally {
                loadState.value = "加载完成"
                LogUtil.e(TAG, "耗时：${System.currentTimeMillis() - time}")
            }
        }
    }

    // Retrofit协程官方支持
    private suspend fun retrofitSuspendQuery(): List<DataResult.DataModel> {
        // withContext{}不会创建新的协程，在指定协程上运行挂起代码块，并挂起该协程直至代码块运行完成,最后一行为返回值，或者有 return
        return withContext(Dispatchers.IO) { // 切换到子线程执行网络请求
            try {
//                val androidResult = serviceApi?.getSuspendAndroid()
//                val iosResult = serviceApi?.getSuspendIOS()

                LogUtil.e("线程 withContext -> ",Thread.currentThread().name)
                val postData = serviceApi?.postData("恢复发货", 90)
                LogUtil.e(TAG, "简洁成功 = ${postData.toString()}")

                // enqueue 实现异步访问
//                serviceApi?.postData("恢复发货", 90)?.enqueue(object :Callback<BaseResponse>{
//                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
//                        LogUtil.e(TAG, "异常 = $t")
//                    }
//
//                    override fun onResponse(
//                        call: Call<BaseResponse>,
//                        response: Response<BaseResponse>
//                    ) {
//                        val baseResponse = response.body() as? BaseResponse
//                        LogUtil.e(TAG, "成功 = ${response.body().toString()}")
//                    }
//
//                })
                mutableListOf<DataResult.DataModel>().apply {
//                    iosResult?.results?.let { addAll(it) }
//                    androidResult?.results?.let { addAll(it) }
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}