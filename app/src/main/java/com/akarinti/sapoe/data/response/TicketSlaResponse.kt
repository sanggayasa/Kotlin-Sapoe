package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.TicketSla
import com.google.gson.annotations.SerializedName

data class TicketSlaResponse(
    @SerializedName("data") val data: TicketSlaData? = null
) : BaseResponse() {
    data class TicketSlaData(
        @SerializedName("list") val list: List<TicketSla>? = null
    )
}