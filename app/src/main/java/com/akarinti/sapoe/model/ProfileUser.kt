package com.akarinti.sapoe.model

import com.google.gson.annotations.SerializedName

class ProfileUser(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("refresh_token") var refreshToken: String? = null,
    @SerializedName("access_token") var accessToken: String? = null,
    @SerializedName("token_type") var tokenType: String? = null,
    @SerializedName("expires_in") var expiresIn: Long = 0
){
    fun toAccessToken(): Token {
        return Token(accessToken, tokenType, expiresIn, refreshToken)
    }
}