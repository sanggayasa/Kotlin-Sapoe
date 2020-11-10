package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.ScheduleApi
import com.akarinti.sapoe.data.body.ChangeStatusBody
import com.akarinti.sapoe.data.body.LogOrderBody
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.body.SuhuBody
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ScheduleEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<ScheduleApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<ScheduleApi> = ScheduleApi::class.java

    fun getListSchedule(type: String, status: String, page: Int, limit: Int) = networkService().getListSchedule(type, status, page, limit)
    fun getListScheduleDistance(type: String, status: String, lat: Double, long: Double, page: Int, limit: Int) =
        networkService().getListScheduleDistance(type, status, lat, long, page, limit)
    fun getQuestionnaire(orderID: String, category: String, itemID: String = "", formID: String? = "")
            = networkService().getQuestionnaire(orderID, category, itemID, formID)
    fun getQuestionnaireOffline(orderID: String, category: String, lat: Double, long: Double)
            = networkService().getQuestionnaireOffline(orderID, category, lat, long)
    fun getAnswer(orderID: String, category: String)
            = networkService().getAnswer(orderID, category)
    fun createSuhu(category: String, orderId: String, orderType: String, picture: String, temperature: String)
            = networkService().createSuhu(SuhuBody(category = category, orderId = orderId, orderType = orderType,picture = picture,temperature = temperature))
    fun getQuestionDetail(questionID: String) = networkService().getQuestionDetail(questionID)
    fun changeScheduleStatusValidated(orderID: String, status: String, lat: Double, long: Double)
            = networkService().changeScheduleStatusValidated(ChangeStatusBody(orderID, status, lat, long), lat, long)
    fun submitAnswer(body: SaveAnswerBody) = networkService().submitAnswer(body)
    fun submitLogHistory(input: LogOrderBody) = networkService().submitHistory(input)
}