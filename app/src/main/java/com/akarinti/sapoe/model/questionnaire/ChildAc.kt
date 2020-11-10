package com.akarinti.sapoe.model.questionnaire
import com.google.gson.annotations.SerializedName

data class ChildAc(
    @SerializedName("id") val id: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("indoor_serial_number") val indoorSerialNumber: String? = null,
    @SerializedName("outdoor_serial_number") val outdoorSerialNumber: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    var isDone: Boolean? = false
)
