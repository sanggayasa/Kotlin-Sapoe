package com.akarinti.sapoe.data.body

import com.google.gson.annotations.SerializedName

data class TokenInquiryBody(
    @SerializedName("grant_type") var grantType: String? = "",
    @SerializedName("username") var username: String? = "",
    @SerializedName("password") var password: String? = ""
)