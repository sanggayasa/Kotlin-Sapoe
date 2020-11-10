package com.akarinti.sapoe.data.entity

import com.akarinti.sapoe.BuildConfig
import com.akarinti.sapoe.data.AbstractNetwork
import com.akarinti.sapoe.data.api.TokenApi
import com.akarinti.sapoe.data.body.TokenInquiryBody
import com.akarinti.sapoe.data.response.TokenResponse
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TokenEntity @Inject constructor() : AbstractNetwork<TokenApi>() {

    override fun getApi(): Class<TokenApi> = TokenApi::class.java

    fun getOauthToken() = networkService().getOauthToken(TokenInquiryBody(BuildConfig.GT, BuildConfig.UN, BuildConfig.PD))

    fun callRequestNewToken(): Call<TokenResponse> = networkService().getNewOauthToken(
        TokenInquiryBody(
            BuildConfig.GT,
            BuildConfig.UN,
            BuildConfig.PD
        )
    )

    fun getWhitelist() = networkService().getWhitelist()
}