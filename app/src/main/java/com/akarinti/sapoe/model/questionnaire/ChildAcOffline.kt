package com.akarinti.sapoe.model.questionnaire
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildAcOffline(
    @SerializedName("id") val id: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("indoor_serial_number") val indoorSerialNumber: String? = null,
    @SerializedName("outdoor_serial_number") val outdoorSerialNumber: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("index") val index: Int? = null,
    @SerializedName("form_list") var formList: List<QuestionOffline>? = null,
    var isDone: Boolean? = false
) : Parcelable
