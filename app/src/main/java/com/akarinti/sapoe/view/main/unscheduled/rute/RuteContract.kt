package com.akarinti.sapoe.view.main.unscheduled.rute

import com.akarinti.sapoe.base.ErrorView
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.model.Unschedule

interface RuteContract {
    interface View: ErrorView {
        fun successCreateRoute(data: Unschedule?)
        fun onLocationSet(data: LocationListResponse.LocationListData)
    }

    interface Presenter{
        fun loadRute()
        fun getLocationList(queryInput: String, page: Int = 1, limit: Int = 10)
        fun createUnschedule(locationId: String, type: String, lat: Double, long: Double)
    }
}