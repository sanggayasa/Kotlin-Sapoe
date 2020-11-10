package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sparepart(
    @SerializedName("brand")
    val brand: String? = null,
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
): Parcelable