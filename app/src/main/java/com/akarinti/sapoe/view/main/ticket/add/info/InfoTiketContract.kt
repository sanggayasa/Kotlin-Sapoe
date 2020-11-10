package com.akarinti.sapoe.view.main.ticket.add.info

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.UpdateTicketBody
import com.akarinti.sapoe.model.answer.ImageResult
import com.akarinti.sapoe.model.ticket.Ticket

interface InfoTiketContract {
    interface View: ErrorView {
        fun onTicketDetail(data: Ticket?, agentID: String)
        fun onCloseTicket()
    }

    interface Presenter{
        fun getTicketDetail(ticketID: String)
        fun takeTicket(ticketID: String, lat: Double, long: Double)
        fun getRepository(): List<ImageResult>
        fun closeTicket(ticketID: String, input: UpdateTicketBody, lat: Double, long: Double)
        fun deleteSparepart(sparepartID: String, ticketID: String)
    }
}