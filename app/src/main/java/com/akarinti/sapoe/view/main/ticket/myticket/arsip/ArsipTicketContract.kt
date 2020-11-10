package com.akarinti.sapoe.view.main.ticket.myticket.arsip

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.model.ticket.Ticket


interface ArsipTicketContract {
    interface View: ErrorView {
        fun onArsipTicketSet(data: List<Ticket>?)
    }

    interface Presenter{
        fun getArsipTicket(lat: Double, long: Double)
    }
}