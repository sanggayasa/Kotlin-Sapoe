package com.akarinti.sapoe.view.main.ticket.add

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.body.TicketCreateBody
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.model.ticket.LocationComponent
import com.akarinti.sapoe.model.ticket.TicketSla
import com.akarinti.sapoe.model.ticket.TicketType


interface TiketContract {
    interface View: ErrorView {
        fun onLocationSet(data: LocationListResponse.LocationListData)
        fun onTicketType(list: List<TicketType>?)
        fun onTicketSla(list: List<TicketSla>?)
        fun onCatTicket(list: List<LocationComponent>?)
        fun gotoNextPage()
    }

    interface Presenter{
        fun getLocationList(queryInput: String, page: Int = 1, limit: Int = 10)
        fun getTicketTypeList()
        fun getTicketSlaList(type: String, queryInput: String)
        fun getCatTicketList(locationId: String)
        fun loadTicket()
        fun createTicket(input: TicketCreateBody, lat: Double, long: Double)
    }
}