package com.akarinti.sapoe.data.entity

import android.content.Context
import com.akarinti.sapoe.data.AuthAbstractNetwork
import com.akarinti.sapoe.data.api.AuthApi
import com.akarinti.sapoe.data.body.LoginBody
import com.akarinti.sapoe.data.header.HeaderManager
import com.akarinti.sapoe.data.interceptor.AuthInterceptor
import javax.inject.Inject

open class AuthEntity @Inject constructor(headerManager: HeaderManager, authInterceptor: AuthInterceptor, context: Context)
    : AuthAbstractNetwork<AuthApi>(headerManager,authInterceptor,context) {

    override fun getApi(): Class<AuthApi> = AuthApi::class.java

    fun loginUser(username: String, password: String, deviceImei: String)
            = networkService().login(LoginBody(username = username, password = password, imei = deviceImei))
    fun logoutUser()
            = networkService().logout()

}