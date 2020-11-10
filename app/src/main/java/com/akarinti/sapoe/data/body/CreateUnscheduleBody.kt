package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class CreateUnscheduleBody(
    @SerializedName("location_id")
    val locationId: String = "",
    @SerializedName("type")
    val type: String = ""
)