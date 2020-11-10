package com.akarinti.sapoe.model.news
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vendor(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("pic_name") val picName: String? = null,
    @SerializedName("pic_contact_number") val picContactNumber: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null
) : Parcelable