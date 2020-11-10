package com.akarinti.sapoe.data.body
import com.google.gson.annotations.SerializedName


data class ChangeStatusBody(
    @SerializedName("order_id") val orderId: String? = null,
    @SerializedName("status") val status: String? = null, // value : open|inprogress|unsent|completed
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("long") val long: Double? = null
)