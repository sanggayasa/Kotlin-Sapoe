package com.burgreens.merchant.model


import com.google.gson.annotations.SerializedName

data class Visit(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("Store")
    val store: String = ""
)
