package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.Ticket
import com.google.gson.annotations.SerializedName

data class TicketDetailResponse(
    @SerializedName("data") val data: TicketData? = null
) : BaseResponse() {
    data class TicketData(@SerializedName("detail") val detail: Ticket? = null)
}