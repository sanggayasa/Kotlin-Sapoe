package com.akarinti.sapoe.data

import com.akarinti.sapoe.BuildConfig
import com.akarinti.sapoe.data.base.BaseNetwork
import com.akarinti.sapoe.data.interceptor.ContentTypeInterceptor
import okhttp3.OkHttpClient

abstract class AbstractNetwork<T>(): BaseNetwork<T>() {


    override fun getBaseUrl(): String = BuildConfig.BASE_URL

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(ContentTypeInterceptor())
        return super.okHttpClientBuilder(builder)
    }

}