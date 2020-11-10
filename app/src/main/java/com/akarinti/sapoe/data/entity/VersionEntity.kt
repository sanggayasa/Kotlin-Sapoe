package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.VersionApi
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class VersionEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<VersionApi>(headerManager, authInterceptor, context) {

    override fun getApi(): Class<VersionApi> = VersionApi::class.java

    fun checkVersion(version: String, code: Int) = networkService().checkVersion(version, code)

}