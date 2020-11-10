package com.akarinti.sapoe.data

import android.content.Context
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import com.akarinti.sapoe.data.interceptor.ContentTypeInterceptor
import okhttp3.OkHttpClient

abstract class AuthAbstractNetwork<T> (val headerManager: HeaderManager, private val authInterceptor: AuthInterceptor,
                                       private val context: Context): AbstractNetwork<T>(){


    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(ContentTypeInterceptor())
        builder.addInterceptor(authInterceptor)
        return super.okHttpClientBuilder(builder)
    }

    fun getNetworkService(): T {
        return networkService()
    }

}