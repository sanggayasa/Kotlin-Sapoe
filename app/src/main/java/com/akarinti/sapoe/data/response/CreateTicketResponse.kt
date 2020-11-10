package com.akarinti.sapoe.data.response

import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.model.ticket.CreateTicket
import com.google.gson.annotations.SerializedName


class CreateTicketResponse (@SerializedName("data") val data: createTicketData? = null): BaseResponse() {
    class createTicketData(@SerializedName("detail") val detail: CreateTicket?=null)
}