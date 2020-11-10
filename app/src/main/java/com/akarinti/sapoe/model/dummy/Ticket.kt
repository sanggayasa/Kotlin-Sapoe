package com.akarinti.sapoe.model.dummy

import com.google.gson.annotations.SerializedName


data class Ticket(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("store")
    val store: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("jenis")
    val jenis: String = ""
)
