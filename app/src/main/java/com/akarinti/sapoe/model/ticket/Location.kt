package com.akarinti.sapoe.model.ticket

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @SerializedName("address") val address: String? = null,
    @SerializedName("created_at") val createdAt: Int = 0,
    @SerializedName("id") val id: String = "",
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("long") val long: Double? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("updated_at") val updatedAt: Int = 0,
    @SerializedName("regency_id") val regencyId: String = ""
) : Parcelable