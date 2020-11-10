package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.child

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.ticket.Ticket


interface UnscheduleChildContract {
    interface View: ErrorView {
        fun onTicketSet(data: List<Ticket>?)
    }

    interface Presenter{
        fun getTicket(lat: Double, long: Double, orderId:String, orderType:String)
    }
}