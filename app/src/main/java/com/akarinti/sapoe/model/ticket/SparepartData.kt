package com.akarinti.sapoe.model.ticket
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SparepartData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("price") val price: Int? = null,
    @SerializedName("unit") val unit: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
) : Parcelable