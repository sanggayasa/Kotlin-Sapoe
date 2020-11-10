package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SparepartList(
    @SerializedName("created_at")
    val createdAt: Int? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("image_after")
    val imageAfter: String? = null,
    @SerializedName("image_before")
    val imageBefore: String? = null,
    @SerializedName("sparepart")
    val sparepart: Sparepart? = null,
    @SerializedName("updated_at")
    val updatedAt: Int? = null
): Parcelable