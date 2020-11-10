package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.UnscheduleApi
import com.akarinti.sapoe.data.body.ChangeStatusBody
import com.akarinti.sapoe.data.body.CreateUnscheduleBody
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UnscheduleEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<UnscheduleApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<UnscheduleApi> = UnscheduleApi::class.java

    fun getListUnschedule(type: String, scheduleType: String, status: String) = networkService().getListUnschedule(type, scheduleType, status)
    fun getListUnscheduleDistance(type: String, scheduleType: String, status: String, lat: Double, long: Double, page: Int, limit: Int) =
        networkService().getListUnscheduleDistance(type, scheduleType, status, lat, long, page, limit)
    fun createUnschedule(locationId: String, type: String, lat: Double, long: Double)
            = networkService().createUnschedule(CreateUnscheduleBody(locationId = locationId, type = type), lat, long)
    fun changeUncheduleStatusValidated(orderID: String, status: String, lat: Double, long: Double)
            = networkService().changeUnscheduleStatusValidated(ChangeStatusBody(orderID, status, lat, long), lat, long)
}