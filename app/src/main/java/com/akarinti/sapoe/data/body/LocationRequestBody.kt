package com.akarinti.sapoe.data.body
import com.google.gson.annotations.SerializedName

data class LocationRequestBody(
    @SerializedName("location_id") val locationId: String? = null,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("long") val long: Double? = null
)