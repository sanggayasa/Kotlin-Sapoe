package com.akarinti.sapoe.view.main.other.send_location

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.data.base.BaseResponse
import com.akarinti.sapoe.data.entity.LocationEntity
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.extension.convertResponse
import com.akarinti.sapoe.extension.getErrorMessage
import com.akarinti.sapoe.extension.uiSubscribe
import com.google.gson.reflect.TypeToken
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.inject.Inject

class SendLocationPresenter @Inject constructor(
    private val locationEntity: LocationEntity
) : BasePresenter<SendLocationContract.View>(), SendLocationContract.Presenter {

    override fun getLocationList(queryInput: String, page: Int, limit: Int) {
        addSubscription(locationEntity.getLocationList(query = queryInput, page = page, limit = limit).uiSubscribe({
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
        }, {}))
    }

    override fun sendLocation(locationID: String, lat: Double, long: Double) {
        addSubscription(
            locationEntity.sendLocationRequest(locationID, lat, long).uiSubscribe({
                val wrapper = it.convertResponse(TypeToken.get(BaseResponse::class.java))
                if (it.code() == HttpURLConnection.HTTP_OK) {
                    view?.onLocationSend()
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