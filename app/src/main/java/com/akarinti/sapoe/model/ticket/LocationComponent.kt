package com.akarinti.sapoe.model.ticket


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class LocationComponent(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_active")
    val isActive: Int = 0,
    @SerializedName("location_id")
    val locationId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = ""
) : Parcelable