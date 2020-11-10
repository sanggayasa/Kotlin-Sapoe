package com.akarinti.sapoe.view.main.ticket.ticketall

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.TicketListResponse

interface TicketListContract {
    interface View: ErrorView {
        fun onTicketSet(data: TicketListResponse)
    }

    interface Presenter{
        fun getTicket(q:String,lat: Double, long: Double, orderId:String, page: Int = 1, limit: Int = 10)
    }
}