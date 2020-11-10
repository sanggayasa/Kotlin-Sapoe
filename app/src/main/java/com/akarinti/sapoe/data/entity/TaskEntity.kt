package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.TaskApi
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class TaskEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<TaskApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<TaskApi> = TaskApi::class.java

    fun getOrderTicketCount() = networkService().getOrderTicketCount()

    fun getNewsList(page: Int, limit: Int) = networkService().getNewsList(page, limit)

}