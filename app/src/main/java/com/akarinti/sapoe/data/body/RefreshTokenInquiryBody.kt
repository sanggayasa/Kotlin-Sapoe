package com.akarinti.sapoe.data.body

import com.google.gson.annotations.SerializedName

data class RefreshTokenInquiryBody(
    @SerializedName("grant_type") var grantType: String? = "",
    @SerializedName("username") var username: String? = "",
    @SerializedName("password") var password: String? = "",
    @SerializedName("refresh_token") val refresh_token: String? = ""
)