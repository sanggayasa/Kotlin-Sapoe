package com.akarinti.sapoe.model
import com.google.gson.annotations.SerializedName


data class MenuCount(
    @SerializedName("scheduled") val scheduled: Int = 0,
    @SerializedName("unscheduledVisit") val unscheduledVisit: Int = 0,
    @SerializedName("ticket") val ticket: Int = 0
)