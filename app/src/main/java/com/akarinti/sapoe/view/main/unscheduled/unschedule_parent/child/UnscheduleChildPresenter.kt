package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.child

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.TicketEntity
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.response.TicketListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


class UnscheduleChildPresenter @Inject constructor(
    val inprogressRepository: InprogressRepository,
    val ticketEntity: TicketEntity
) : BasePresenter<UnscheduleChildContract.View>(), UnscheduleChildContract.Presenter
{
    override fun getTicket(lat: Double, long: Double, orderId:String, orderType: String) {
        addSubscription(
            ticketEntity.getTicketMe(
                "",
                "",
                lat,
                long,
                orderId,
                orderType,
                1,
                20
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(TicketListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onTicketSet(wrapper.response.data.list)
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