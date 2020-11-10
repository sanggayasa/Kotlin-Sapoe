package com.akarinti.sapoe.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Unschedule(
    @SerializedName("agent")
    val agent: Agent? = null,
    @SerializedName("assignedBy")
    val assignedBy: AssignedBy? = null,
    @SerializedName("assigned_date")
    val assignedDate: String? = null,
    @SerializedName("created_at")
    val createdAt: Int? = null,
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("location")
    val location: Location? = null,
    @SerializedName("scheduleType")
    val scheduleType: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("updated_at")
    val updatedAt: Int? = null,
    @SerializedName("distance")
    val distance: Double? = null,
    var isEnable: Boolean = true
) : Parcelable {
    @Parcelize
    data class Agent(
        @SerializedName("created_at")
        val createdAt: Int? = null,
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("updated_at")
        val updatedAt: Int? = null
    ) : Parcelable

    @Parcelize
    data class AssignedBy(
        @SerializedName("created_at")
        val createdAt: Int? = null,
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("updated_at")
        val updatedAt: Int? = null
    ) : Parcelable

    @Parcelize
    data class Location(
        @SerializedName("address")
        val address: String? = null,
        @SerializedName("created_at")
        val createdAt: Int? = null,
        @SerializedName("district")
        val district: District? = null,
        @SerializedName("id")
        val id: String? = null,
        @SerializedName("lat")
        val lat: Double? = null,
        @SerializedName("long")
        val long: Double? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("province")
        val province: Province? = null,
        @SerializedName("regency")
        val regency: Regency? = null,
        @SerializedName("updated_at")
        val updatedAt: Int? = null,
        @SerializedName("village")
        val village: Village? = null,
        @SerializedName("zip_code")
        val zipCode: Int? = null
    ) : Parcelable {
        @Parcelize
        data class District(
            @SerializedName("id")
            val id: String? = null,
            @SerializedName("name")
            val name: String? = null
        ) : Parcelable
        @Parcelize
        data class Province(
            @SerializedName("id")
            val id: String? = null,
            @SerializedName("name")
            val name: String? = null
        ) : Parcelable
        @Parcelize
        data class Regency(
            @SerializedName("id")
            val id: String? = null,
            @SerializedName("name")
            val name: String? = null
        ) : Parcelable
        @Parcelize
        data class Village(
            @SerializedName("id")
            val id: String? = null,
            @SerializedName("name")
            val name: String? = null
        ) : Parcelable
    }
}