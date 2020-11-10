package com.akarinti.sapoe.view.main.unscheduled.arsip

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.UnscheduleEntity
import com.akarinti.sapoe.data.response.UnscheduleListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class UnscheduledArsipPresenter @Inject constructor(
    private val unscheduleEntity: UnscheduleEntity
) : BasePresenter<UnscheduledArsipContract.View>(), UnscheduledArsipContract.Presenter {
    override fun getCompleteUnscheduleManual(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int, limit: Int) {
        addSubscription(unscheduleEntity.getListUnscheduleDistance(
                "",
                Params.UNSCHEDULE_MANUAL,
                Params.STATUS_COMPLETED,
                lat,
                long,
                page,
                limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(UnscheduleListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.gotoCompleteUnscheduleManualPager(wrapper.response)
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

    override fun getCompleteUnschedule(lat: Double, long: Double, isNetworkAvailable: Boolean, page: Int, limit: Int) {
        addSubscription(unscheduleEntity.getListUnscheduleDistance(
                "",
                Params.UNSCHEDULE_AUTO,
                Params.STATUS_COMPLETED,
                lat,
                long,
                page,
                limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(UnscheduleListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.gotoCompleteUnschedulePager(wrapper.response)
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