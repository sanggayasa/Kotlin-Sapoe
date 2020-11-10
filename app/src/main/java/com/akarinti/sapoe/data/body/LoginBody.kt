package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("device_id") val deviceId: String? = "",
    @SerializedName("password") val password: String? = "",
    @SerializedName("registration_id") val registrationId: String? = "",
    @SerializedName("username") val username: String? = "",
    @SerializedName("imei") val imei: String? = null
)