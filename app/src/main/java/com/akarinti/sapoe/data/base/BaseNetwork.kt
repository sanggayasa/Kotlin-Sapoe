package com.akarinti.sapoe.data.base

import com.akarinti.sapoe.BuildConfig
import com.google.gson.GsonBuilder
import io.reactivex.annotations.Nullable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetwork<T>{

    @Nullable
    var service: T? = null

    fun loggingInterceptor(): Interceptor {
        var httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }


    open fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder =
        builder.addInterceptor(loggingInterceptor())

    fun provideClient(): OkHttpClient = okHttpClientBuilder(OkHttpClient.Builder())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun gsonHandler(builder: GsonBuilder): GsonBuilder {
        return builder
    }

    fun provideRetrofit(): Retrofit {
        val gson = this.gsonHandler(GsonBuilder().setPrettyPrinting()).setDateFormat("yyyy-MM-dd\'T\'hh:mm:ssZ").create()
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(provideClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    open fun networkService(): T {
        if (service == null) {
            val retrofit = provideRetrofit()
            RetrofitHelper.init(retrofit)
            service = retrofit.create(getApi())
        }
        return service!!
    }


    abstract fun getApi(): Class<T>

    abstract fun getBaseUrl(): String

}