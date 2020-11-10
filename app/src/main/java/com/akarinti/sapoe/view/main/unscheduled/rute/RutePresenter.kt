package com.akarinti.sapoe.view.main.unscheduled.rute

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.entity.LocationEntity
import com.akarinti.sapoe.data.entity.UnscheduleEntity
import com.akarinti.sapoe.data.response.CreateUnscheduleResponse
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject


class RutePresenter @Inject constructor(
    private val locationEntity: LocationEntity,
    private val unscheduleEntity: UnscheduleEntity

) : BasePresenter<RuteContract.View>(), RuteContract.Presenter {
    override fun createUnschedule(locationId: String, type: String, lat: Double, long: Double) {
        addSubscription(unscheduleEntity.createUnschedule(locationId, type, lat, long).uiSubscribe({
            val wrapper = it.convertResponse(TypeToken.get(CreateUnscheduleResponse::class.java))
            if (null != wrapper.response?.data && it.code() == HttpURLConnection.HTTP_OK) {
                view?.successCreateRoute(wrapper.response.data.detail)
            } else {
                view?.errorScreen(wrapper.json.getErrorMessage(it.message()), it.code())
            }
        }, {view?.errorScreen(it.message)}, {}))
    }

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

    override fun loadRute() {

    }

}