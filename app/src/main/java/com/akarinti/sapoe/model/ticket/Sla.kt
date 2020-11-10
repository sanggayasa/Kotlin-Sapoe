package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sla(
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("day")
    val day: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0
) : Parcelable