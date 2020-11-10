package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Ac(
    @SerializedName("brand")
    val brand: String = "",
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("indoor_serial_number")
    val indoorSerialNumber: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("outdoor_serial_number")
    val outdoorSerialNumber: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0
):Parcelable