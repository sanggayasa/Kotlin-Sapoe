package com.akarinti.sapoe.model.ticket


import com.google.gson.annotations.SerializedName

data class CreateTicket(
    @SerializedName("category_type")
    val categoryType: String? = null,
    @SerializedName("category_value")
    val categoryValue: String? = null,
    @SerializedName("close_date")
    val closeDate: Any? = null,
    @SerializedName("created_at")
    val createdAt: Int? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("image_after")
    val imageAfter: Any? = null,
    @SerializedName("image_before")
    val imageBefore: Any? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("neonbox")
    val neonbox: Neonbox? = null,
    @SerializedName("ongoing_sla")
    val ongoingSla: Int? = null,
    @SerializedName("order_type")
    val orderType: String? = null,
    @SerializedName("sparepart_list")
    val sparepartList: List<SparepartList>? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("ticket_type")
    val ticketType: String? = null,
    @SerializedName("updated_at")
    val updatedAt: Int? = null
)