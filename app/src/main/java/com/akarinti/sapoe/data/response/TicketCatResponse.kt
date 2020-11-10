package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.LocationComponent
import com.google.gson.annotations.SerializedName

data class TicketCatResponse(@SerializedName("data") val data: TicketCatData? = null
) : BaseResponse() {
    data class TicketCatData(
        @SerializedName("list") val list: List<LocationComponent>? = null
    )
}