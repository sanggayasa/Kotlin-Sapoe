package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.Pagination
import com.akarinti.sapoe.model.ticket.Ticket
import com.google.gson.annotations.SerializedName


data class TicketListResponse(@SerializedName("data") val data: TicketListData? = null
) : BaseResponse() {
    data class TicketListData(
        @SerializedName("list") val list: List<Ticket>? = null,
        @SerializedName("pagination") val pagination: Pagination = Pagination()
    )
}