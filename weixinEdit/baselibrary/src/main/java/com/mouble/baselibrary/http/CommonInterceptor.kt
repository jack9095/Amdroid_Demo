package com.mouble.baselibrary.http

import android.text.TextUtils
//import com.alibaba.android.arouter.launcher.ARouter
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * 添加通用请求参数的拦截器
 */
class CommonInterceptor: Interceptor {
    private val TAG = CommonInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

//        val token = SharedPreferencesUtils.getSharePrefString("token")
//        LogUtil.e("Authorization = ", token)

        val request = chain.request().newBuilder()
//            .addHeader("Authorization", token)
            .build()

//        LogUtil.e("$TAG   request  URL Method", "request:$request")
//        LogUtil.e("$TAG   request  head", "request:" + request.headers().toString())
        val body = request.body
        if (body is FormBody) {
            val formBody = body as FormBody?
            for (i in 0 until formBody!!.size) {
//                LogUtil.e("$TAG   request  body", "key: " + formBody.name(i) + "   value: " + formBody.value(i))
            }
        }

        val response = chain.proceed(request)
        val mediaType = response.body?.contentType()

        var content = ""
        if (response.body != null) {
            content = response.body?.string().toString()
        }

//        LogUtil.e("$TAG   response", "response body:$content")
        if (!TextUtils.isEmpty(content)) {
            try {
//                val baseBean = GsonUtils().instance.fromJson<Any>(content, BaseResponse::class.java) as? BaseResponse<*>
//                if (baseBean.code == 999) { // 会话已失效
//                    ARouter.getInstance().build("/main/login").navigation()
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return response.newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .build()
    }
}