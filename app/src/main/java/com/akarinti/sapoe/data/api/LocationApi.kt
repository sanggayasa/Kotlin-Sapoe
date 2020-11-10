package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.LocationRequestBody
import com.akarinti.sapoe.data.body.LocationTrackingBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LocationApi {

    @GET("/location/agent")
    fun getLocationList(
        @Query("q") q: String? = "",
        @Query("sort") sort: String? = "name:asc",
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<Response<ResponseBody>>

    @POST("/location-request")
    fun sendLocationRequest(@Body locationRequestBody: LocationRequestBody)
            : Observable<Response<ResponseBody>>

    @POST("/location-tracking")
    fun sendLocationTracking(@Body locationTrackingBody: LocationTrackingBody)
            : Observable<Response<ResponseBody>>
}