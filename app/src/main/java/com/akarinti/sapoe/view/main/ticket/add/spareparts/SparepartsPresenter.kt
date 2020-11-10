package com.akarinti.sapoe.view.main.ticket.add.spareparts

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.body.SparepartsBody
import com.akarinti.sapoe.data.entity.TicketEntity
import com.akarinti.sapoe.data.response.SparepartListResponse
import com.akarinti.sapoe.data.response.SparepartsCategoryResponse
import com.akarinti.sapoe.data.response.SparepartsItemResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


class SparepartsPresenter @Inject constructor(
    private val ticketEntity: TicketEntity

) : BasePresenter<SparepartsContract.View>(),
    SparepartsContract.Presenter {
    override fun loadParts() {
    }

    override fun getSparepartsList(categoryId: String, q:String, cityID: String) {
        addSubscription(ticketEntity.getSpareparts(categoryId, q, cityID).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SparepartsItemResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onSparepartsList(wrapper.response.data.list)
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

    override fun getSparepartsCat() {
        addSubscription(ticketEntity.getSparepartsCategory().uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SparepartsCategoryResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onSparepartsCat(wrapper.response.data.list)
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

    override fun createSparepart(ticketId:String, input:SparepartsBody) {
        addSubscription(ticketEntity.updateSpareparts(ticketId,input).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SparepartListResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onCreateSparepart()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
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

    override fun createSparepartNext(ticketId: String, input: SparepartsBody) {
        addSubscription(ticketEntity.updateSpareparts(ticketId,input).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SparepartListResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.onCreateNext()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
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