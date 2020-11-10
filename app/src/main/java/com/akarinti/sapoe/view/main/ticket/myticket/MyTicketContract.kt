package com.akarinti.sapoe.view.main.ticket.myticket

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.TicketListResponse

interface MyTicketContract {
    interface View: ErrorView {
        fun onMyTicketSet(data: TicketListResponse)
    }

    interface Presenter{
        fun getMyTicket(lat: Double, long: Double, page: Int = 1, limit: Int = 10)
    }
}