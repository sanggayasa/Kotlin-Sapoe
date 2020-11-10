package com.akarinti.sapoe.model.ticket
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SparepartItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("image_before") val imageBefore: String? = null,
    @SerializedName("image_after") val imageAfter: String? = null,
    @SerializedName("sparepart") val sparepart: SparepartData? = null
) : Parcelable