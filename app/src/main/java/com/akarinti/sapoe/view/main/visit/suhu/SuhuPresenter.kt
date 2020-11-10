package com.akarinti.sapoe.view.main.visit.suhu

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.ScheduleEntity
import com.akarinti.sapoe.data.repository.InprogressRepository
import com.akarinti.sapoe.data.response.SuhuResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import javax.inject.Inject


class SuhuPresenter @Inject constructor(
    private val scheduleEntity: ScheduleEntity,
    val inprogressRepository: InprogressRepository

) : BasePresenter<SuhuContract.View>(), SuhuContract.Presenter {
    override fun createSuhuClean(
        category: String,
        orderId: String,
        orderType: String,
        picture: String,
        temperature: String
    ) {
        addSubscription(scheduleEntity.createSuhu(category, orderId, orderType, picture, temperature).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(SuhuResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.gotoNextPage()
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()))
            }
        }, {view?.errorScreen(it.message)}, {}))
    }

    override fun loadSuhu() {
    }

}