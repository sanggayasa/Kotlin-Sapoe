package com.akarinti.sapoe.view.main.visit.arsip

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.response.ScheduleListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.akarinti.sapoe.objects.Params
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class VIsitArsipPresenter @Inject constructor(private val scheduleEntity: ScheduleEntity)
    : BasePresenter<VisitArsipContract.View>(), VisitArsipContract.Presenter {
    override fun getCompletedSchedule(page: Int, limit: Int) {
        addSubscription(scheduleEntity.getListSchedule(
                "",
                Params.STATUS_COMPLETED, page, limit
            ).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(ScheduleListResponse::class.java))
                if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onCompletedSchedulePager(wrapper.response)
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