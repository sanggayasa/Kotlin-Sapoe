package com.akarinti.sapoe.model

import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("code") val code: Int? = 0,
    @SerializedName("status") val status: Boolean? = false,
    @SerializedName("message") val message: String? = ""
)