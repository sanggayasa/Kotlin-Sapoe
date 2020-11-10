package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class TicketCreateBody(
    @SerializedName("location_id") val locationId: String = "",
    @SerializedName("sla_id") val slaId: String = "",
    @SerializedName("category_type") val categoryType: String = "",
    @SerializedName("category_id") val categoryId: String = "",
    @SerializedName("ticket_type") val ticketType: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("order_type") val orderType: String? = "",
    @SerializedName("order_id") val orderId: String? = ""
)