package com.akarinti.sapoe.view.main.visit.visit_parent.child

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.ticket.Ticket

interface VisitChildContract {
    interface View: ErrorView {
        fun onTicketSet(data: List<Ticket>?)
    }

    interface Presenter{
        fun getTicket(lat: Double, long: Double, orderId:String, orderType:String)
    }
}