package com.akarinti.sapoe.model


import com.google.gson.annotations.SerializedName

data class Suhu(
    @SerializedName("category")
    val category: String = "",
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("order_id")
    val orderId: String = "",
    @SerializedName("order_type")
    val orderType: String = "",
    @SerializedName("picture")
    val picture: String = "",
    @SerializedName("temperature")
    val temperature: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0
)