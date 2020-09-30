package com.kuanquan.weixinedit.api

import com.mouble.baselibrary.http.BaseResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

//    @GET("data/Android/2/1")
//    suspend fun getSuspendAndroid(): BaseResponse<List<DataResult.DataModel>>
//
//    @GET("data/iOS/2/1")
//    suspend fun getSuspendIOS(): BaseResponse<List<DataResult.DataModel>>

    @FormUrlEncoded
    @POST("add2gank")
    suspend fun postData(@Field("desc") desc: String, @Field("type") type: Int): BaseResponse
//    fun postData(@Field("desc") desc: String, @Field("type") type: Int): Call<BaseResponse>
//    fun getIndexHotData(@Field("desc") desc: String, @Field("type") type: Int): Call<请求返回的结果bean对象>
}