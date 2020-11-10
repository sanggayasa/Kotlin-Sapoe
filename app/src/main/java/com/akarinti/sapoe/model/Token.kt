package com.akarinti.sapoe.model

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("access_token") val accessToken: String? = "",
    @SerializedName("token_type") val tokenType: String? = "",
    @SerializedName("expires_in") val expiresIn: Long? = 0,
    @SerializedName("refresh_token") val refreshToken: String? = ""
)