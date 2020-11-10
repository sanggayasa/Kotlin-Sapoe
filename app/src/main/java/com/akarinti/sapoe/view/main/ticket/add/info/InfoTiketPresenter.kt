package com.akarinti.sapoe.view.main.ticket.add.info

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.body.UpdateTicketBody
import com.akarinti.sapoe.data.entity.TicketEntity
import com.akarinti.sapoe.data.repository.ImageRepository
import com.akarinti.sapoe.data.repository.ProfileRepository
import com.akarinti.sapoe.data.response.TicketDetailResponse
import com.akarinti.sapoe.event.TicketRefreshEvent
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.model.answer.ImageResult
import com.akarinti.sapoe.objects.RxBus
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


class InfoTiketPresenter @Inject constructor(
    val profileRepository: ProfileRepository,
    private val ticketEntity: TicketEntity,
    private val imageRepository: ImageRepository
    ) : BasePresenter<InfoTiketContract.View>(), InfoTiketContract.Presenter {

    override fun getRepository(): List<ImageResult> = imageRepository.imageResult ?: ArrayList()

    override fun getTicketDetail(ticketID: String) {
        addSubscription(ticketEntity.getTicketDetail(ticketID).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(TicketDetailResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onTicketDetail(wrapper.response.data.detail, profileRepository.profile!!.id!!)
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

    override fun takeTicket(ticketID: String, lat: Double, long: Double) {
        addSubscription(ticketEntity.takeTicket(ticketID, profileRepository.profile!!.id!!, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                getTicketDetail(ticketID)
                RxBus.publish(TicketRefreshEvent())
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

    override fun closeTicket(ticketID: String, input: UpdateTicketBody, lat: Double, long: Double) {
        addSubscription(ticketEntity.updateTicketStatus(ticketID, lat, long, input).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                view?.onCloseTicket()
                RxBus.publish(TicketRefreshEvent())
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {}))
    }

    override fun deleteSparepart(sparepartID: String, ticketID: String) {
        addSubscription(ticketEntity.deleteSparepart(sparepartID).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
            if (it.code() == HttpURLConnection.HTTP_OK) {
                getTicketDetail(ticketID)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {
            if (it is ConnectException || it is SocketTimeoutException) {
                view?.errorConnection()
            } else {
                view?.errorScreen(it.message)
            }
        }, {}))
    }
}