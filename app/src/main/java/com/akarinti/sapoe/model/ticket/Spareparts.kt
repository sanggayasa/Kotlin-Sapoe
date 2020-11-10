package com.akarinti.sapoe.model.ticket


import com.google.gson.annotations.SerializedName

data class Spareparts(
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category")
    val category: Category? = null,
    @SerializedName("city")
    val city: City? = null,
    @SerializedName("created_at")
    val createdAt: Int? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("price")
    val price: Int? = null,
    @SerializedName("unit")
    val unit: String? = null,
    @SerializedName("updated_at")
    val updatedAt: Int? = null
)