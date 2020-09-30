package com.kuanquan.weixinedit.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mouble.baselibrary.base.BaseApiServiceViewModel
import com.mouble.baselibrary.util.LogUtil
import com.kuanquan.weixinedit.api.ApiService
import com.kuanquan.weixinedit.model.DataResult
import com.kuanquan.weixinedit.model.ProviderMultiEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel: BaseApiServiceViewModel<ApiService>(ApiService::class.java) {

    private var lists = mutableListOf<ProviderMultiEntity>()
    private var images = mutableListOf<String>()
    private val TAG = this.javaClass.simpleName

    val resultLiveData = MutableLiveData<MutableList<ProviderMultiEntity>>()

    fun setResult(){
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://pic.3h3.com/up/2014-3/20143314140858312456.gif")
        images.add("https://f12.baidu.com/it/u=3294379970,949120920&fm=72")
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://img.my.csdn.net/uploads/201701/06/1483664741_1378.jpg")
        images.add("http://img.my.csdn.net/uploads/201701/17/1484647897_1978.jpg")
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://pic.3h3.com/up/2014-3/20143314140858312456.gif")
        images.add("https://f12.baidu.com/it/u=3294379970,949120920&fm=72")

        for (i in 0 .. 5){
            val model = ProviderMultiEntity()
            model.type = 0
            model.createon = "昨天$i"
            model.name = "韦一笑$i"
            model.content = "茫茫的长白大山瀚的草原，浩始森林，大山脚下，原始森林环抱中散落着几十户人家的，" +
                    "一个小山村，茅草房，对面炕，烟筒立在屋后边。在村东头有一个独立的房子，那就是青年点，窗前有一道小溪流过。" +
                    "学子在这里吃饭，由这里出发每天随社员去地里干活。干的活要么上山伐，树，抬树，要么砍柳树毛子开荒种地。" +
                    "在山里，可听那吆呵声：“顺山倒了！”放树谨防回头棒！，树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。"

            lists.add(model)
        }

        for (i in 0 .. 5){
            val model = ProviderMultiEntity()
            model.type = 1
            model.createon = "前天$i"
            model.name = "寒冰绵掌$i"
            model.content = "树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。"
            model.images = images
            lists.add(model)
        }

        resultLiveData.value = lists
    }


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