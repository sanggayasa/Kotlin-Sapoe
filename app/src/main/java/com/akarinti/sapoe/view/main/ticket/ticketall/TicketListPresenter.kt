package com.akarinti.sapoe.view.main.ticket.ticketall

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.TicketEntity
import com.akarinti.sapoe.data.response.TicketListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class TicketListPresenter @Inject constructor(
    private val tiketEntity: TicketEntity
) : BasePresenter<TicketListContract.View>(), TicketListContract.Presenter {
    override fun getTicket(q:String,lat: Double, long: Double, orderId:String, page: Int, limit: Int) {
        addSubscription(
            tiketEntity.getListTicket(
                q,
                "",
                "${Params.STATUS_OPEN}",
                lat,
                long,
                orderId,
                page,
                limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(TicketListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onTicketSet(wrapper.response)
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
}