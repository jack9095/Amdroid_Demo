package com.mouble.baselibrary.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpHelper {

    @Volatile
    private var mHttpHelper: HttpHelper? = null

    private var mOkHttpClient: OkHttpClient? = null  // okhttp的委托
    private var mRetrofit: Retrofit? = null
    private var mBuilder: OkHttpClient.Builder? = null
    private var BASE_URL: String? = null

    fun getInstance(): HttpHelper? {
        if (mHttpHelper == null) {
            synchronized(HttpHelper::class.java) {
                if (mHttpHelper == null) {
                    mHttpHelper = HttpHelper
                }
            }
        }
        return mHttpHelper
    }

    // TODO 初始化网络请求的方法 建议在 Application 中执行
    fun init(baseUrl: String) {
        Builder()
            .initOkHttp()
            .createRetrofit(baseUrl)
            .build()
    }

    class Builder {
        internal var mOkHttpClient: OkHttpClient? = null

        internal var mBuilder: OkHttpClient.Builder? = null

        internal var mRetrofit: Retrofit? = null

        /**
         * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志
         *
         * @return Builder
         */
        fun initOkHttp(): Builder {
            val interceptor = HttpLoggingInterceptor(HttpLogger()) // 初始化日志拦截器
            val commonInterceptor = CommonInterceptor()
            /**
             * BASEIC:请求/响应行
             * HEADER:请求/响应行 + 头
             * BODY:请求/响应航 + 头 + 体
             */
            interceptor.level = HttpLoggingInterceptor.Level.BODY  // 设置日志的打印级别
            if (mBuilder == null) {
                synchronized(HttpHelper::class.java) {
                    if (mBuilder == null) {
                        mBuilder = OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(commonInterceptor)
                            .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                    }
                }
            }

            return this
        }

        /**
         * 添加拦截器
         *
         * @param mInterceptor Interceptor
         * @return Builder
         */
        fun addInterceptor(mInterceptor: Interceptor): Builder {
            checkNotNull(mInterceptor)
            this.mBuilder?.addNetworkInterceptor(mInterceptor)
            return this
        }

        /**
         * create retrofit
         *
         * @param baseUrl baseUrl
         * @return Builder
         */
        fun createRetrofit(baseUrl: String): Builder {
            val builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(baseUrl)
            BASE_URL = baseUrl
            this.mOkHttpClient = mBuilder!!.build()
            this.mRetrofit = builder.client(mOkHttpClient!!)
                .build()
            return this
        }

        fun build() {
            build(this)
        }

    }

    private fun build(builder: Builder) {
        mBuilder = builder.mBuilder
        mOkHttpClient = builder.mOkHttpClient
        mRetrofit = builder.mRetrofit
    }

    // 获取自定义 ApiService 接口的实体
    fun <T> create(clz: Class<T>): T? {
        return mRetrofit?.create(clz)
    }
}