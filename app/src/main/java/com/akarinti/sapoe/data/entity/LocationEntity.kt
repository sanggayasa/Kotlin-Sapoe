package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.LocationApi
import com.akarinti.sapoe.data.body.LocationRequestBody
import com.akarinti.sapoe.data.body.LocationTrackingBody
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject

open class LocationEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<LocationApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<LocationApi> = LocationApi::class.java

    fun getLocationList(query: String? = "", sorting: String? = "name:asc", page: Int, limit: Int)
            = networkService().getLocationList(query, sorting, page, limit)

    fun sendLocationRequest(locationID: String, lat: Double, long: Double)
            = networkService().sendLocationRequest(LocationRequestBody(locationID, lat, long))

    fun sendLocationTracking(lat: Double, long: Double, deviceName: String, deviceImei: String)
            = networkService().sendLocationTracking(LocationTrackingBody(lat, long, deviceName, deviceImei))
}