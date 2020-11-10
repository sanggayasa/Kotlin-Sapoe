package com.akarinti.sapoe.model.news
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("publish_at") val publishAt: Long? = null,
//    @SerializedName("vendor") val vendor: Vendor? = null,
//    @SerializedName("office") val office: Office? = null,
    @SerializedName("status") val status: String? = null
) : Parcelable