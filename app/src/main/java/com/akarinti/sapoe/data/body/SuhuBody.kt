package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class SuhuBody(
    @SerializedName("category")
    val category: String = "",
    @SerializedName("order_id")
    val orderId: String = "",
    @SerializedName("order_type")
    val orderType: String = "",
    @SerializedName("picture")
    val picture: String = "",
    @SerializedName("temperature")
    val temperature: String = ""
)