package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.TicketType
import com.google.gson.annotations.SerializedName

data class TicketTypeResponse(
    @SerializedName("data") val data: TicketTypeData? = null
) : BaseResponse() {
    data class TicketTypeData(
        @SerializedName("list") val list: List<TicketType>? = null
    )
}