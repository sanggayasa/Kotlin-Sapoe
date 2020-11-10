package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.LoginBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("/user/login")
    fun login(@Body loginBody: LoginBody): Observable<Response<ResponseBody>>
    @POST("/user/logout")
    fun logout(): Observable<Response<ResponseBody>>
    @GET("/check/version")
    fun version(): Observable<Response<ResponseBody>>
}