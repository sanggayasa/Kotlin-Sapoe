package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.SaveApi
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class SaveEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<SaveApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<SaveApi> = SaveApi::class.java

    fun createUnschedule(input: SaveAnswerBody) = networkService().saveAnswer(input)
}
