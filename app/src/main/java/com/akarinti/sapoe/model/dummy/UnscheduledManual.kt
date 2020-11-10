package com.akarinti.sapoe.model.dummy

import com.google.gson.annotations.SerializedName


data class UnscheduledManual(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("Store")
    val store: String = ""
)
