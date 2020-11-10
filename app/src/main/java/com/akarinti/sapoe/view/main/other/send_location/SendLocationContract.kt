package com.akarinti.sapoe.view.main.other.send_location

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.LocationListResponse

interface SendLocationContract {
    interface View : ErrorView {
        fun onLocationSet(data: LocationListResponse.LocationListData)
        fun onLocationSend()
    }
    interface Presenter {
        fun getLocationList(queryInput: String, page: Int = 1, limit: Int = 10)
        fun sendLocation(locationID: String, lat: Double, long: Double)
    }
}