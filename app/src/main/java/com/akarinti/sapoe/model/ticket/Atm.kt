package com.akarinti.sapoe.model.ticket


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Atm(
    @SerializedName("booth_type")
    val boothType: String = "",
    @SerializedName("contract_status")
    val contractStatus: String = "",
    @SerializedName("created_at")
    val createdAt: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("is_active")
    val isActive: Boolean = false,
    @SerializedName("is_trash_ready")
    val isTrashReady: Boolean = false,
    @SerializedName("machine_type")
    val machineType: String = "",
    @SerializedName("updated_at")
    val updatedAt: Int = 0,
    @SerializedName("wsid")
    val wsid: String = ""
):Parcelable