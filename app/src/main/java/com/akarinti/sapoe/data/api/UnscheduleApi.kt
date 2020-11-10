package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.ChangeStatusBody
import com.akarinti.sapoe.data.body.CreateUnscheduleBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UnscheduleApi {

    @GET("/unscheduled-visit/agent")
    fun getListUnschedule(
        @Query("type") type: String,
        @Query("scheduleType") scheduleType: String,
        @Query("status") status: String
    ) : Observable<Response<ResponseBody>>
    @GET("/unscheduled-visit/agent")
    fun getListUnscheduleDistance(
        @Query("type") type: String,
        @Query("scheduleType") scheduleType: String,
        @Query("status") status: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<Response<ResponseBody>>

    @POST("/unscheduled-visit/agent")
    fun createUnschedule(@Body createUnscheduleBody: CreateUnscheduleBody,
                         @Query("latitude") latitude: Double,
                         @Query("longitude") longitude: Double): Observable<Response<ResponseBody>>

    @PUT("/unscheduledorder/changestatus")
    fun changeUnscheduleStatusValidated(@Body body: ChangeStatusBody,
                                        @Query("latitude") latitude: Double,
                                        @Query("longitude") longitude: Double): Observable<Response<ResponseBody>>
}