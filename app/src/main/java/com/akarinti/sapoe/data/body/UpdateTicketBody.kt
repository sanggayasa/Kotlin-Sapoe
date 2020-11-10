package com.akarinti.sapoe.data.body


import com.google.gson.annotations.SerializedName

data class UpdateTicketBody(
    @SerializedName("image_before") val imageBefore: String? = null,
    @SerializedName("image_after") val imageAfter: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("close_date") val closeDate: Long? = null,
    @SerializedName("agent_id") val agentID: String? = null
)