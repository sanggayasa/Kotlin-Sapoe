package com.akarinti.sapoe.data.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskApi {

    @GET("/order-ticket-count")
    fun getOrderTicketCount() : Observable<Response<ResponseBody>>

    @GET("/broadcast/agent")
    fun getNewsList(@Query("page") page: Int, @Query("limit") limit: Int) : Observable<Response<ResponseBody>>

}