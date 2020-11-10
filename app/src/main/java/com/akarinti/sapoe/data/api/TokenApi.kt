package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.TokenInquiryBody
import com.akarinti.sapoe.data.response.TokenResponse
import com.akarinti.sapoe.data.response.WhitelistAppResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TokenApi{

    @POST("oauth/token")
    fun getOauthToken(@Body tokenBody: TokenInquiryBody) : Observable<Response<ResponseBody>>

    @POST("oauth/token")
    fun getNewOauthToken(@Body tokenBody: TokenInquiryBody) : Call<TokenResponse>

    @GET("whitelist-fake-gps")
    fun getWhitelist(): Observable<Response<WhitelistAppResponse>>
}