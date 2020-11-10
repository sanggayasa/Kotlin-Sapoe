package com.akarinti.sapoe.data.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VersionApi {

    @GET("/check/version?device_type=android")
    fun checkVersion(
        @Query("app_version") version: String,
        @Query("build_version") code: Int
    ) : Observable<Response<ResponseBody>>

}