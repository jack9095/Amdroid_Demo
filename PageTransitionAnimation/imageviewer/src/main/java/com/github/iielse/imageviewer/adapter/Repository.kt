package com.github.iielse.imageviewer.adapter

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.github.iielse.imageviewer.core.Components

class Repository {
    private val dataProvider by lazy { Components.requireDataProvider() }

    fun dataSourceFactory(): DataSource.Factory<Long, Item> {
        return object : DataSource.Factory<Long, Item>() {
            override fun create(): DataSource<Long, Item> {
                return dataSource()
            }
        }
    }

    private fun dataSource() = object : ItemKeyedDataSource<Long, Item>() {
        override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Item>) {
            val result = dataProvider.loadInitial()
            callback.onResult(result.map { Item.from(it) }, 0, result.size)
        }

        override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Item>) {
            dataProvider.loadAfter(params.key) {
                callback.onResult(it.map { Item.from(it) })
            }
        }

        override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Item>) {
            dataProvider.loadBefore(params.key) {
                callback.onResult(it.map { Item.from(it) })
            }
        }

        override fun getKey(item: Item): Long {
            return item.id
        }
    }
}

