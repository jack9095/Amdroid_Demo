package com.kuanquan.datastoresimple.di

import android.content.Context
import com.kuanquan.datastoresimple.data.DataStoreRepository
import com.kuanquan.datastoresimple.data.IDataStoreRepository
import com.kuanquan.datastoresimple.data.IRepository
import com.kuanquan.datastoresimple.data.SharedPreferencesRepository

class RepositoryModule {

    fun provideSharedPreferencesRepository(ctx: Context): IRepository {
        return SharedPreferencesRepository(ctx)
    }

    fun provideDataStoreRepository(ctx: Context): IDataStoreRepository {
        return DataStoreRepository(ctx)
    }
}