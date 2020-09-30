package com.kuanquan.weixinedit.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource

/**
 * 数据来源，从网络获取
 */
class ConcertDataSource: PositionalDataSource<Concert>() {

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Concert>) {
        Log.e("ConcertDataSource","家在数据")
        // 分页的加载数据，从第一页开始，每翻到新的一页都走这个方法
        callback.onResult(fetchItems(params.startPosition, params.loadSize));
    }

    // 初始化加载数据
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Concert>) {
        Log.e("ConcertDataSource","初始化数据")
        // 加载数据，每页20条，总共加载 200 条
        callback.onResult(fetchItems(0, 20), 0, 200);
    }

    private fun fetchItems(startPosition: Int, pageSize: Int): List<Concert> {
        val list: MutableList<Concert> = ArrayList()
        // 遍历的时候不包含最后一个
        for (i in startPosition until startPosition + pageSize) {
            val concert = Concert("author = $i","content = $i","title = $i")
            list.add(concert)
        }
        return list
    }
}

/**
 * 数据产生的工厂
 */
class ConcertFactory : DataSource.Factory<Int, Concert>() {
    private val mSourceLiveData = MutableLiveData<ConcertDataSource>()
    override fun create(): DataSource<Int, Concert> {
        val concertDataSource = ConcertDataSource()
        mSourceLiveData.postValue(concertDataSource)
        return concertDataSource
    }
}