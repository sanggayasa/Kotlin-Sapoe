package com.akarinti.sapoe.model.questionnaire

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    @SerializedName("id") val id: String? = null,
    @SerializedName("order_id") val orderId: String? = null,
    @SerializedName("order_type") val orderType: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("item_id") val itemId: Int? = null,
    @SerializedName("answer") val answer: String? = null
) : Parcelable