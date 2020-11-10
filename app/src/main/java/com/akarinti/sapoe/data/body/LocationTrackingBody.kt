package com.akarinti.sapoe.data.body
import com.google.gson.annotations.SerializedName


data class LocationTrackingBody(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("long") val long: Double? = null,
    @SerializedName("device_name") val deviceName: String? = null,
    @SerializedName("imei") val imei: String? = null
)