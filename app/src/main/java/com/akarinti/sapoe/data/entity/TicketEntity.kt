package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.TicketApi
import com.akarinti.sapoe.data.body.*
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class TicketEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<TicketApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<TicketApi> = TicketApi::class.java

    fun getListTicket(q: String, category: String, status: String, lat: Double, long: Double, orderId:String, page: Int, limit: Int) =
        networkService().getTicketAll(q, category, status, lat, long, orderId, page, limit)
    fun getTicketMe(category: String, status: String, lat: Double, long: Double, orderId:String, orderType:String, page: Int, limit: Int) =
        networkService().getTicketMe(category, status, lat, long, orderId, orderType, page, limit)
    fun getTicketType() = networkService().getTicketType()
    fun getTicketSla(type: String, queryInput: String) = networkService().getTicketSla(type, queryInput)
    fun getSparepartsCategory() = networkService().getSparepartsCategory()
    fun getSpareparts(categoryId: String, q:String, cityID: String) = networkService().getSpareparts(categoryId, q, cityID)
    fun getCatTicket(locationId:String) = networkService().getTicketLocation(locationId = locationId)
    fun createTicket(input:TicketCreateBody, lat: Double, long: Double) = networkService().createTicket(input, lat, long)
    fun changeTicketStatus(orderID: String, status: String)
            = networkService().changeTicketStatus(ChangeStatusBody(orderID, status))
    fun updateTicketStatus(ticketId: String, lat: Double, long: Double, body: UpdateTicketBody)
                = networkService().updateTicketStatus(ticketId,lat, long, body)
    fun updateSpareparts(ticketId: String, input:SparepartsBody)
                = networkService().updateSpareparts(ticketId,input)
    fun getTicketDetail(ticketID: String) = networkService().getTicketDetail(ticketID)
    fun takeTicket(ticketID: String, agentID: String, lat: Double, long: Double) = networkService().takeTicket(ticketID, lat, long, TicketAgentBody(agentID))
    fun deleteSparepart(sparepartID: String) = networkService().deleteSparepart(sparepartID)
}