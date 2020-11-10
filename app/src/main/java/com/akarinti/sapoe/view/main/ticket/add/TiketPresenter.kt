package com.akarinti.sapoe.view.main.ticket.add

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.body.TicketCreateBody
import com.akarinti.sapoe.data.entity.LocationEntity
import com.akarinti.sapoe.data.entity.TicketEntity
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.response.*
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


class TiketPresenter @Inject constructor(
    private val locationEntity: LocationEntity,
    private val ticketEntity: TicketEntity,
    val inprogressRepository: InprogressRepository
) : BasePresenter<TiketContract.View>(),
    TiketContract.Presenter {
    override fun getCatTicketList(locationId:String) {
        addSubscription(ticketEntity.getCatTicket(locationId).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(TicketCatResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onCatTicket(wrapper.response.data.list)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))
    }

    override fun createTicket(input: TicketCreateBody, lat: Double, long: Double) {
        addSubscription(ticketEntity.createTicket(input, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(CreateTicketResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.gotoNextPage()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))    }

    override fun getTicketSlaList(type: String, queryInput: String) {
        addSubscription(ticketEntity.getTicketSla(type, queryInput).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(TicketSlaResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onTicketSla(wrapper.response.data.list)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {})
        )
    }

    override fun getTicketTypeList() {
        addSubscription(ticketEntity.getTicketType().uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(TicketTypeResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onTicketType(wrapper.response.data.list)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {})
        )    }

    override fun getLocationList(queryInput: String, page: Int, limit: Int) {
        addSubscription(locationEntity.getLocationList(query = queryInput, page = 1, limit = 100).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(LocationListResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onLocationSet(wrapper.response.data)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {})
        )
    }
    override fun loadTicket() {
    }

}