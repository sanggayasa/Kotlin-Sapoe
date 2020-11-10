package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class ChanegStatusTicketBody(
    @SerializedName("status")
    val status: String = ""
)