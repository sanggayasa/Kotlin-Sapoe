package com.burgreens.merchant.model


import com.google.gson.annotations.SerializedName

data class Temuan(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("jenis")
    val jenis: String = ""
)
